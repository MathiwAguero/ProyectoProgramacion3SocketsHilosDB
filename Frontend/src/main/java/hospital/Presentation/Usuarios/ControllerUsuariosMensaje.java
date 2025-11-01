package hospital.Presentation.Usuarios;

import hospital.Entities.Entities.UsuarioBase;
import hospital.Logic.NotificationManager;
import hospital.Logic.Service;
import hospital.Presentation.ThreadListener;

import java.util.ArrayList;
import java.util.List;

public class ControllerUsuariosMensaje implements ThreadListener {
    private Usuarios view;
    private ModelUsuariosMensaje model;

    public ControllerUsuariosMensaje(Usuarios view, ModelUsuariosMensaje model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        // Registrarse para recibir notificaciones
        NotificationManager.getInstance().register(this);

        // Cargar usuarios online inicialmente
        cargarUsuariosOnline();
    }

    /**
     * Carga la lista de usuarios online desde el servidor
     */
    public void cargarUsuariosOnline() {
        try {
            List<UsuarioBase> usuariosOnline = Service.getInstance().getUsuariosOnline();
            model.setList(usuariosOnline);
            System.out.println("✓ Usuarios online cargados: " + usuariosOnline.size());
        } catch (Exception e) {
            System.err.println("✗ Error cargando usuarios online: " + e.getMessage());
            model.setList(new ArrayList<>());
        }
    }

    /**
     * Envía un mensaje a un usuario
     */
    public void enviarMensaje(String destinatarioId, String mensaje) throws Exception {
        if (destinatarioId == null || destinatarioId.trim().isEmpty()) {
            throw new Exception("Debe seleccionar un destinatario");
        }

        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new Exception("El mensaje no puede estar vacío");
        }

        Service.getInstance().enviarMensaje(destinatarioId, mensaje);

        // Agregar mensaje al historial local
        model.agregarMensaje("Yo → " + destinatarioId + ": " + mensaje);
    }

    /**
     * Maneja notificaciones del servidor
     */
    @Override
    public void deliver_message(String message) {
        System.out.println("📨 Mensaje recibido: " + message);

        if (message.startsWith("USER_ONLINE:")) {
            // Usuario se conectó
            String userId = message.substring("USER_ONLINE:".length());
            System.out.println("✓ Usuario online: " + userId);
            cargarUsuariosOnline();

        } else if (message.startsWith("USER_OFFLINE:")) {
            // Usuario se desconectó
            String userId = message.substring("USER_OFFLINE:".length());
            System.out.println("✓ Usuario offline: " + userId);
            cargarUsuariosOnline();

        } else {
            // Es un mensaje de chat
            model.agregarMensaje("Recibido: " + message);
        }
    }

    /**
     * Limpia el historial de mensajes
     */
    public void limpiarMensajes() {
        model.setMensajes(new ArrayList<>());
    }

    /**
     * Desregistra el listener al cerrar
     */
    public void cleanup() {
        NotificationManager.getInstance().unregister(this);
        System.out.println("✓ ControllerUsuariosMensaje desregistrado");
    }
}