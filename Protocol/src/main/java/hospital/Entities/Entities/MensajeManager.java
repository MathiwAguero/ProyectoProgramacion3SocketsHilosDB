package hospital.Entities.Entities;


import hospital.Entities.Entities.Mensaje;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Gestor de mensajes en memoria (sin base de datos)
 */
public class MensajeManager {
    private static MensajeManager instance;

    // Mapa: "usuario1-usuario2" -> Lista de mensajes
    private Map<String, List<Mensaje>> conversaciones;
    private int contadorId = 1;

    private MensajeManager() {
        conversaciones = new ConcurrentHashMap<>();
    }

    public static synchronized MensajeManager getInstance() {
        if (instance == null) {
            instance = new MensajeManager();
        }
        return instance;
    }

    /**
     * Genera clave única para la conversación (ordenada alfabéticamente)
     */
    private String generarClave(String usuario1, String usuario2) {
        if (usuario1.compareTo(usuario2) < 0) {
            return usuario1 + "-" + usuario2;
        } else {
            return usuario2 + "-" + usuario1;
        }
    }

    /**
     * Guarda un mensaje
     */
    public void guardarMensaje(String remitenteId, String destinatarioId, String texto) {
        String clave = generarClave(remitenteId, destinatarioId);

        Mensaje mensaje = new Mensaje();
        mensaje.setId(contadorId++);
        mensaje.setRemitenteId(remitenteId);
        mensaje.setDestinatarioId(destinatarioId);
        mensaje.setMensaje(texto);
        mensaje.setFecha(new Timestamp(System.currentTimeMillis()));
        mensaje.setLeido(false);

        conversaciones.computeIfAbsent(clave, k -> new ArrayList<>()).add(mensaje);

        System.out.println("✓ Mensaje guardado: " + remitenteId + " -> " + destinatarioId);
    }

    /**
     * Obtiene todos los mensajes entre dos usuarios
     */
    public List<Mensaje> obtenerMensajes(String usuario1, String usuario2) {
        String clave = generarClave(usuario1, usuario2);
        return new ArrayList<>(conversaciones.getOrDefault(clave, new ArrayList<>()));
    }

    /**
     * Marca como leídos los mensajes que un usuario envió a otro
     */
    public void marcarComoLeido(String remitenteId, String destinatarioId) {
        String clave = generarClave(remitenteId, destinatarioId);

        List<Mensaje> mensajes = conversaciones.get(clave);
        if (mensajes != null) {
            mensajes.stream()
                    .filter(m -> m.getRemitenteId().equals(remitenteId) &&
                            m.getDestinatarioId().equals(destinatarioId))
                    .forEach(m -> m.setLeido(true));
        }
    }

    /**
     * Cuenta mensajes no leídos de un usuario hacia otro
     */
    public int contarNoLeidos(String remitenteId, String destinatarioId) {
        String clave = generarClave(remitenteId, destinatarioId);

        List<Mensaje> mensajes = conversaciones.get(clave);
        if (mensajes == null) return 0;

        return (int) mensajes.stream()
                .filter(m -> m.getRemitenteId().equals(remitenteId) &&
                        m.getDestinatarioId().equals(destinatarioId) &&
                        !m.isLeido())
                .count();
    }

    /**
     * Obtiene todos los usuarios con los que alguien ha chateado
     */
    public List<String> obtenerContactos(String usuarioId) {
        Set<String> contactos = new HashSet<>();

        for (String clave : conversaciones.keySet()) {
            String[] usuarios = clave.split("-");
            if (usuarios[0].equals(usuarioId)) {
                contactos.add(usuarios[1]);
            } else if (usuarios[1].equals(usuarioId)) {
                contactos.add(usuarios[0]);
            }
        }

        return new ArrayList<>(contactos);
    }

    /**
     * Limpia todos los mensajes (útil para reiniciar)
     */
    public void limpiarTodo() {
        conversaciones.clear();
        contadorId = 1;
    }

    /**
     * Limpia conversación específica
     */
    public void limpiarConversacion(String usuario1, String usuario2) {
        String clave = generarClave(usuario1, usuario2);
        conversaciones.remove(clave);
    }

    /**
     * Obtiene el PRIMER mensaje no leído que remitenteId envió a destinatarioId
     * Retorna null si no hay mensajes pendientes
     */
    public Mensaje obtenerPrimerMensajeNoLeido(String remitenteId, String destinatarioId) {
        String clave = generarClave(remitenteId, destinatarioId);

        List<Mensaje> mensajes = conversaciones.get(clave);
        if (mensajes == null) return null;

        // Buscar el primer mensaje no leído del remitente hacia el destinatario
        for (Mensaje msg : mensajes) {
            if (msg.getRemitenteId().equals(remitenteId) &&
                    msg.getDestinatarioId().equals(destinatarioId) &&
                    !msg.isLeido()) {
                return msg;
            }
        }

        return null; // No hay mensajes pendientes
    }

    /**
     * Marca UN mensaje específico como leído
     */
    public void marcarMensajeComoLeido(int mensajeId) {
        for (List<Mensaje> lista : conversaciones.values()) {
            for (Mensaje msg : lista) {
                if (msg.getId() == mensajeId) {
                    msg.setLeido(true);
                    return;
                }
            }
        }
    }
    public int contarMensajesPendientes(String remitenteId, String destinatarioId) {
        String clave = generarClave(remitenteId, destinatarioId);

        List<Mensaje> mensajes = conversaciones.get(clave);
        if (mensajes == null) return 0;

        return (int) mensajes.stream()
                .filter(m -> m.getRemitenteId().equals(remitenteId) &&
                        m.getDestinatarioId().equals(destinatarioId) &&
                        !m.isLeido())
                .count();
    }
}