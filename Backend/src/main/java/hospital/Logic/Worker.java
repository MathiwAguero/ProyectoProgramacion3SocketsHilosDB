package hospital.Logic;


import hospital.Entities.Entities.Paciente;
import hospital.Entities.Entities.Protocol;
import hospital.Entities.Entities.*;
import hospital.Logic.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Worker extends Thread {
    Server srv;
    Socket s;
    Service service;
    ObjectOutputStream os;
    ObjectInputStream is;
    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;
    boolean continuar;

    public Worker(Server srv, Socket s, Service service,
                  ObjectOutputStream os, ObjectInputStream is, String sid) {
        this.srv = srv;
        this.s = s;
        this.service = service;
        this.os = os;
        this.is = is;
        this.sid = sid; // 🆕
    }
    private String usuarioId;
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }
    public void setAs(Socket as, ObjectOutputStream aos, ObjectInputStream ais) {
        this.as = as;
        this.aos = aos;
        this.ais = ais;
    }


    @Override
    public void run() {
        continuar = true;
        System.out.println("Worker comenzó a escuchar peticiones...");

        while (continuar) {
            try {
                int method = is.readInt();
                System.out.println("Operación recibida: " + method);

                switch (method) {
                    // ============ PACIENTES ============
                    case Protocol.PACIENTE_CREATE:
                        handlePacienteCreate();
                        srv.deliver_message(this, "Nuevo paciente creado");
                        break;
                    case Protocol.PACIENTE_READ:
                        handlePacienteRead();
                        break;
                    case Protocol.PACIENTE_UPDATE:
                        handlePacienteUpdate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.PACIENTE_DELETE:
                        handlePacienteDelete();
                        srv.deliver_message(this, "Elimina receta creada");
                        break;
                    case Protocol.PACIENTE_SEARCH:
                        handlePacienteSearch();
                        break;
                    case Protocol.PACIENTE_SEARCH_ALL:
                        handlePacienteSearchAll();
                        break;

                    // ============ MEDICOS ============
                    case Protocol.MEDICO_CREATE:
                        handleMedicoCreate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.MEDICO_READ:
                        handleMedicoRead();
                        break;
                    case Protocol.MEDICO_UPDATE:
                        handleMedicoUpdate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.MEDICO_DELETE:
                        handleMedicoDelete();
                        srv.deliver_message(this, "Elimina receta creada");
                        break;
                    case Protocol.MEDICO_SEARCH:
                        handleMedicoSearch();
                        break;
                    case Protocol.MEDICO_SEARCH_ALL:
                        handleMedicoSearchAll();
                        break;

                    // ============ FARMACEUTAS ============
                    case Protocol.FARMACEUTA_CREATE:
                        handleFarmaceutaCreate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.FARMACEUTA_READ:
                        handleFarmaceutaRead();
                        break;
                    case Protocol.FARMACEUTA_UPDATE:
                        handleFarmaceutaUpdate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.FARMACEUTA_DELETE:
                        handleFarmaceutaDelete();
                        srv.deliver_message(this, "Elimina receta creada");
                        break;
                    case Protocol.FARMACEUTA_SEARCH:
                        handleFarmaceutaSearch();
                        break;
                    case Protocol.FARMACEUTA_SEARCH_ALL:
                        handleFarmaceutaSearchAll();
                        break;

                    // ============ MEDICAMENTOS ============
                    case Protocol.MEDICAMENTO_CREATE:
                        handleMedicamentoCreate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.MEDICAMENTO_READ:
                        handleMedicamentoRead();
                        break;
                    case Protocol.MEDICAMENTO_UPDATE:
                        handleMedicamentoUpdate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.MEDICAMENTO_DELETE:
                        handleMedicamentoDelete();
                        srv.deliver_message(this, "Elimina receta creada");
                        break;
                    case Protocol.MEDICAMENTO_SEARCH:
                        handleMedicamentoSearch();
                        break;
                    case Protocol.MEDICAMENTO_SEARCH_ALL:
                        handleMedicamentoSearchAll();
                        break;

                    // ============ RECETAS ============
                    case Protocol.RECETA_CREATE:
                        handleRecetaCreate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.RECETA_READ:
                        handleRecetaRead();
                        break;
                    case Protocol.RECETA_UPDATE:
                        handleRecetaUpdate();
                        srv.deliver_message(this, "Nueva receta creada");
                        break;
                    case Protocol.RECETA_DELETE:
                        handleRecetaDelete();
                        srv.deliver_message(this, "Elimina receta creada");
                        break;
                    case Protocol.RECETA_SEARCH:
                        handleRecetaSearch();
                        break;
                    case Protocol.RECETA_SEARCH_ALL:
                        handleRecetaSearchAll();
                        break;
                    case Protocol.RECETA_SEARCH_BY_PACIENTE:  // ← ESTO FALTABA
                        handleRecetaSearchByPaciente();
                        break;
                    case Protocol.RECETA_SEARCH_BY_ESTADO:    // ← ESTO FALTABA
                        handleRecetaSearchByEstado();
                        break;

                    // ============ USUARIOS ============
                    case Protocol.USUARIO_LOGIN:
                        handleUsuarioLogin();
                        break;
                    case Protocol.USUARIO_CHANGE_PASSWORD:    // ← ESTO FALTABA
                        handleUsuarioChangePassword();
                        break;
                    case Protocol.USUARIO_SEARCH_ACTIVE:
                        handleUsuarioSearchActive();
                        break;

                    // ============ ADMINS ============
                    case Protocol.ADMIN_SEARCH_ALL:
                        handleAdminSearchAll();
                        break;

                    // ============ DESCONEXIÓN ============
                    case Protocol.DISCONNECT:
                        detener();
                        srv.remove(this);
                        return;
                    case Protocol.USUARIO_GET_ONLINE:
                        handleUsuariosOnline();
                        break;

                    case Protocol.MENSAJE_SEND:
                        handleMensajeEnviar();
                        break;

                    default:
                        System.err.println("Operación desconocida: " + method);
                        os.writeInt(Protocol.ERROR_ERROR);
                        os.flush();
                }

            } catch (IOException e) {
                System.err.println("Cliente desconectado inesperadamente");
                detener();
                return;
            } catch (Exception e) {
                System.err.println("Error procesando petición: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    // ==================== PACIENTES ====================
    private void handlePacienteCreate() throws IOException {
        try {
            Paciente p = (Paciente) is.readObject();
            service.create(p);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Paciente creado: " + p.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error creando paciente: " + ex.getMessage());
        }
        os.flush();
    }

    private void handlePacienteRead() throws IOException {
        try {
            String id = (String) is.readObject();
            Paciente p = service.readPaciente(id);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(p);
            System.out.println("✓ Paciente leído: " + id);
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error leyendo paciente: " + ex.getMessage());
        }
        os.flush();
    }

    private void handlePacienteUpdate() throws IOException {
        try {
            Paciente p = (Paciente) is.readObject();
            service.update(p);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Paciente actualizado: " + p.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error actualizando paciente: " + ex.getMessage());
        }
        os.flush();
    }

    private void handlePacienteDelete() throws IOException {
        try {
            Paciente p = (Paciente) is.readObject();
            service.delete(p);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Paciente eliminado: " + p.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error eliminando paciente: " + ex.getMessage());
        }
        os.flush();
    }

    private void handlePacienteSearch() throws IOException {
        try {
            String nombre = (String) is.readObject();
            List<Paciente> lista = service.findPacientesByNombre(nombre);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Búsqueda pacientes: " + lista.size() + " resultados");
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error buscando pacientes: " + ex.getMessage());
        }
        os.flush();
    }

    private void handlePacienteSearchAll() throws IOException {
        try {
            List<Paciente> lista = service.findAllPacientes();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Todos los pacientes: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error listando pacientes: " + ex.getMessage());
        }
        os.flush();
    }

    // ==================== MEDICOS ====================
    private void handleMedicoCreate() throws IOException {
        try {
            Medico m = (Medico) is.readObject();
            service.create(m);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Médico creado: " + m.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error creando médico: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicoRead() throws IOException {
        try {
            String id = (String) is.readObject();
            Medico m = service.readMedico(id);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(m);
            System.out.println("✓ Médico leído: " + id);
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error leyendo médico: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicoUpdate() throws IOException {
        try {
            Medico m = (Medico) is.readObject();
            service.update(m);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Médico actualizado: " + m.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error actualizando médico: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicoDelete() throws IOException {
        try {
            Medico m = (Medico) is.readObject();
            service.delete(m);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Médico eliminado: " + m.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error eliminando médico: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicoSearch() throws IOException {
        try {
            String nombre = (String) is.readObject();
            List<Medico> lista = service.findMedicosByNombre(nombre);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Búsqueda médicos: " + lista.size() + " resultados");
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error buscando médicos: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicoSearchAll() throws IOException {
        try {
            List<Medico> lista = service.findAllMedicos();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Todos los médicos: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error listando médicos: " + ex.getMessage());
        }
        os.flush();
    }

    // ==================== FARMACEUTAS ====================
    private void handleFarmaceutaCreate() throws IOException {
        try {
            Farmaceuta f = (Farmaceuta) is.readObject();
            service.create(f);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Farmaceuta creado: " + f.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error creando farmaceuta: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleFarmaceutaRead() throws IOException {
        try {
            String id = (String) is.readObject();
            Farmaceuta f = service.readFarmaceuta(id);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(f);
            System.out.println("✓ Farmaceuta leído: " + id);
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error leyendo farmaceuta: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleFarmaceutaUpdate() throws IOException {
        try {
            Farmaceuta f = (Farmaceuta) is.readObject();
            service.update(f);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Farmaceuta actualizado: " + f.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error actualizando farmaceuta: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleFarmaceutaDelete() throws IOException {
        try {
            Farmaceuta f = (Farmaceuta) is.readObject();
            service.delete(f);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Farmaceuta eliminado: " + f.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error eliminando farmaceuta: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleFarmaceutaSearch() throws IOException {
        try {
            String nombre = (String) is.readObject();
            List<Farmaceuta> lista = service.findFarmaceutasByNombre(nombre);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Búsqueda farmaceutas: " + lista.size() + " resultados");
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error buscando farmaceutas: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleFarmaceutaSearchAll() throws IOException {
        try {
            List<Farmaceuta> lista = service.findAllFarmaceutas();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Todos los farmaceutas: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error listando farmaceutas: " + ex.getMessage());
        }
        os.flush();
    }

    // ==================== MEDICAMENTOS ====================
    private void handleMedicamentoCreate() throws IOException {
        try {
            Medicamento m = (Medicamento) is.readObject();
            service.create(m);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Medicamento creado: " + m.getCodigo());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error creando medicamento: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicamentoRead() throws IOException {
        try {
            String codigo = (String) is.readObject();
            Medicamento m = service.readMedicamento(codigo);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(m);
            System.out.println("✓ Medicamento leído: " + codigo);
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error leyendo medicamento: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicamentoUpdate() throws IOException {
        try {
            Medicamento m = (Medicamento) is.readObject();
            service.update(m);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Medicamento actualizado: " + m.getCodigo());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error actualizando medicamento: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicamentoDelete() throws IOException {
        try {
            Medicamento m = (Medicamento) is.readObject();
            service.delete(m);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Medicamento eliminado: " + m.getCodigo());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error eliminando medicamento: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicamentoSearch() throws IOException {
        try {
            String nombre = (String) is.readObject();
            List<Medicamento> lista = service.findMedicamentosByNombre(nombre);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Búsqueda medicamentos: " + lista.size() + " resultados");
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error buscando medicamentos: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleMedicamentoSearchAll() throws IOException {
        try {
            List<Medicamento> lista = service.findAllMedicamentos();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Todos los medicamentos: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error listando medicamentos: " + ex.getMessage());
        }
        os.flush();
    }

    // ==================== RECETAS ====================
    private void handleRecetaCreate() throws IOException {
        try {
            Receta r = (Receta) is.readObject();

            // Debugging
            System.out.println("=== RECETA CREATE ===");
            System.out.println("ID: " + r.getId());
            System.out.println("Paciente: " + (r.getPaciente() != null ? r.getPaciente().getId() : "NULL"));
            System.out.println("Médico: " + (r.getMedico() != null ? r.getMedico().getId() : "NULL"));
            System.out.println("Estado: " + r.getEstado());
            System.out.println("Detalles: " + (r.getDetalles() != null ? r.getDetalles().size() : "0"));

            service.create(r);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Receta creada exitosamente: " + r.getId());

        } catch (Exception ex) {
            System.err.println("✗ ERROR CREANDO RECETA:");
            System.err.println("Tipo: " + ex.getClass().getName());
            System.err.println("Mensaje: " + ex.getMessage());

            // Enviar mensaje de error específico al cliente
            os.writeInt(Protocol.ERROR_ERROR);
            os.writeObject(ex.getMessage()); // ← AÑADIR ESTO
        }
        os.flush();
    }

    private void handleRecetaRead() throws IOException {
        try {
            String id = (String) is.readObject();
            Receta r = service.readReceta(id);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(r);
            System.out.println("✓ Receta leída: " + id);
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error leyendo receta: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleRecetaUpdate() throws IOException {
        try {
            Receta r = (Receta) is.readObject();
            service.update(r);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Receta actualizada: " + r.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error actualizando receta: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleRecetaDelete() throws IOException {
        try {
            Receta r = (Receta) is.readObject();
            service.delete(r);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Receta eliminada: " + r.getId());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error eliminando receta: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleRecetaSearch() throws IOException {
        try {
            String id = (String) is.readObject();
            List<Receta> lista = service.findAllRecetas().stream()
                    .filter(r -> r.getId().contains(id))
                    .toList();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Búsqueda recetas: " + lista.size() + " resultados");
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error buscando recetas: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleRecetaSearchAll() throws IOException {
        try {
            List<Receta> lista = service.findAllRecetas();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Todas las recetas: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error listando recetas: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleRecetaSearchByPaciente() throws IOException {
        try {
            String pacienteId = (String) is.readObject();
            List<Receta> lista = service.findRecetasByPaciente(pacienteId);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Recetas por paciente: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error buscando recetas por paciente: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleRecetaSearchByEstado() throws IOException {
        try {
            EstadoReceta estado = (EstadoReceta) is.readObject();
            List<Receta> lista = service.findRecetasByEstado(estado);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Recetas por estado: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error buscando recetas por estado: " + ex.getMessage());
        }
        os.flush();
    }

    // ==================== USUARIOS ====================
    private void handleUsuarioLogin() throws IOException {
        try {
            String id = (String) is.readObject();
            String clave = (String) is.readObject();
            UsuarioBase usuario = service.authenticate(id, clave);

            // Registrar usuario en este worker
            this.usuarioId = id;

            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(usuario);

            // Notificar a todos que este usuario está online
            srv.notifyUserOnline(id);

            System.out.println("✓ Login exitoso: " + id + " (Workers activos: " + srv.workers.size() + ")");
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error en login: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleUsuarioChangePassword() throws IOException {
        try {
            String id = (String) is.readObject();
            String nuevaClave = (String) is.readObject();
            service.updateClave(id, nuevaClave);
            os.writeInt(Protocol.ERROR_NO_ERROR);
            System.out.println("✓ Contraseña cambiada: " + id);
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error cambiando contraseña: " + ex.getMessage());
        }
        os.flush();
    }

    private void handleUsuarioSearchActive() throws IOException {
        try {
            List<UsuarioBase> lista = service.findUsuariosActivos();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Usuarios activos: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error listando usuarios activos: " + ex.getMessage());
        }
        os.flush();
    }

    // ==================== ADMINS ====================
    private void handleAdminSearchAll() throws IOException {
        try {
            List<Admin> lista = service.findAllAdmins();
            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(lista);
            System.out.println("✓ Todos los admins: " + lista.size());
        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error listando admins: " + ex.getMessage());
        }
        os.flush();
    }

    // ==================== CONTROL ====================

    public synchronized void deliver_message(String message) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_MESSAGE);
                aos.writeObject(message);
                aos.flush();
            } catch (Exception e) {
                System.err.println("Error enviando mensaje: " + e.getMessage());
            }
        }
    }
    private void handleUsuariosOnline() throws IOException {
        try {
            List<UsuarioBase> usuariosOnline = new ArrayList<>();

            // Recorrer todos los workers y obtener sus usuarios
            // EXCEPTO el usuario que hace la petición
            for (Worker w : srv.workers) {
                if (w.usuarioId != null && !w.usuarioId.isEmpty()) {
                    // ✅ Excluir al usuario actual
                    if (w.usuarioId.equals(this.usuarioId)) {
                        continue; // Saltar este usuario
                    }

                    try {
                        UsuarioBase usuario = Service.getInstance().readUsuario(w.usuarioId);
                        if (usuario != null) {
                            usuariosOnline.add(usuario);
                        }
                    } catch (Exception e) {
                        System.err.println("Error leyendo usuario " + w.usuarioId);
                    }
                }
            }

            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject(usuariosOnline);
            System.out.println("✓ Usuarios online enviados: " + usuariosOnline.size() +
                    " (excluido: " + this.usuarioId + ")");

        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            System.err.println("✗ Error obteniendo usuarios online: " + ex.getMessage());
        }
        os.flush();
    }

    /**
     * Envía un mensaje a otro usuario
     */
    private void handleMensajeEnviar() throws IOException {
        try {
            String destinatarioId = (String) is.readObject();
            String mensaje = (String) is.readObject();

            if (this.usuarioId == null) {
                os.writeInt(Protocol.ERROR_ERROR);
                os.writeObject("No hay usuario logueado");
                os.flush();
                return;
            }

            // Buscar el worker del destinatario
            Worker destinatario = null;
            for (Worker w : srv.workers) {
                if (destinatarioId.equals(w.usuarioId)) {
                    destinatario = w;
                    break;
                }
            }

            if (destinatario == null) {
                os.writeInt(Protocol.ERROR_ERROR);
                os.writeObject("Usuario destinatario no está online");
                os.flush();
                return;
            }

            // Enviar mensaje al destinatario
            destinatario.deliver_message(this.usuarioId + ": " + mensaje);

            os.writeInt(Protocol.ERROR_NO_ERROR);
            os.writeObject("Mensaje enviado correctamente");
            System.out.println("✓ Mensaje enviado de " + this.usuarioId + " a " + destinatarioId);

        } catch (Exception ex) {
            os.writeInt(Protocol.ERROR_ERROR);
            os.writeObject("Error enviando mensaje: " + ex.getMessage());
            System.err.println("✗ Error enviando mensaje: " + ex.getMessage());
        }
        os.flush();
    }
    public void detener() {
        // ✅ IMPORTANTE: Notificar ANTES de cerrar el socket
        if (usuarioId != null && !usuarioId.isEmpty()) {
            System.out.println("→ Notificando desconexión de: " + usuarioId);
            srv.notifyUserOffline(usuarioId);
        }

        continuar = false;

        // Remover este worker de la lista del servidor
        srv.remove(this);

        try {
            if (s != null && !s.isClosed()) {
                s.close();
            }
            // También cerrar socket asíncrono si existe
            if (as != null && !as.isClosed()) {
                as.close();
            }
        } catch (IOException e) {
            System.err.println("Error cerrando socket: " + e.getMessage());
        }

        System.out.println("✓ Worker detenido" + (usuarioId != null ? " (" + usuarioId + ")" : ""));
    }
}