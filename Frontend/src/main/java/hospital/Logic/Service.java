package hospital.Logic;

import hospital.Entities.Entities.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;

    public Service() {
        try {
            s = new Socket(Protocol.SERVER, Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) {
            System.err.println("Error conectando al servidor: " + e.getMessage());
            System.exit(-1);
        }
    }

    // ============ PACIENTES ============
    public void create(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE DUPLICADO");
    }

    public Paciente readPaciente(String id) throws Exception {
        os.writeInt(Protocol.PACIENTE_READ);
        os.writeObject(id);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Paciente) is.readObject();
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public void update(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public void delete(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public List<Paciente> findAllPacientes() throws Exception {
        os.writeInt(Protocol.PACIENTE_SEARCH_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Paciente>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR PACIENTES");
    }

    public List<Paciente> findPacientesByNombre(String nombre) throws Exception {
        os.writeInt(Protocol.PACIENTE_SEARCH);
        os.writeObject(nombre);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Paciente>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR PACIENTES");
    }

    // ============ MEDICOS ============
    public void create(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICO DUPLICADO");
    }

    public Medico readMedico(String id) throws Exception {
        os.writeInt(Protocol.MEDICO_READ);
        os.writeObject(id);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medico) is.readObject();
        else throw new Exception("MEDICO NO EXISTE");
    }

    public void update(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICO NO EXISTE");
    }

    public void delete(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICO NO EXISTE");
    }

    public List<Medico> findAllMedicos() throws Exception {
        os.writeInt(Protocol.MEDICO_SEARCH_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Medico>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR MEDICOS");
    }

    // ============ FARMACEUTAS ============
    public void create(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACEUTA DUPLICADO");
    }

    public Farmaceuta readFarmaceuta(String id) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_READ);
        os.writeObject(id);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Farmaceuta) is.readObject();
        else throw new Exception("FARMACEUTA NO EXISTE");
    }

    public void update(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACEUTA NO EXISTE");
    }

    public void delete(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACEUTA NO EXISTE");
    }

    public List<Farmaceuta> findAllFarmaceutas() throws Exception {
        os.writeInt(Protocol.FARMACEUTA_SEARCH_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Farmaceuta>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR FARMACEUTAS");
    }

    // ============ MEDICAMENTOS ============
    public void create(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO DUPLICADO");
    }

    public Medicamento readMedicamento(String codigo) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_READ);
        os.writeObject(codigo);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medicamento) is.readObject();
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public void update(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public void delete(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public List<Medicamento> findAllMedicamentos() throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_SEARCH_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Medicamento>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR MEDICAMENTOS");
    }

    // ============ RECETAS ============
    public void create(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("RECETA DUPLICADA");
    }

    public Receta readReceta(String id) throws Exception {
        os.writeInt(Protocol.RECETA_READ);
        os.writeObject(id);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Receta) is.readObject();
        else throw new Exception("RECETA NO EXISTE");
    }

    public void update(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("RECETA NO EXISTE");
    }

    public void delete(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("RECETA NO EXISTE");
    }

    public List<Receta> findAllRecetas() throws Exception {
        os.writeInt(Protocol.RECETA_SEARCH_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Receta>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR RECETAS");
    }

    public List<Receta> findRecetasByPaciente(String pacienteId) throws Exception {
        os.writeInt(Protocol.RECETA_SEARCH_BY_PACIENTE);
        os.writeObject(pacienteId);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Receta>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR RECETAS");
    }

    public List<Receta> findRecetasByEstado(EstadoReceta estado) throws Exception {
        os.writeInt(Protocol.RECETA_SEARCH_BY_ESTADO);
        os.writeObject(estado);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Receta>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR RECETAS");
    }

    // ============ USUARIOS ============
    public UsuarioBase authenticate(String id, String clave) throws Exception {
        os.writeInt(Protocol.USUARIO_LOGIN);
        os.writeObject(id);
        os.writeObject(clave);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (UsuarioBase) is.readObject();
        else throw new Exception("CREDENCIALES INVÁLIDAS");
    }

    public List<UsuarioBase> findUsuariosActivos() throws Exception {
        os.writeInt(Protocol.USUARIO_SEARCH_ACTIVE);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<UsuarioBase>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR USUARIOS");
    }

    public void updateClave(String id, String nuevaClave) throws Exception {
        os.writeInt(Protocol.USUARIO_CHANGE_PASSWORD);
        os.writeObject(id);
        os.writeObject(nuevaClave);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("ERROR AL CAMBIAR CONTRASEÑA");
    }

    // ============ ADMINS ============
    public List<Admin> findAllAdmins() throws Exception {
        os.writeInt(Protocol.ADMIN_SEARCH_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Admin>) is.readObject();
        }
        else throw new Exception("ERROR AL BUSCAR ADMINS");
    }

    // ============ DETALLES ============
    public void createDetalle(RecipeDetails detalle, String recetaId) throws Exception {
        // Implementar si necesitas crear detalles individuales
        throw new Exception("NO IMPLEMENTADO");
    }

    public List<RecipeDetails> findAllDetalles() throws Exception {
        // Implementar si necesitas listar todos los detalles
        throw new Exception("NO IMPLEMENTADO");
    }

    public List<RecipeDetails> findDetallesByReceta(String recetaId) throws Exception {
        // Los detalles vienen dentro de la receta al hacer readReceta
        Receta r = readReceta(recetaId);
        return r.getDetalles();
    }

    // ============ CONTROL ============
    private void disconnect() throws Exception {
        os.writeInt(Protocol.DISCONNECT);
        os.flush();
        s.shutdownOutput();
        s.close();
    }

    public void stop() {
        try {
            disconnect();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    // ============ MÉTODOS AUXILIARES PARA COMPATIBILIDAD ============
    public boolean existsPaciente(String id) throws Exception {
        try {
            readPaciente(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsMedico(String id) throws Exception {
        try {
            readMedico(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsFarmaceuta(String id) throws Exception {
        try {
            readFarmaceuta(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsMedicamento(String codigo) throws Exception {
        try {
            readMedicamento(codigo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsReceta(String id) throws Exception {
        try {
            readReceta(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Métodos adicionales que tu aplicación usa
    public void create(UsuarioBase usuario) throws Exception {
        // Implementar según necesites
        throw new Exception("NO IMPLEMENTADO - usa authenticate()");
    }

    public UsuarioBase readUsuario(String id) throws Exception {
        throw new Exception("NO IMPLEMENTADO - usa authenticate()");
    }

    public void update(UsuarioBase usuario) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public void delete(UsuarioBase usuario) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public List<UsuarioBase> findAllUsuarios() throws Exception {
        throw new Exception("NO IMPLEMENTADO - usa findUsuariosActivos()");
    }

    public void setUsuarioActivo(String id, boolean activo) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public void create(Admin admin) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public Admin readAdmin(String id) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public List<Admin> findAdminsByClave(String clave) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public List<Medicamento> findMedicamentosByNombre(String nombre) throws Exception {
        // Implementar búsqueda por nombre en el servidor si la necesitas
        List<Medicamento> todos = findAllMedicamentos();
        return todos.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public List<Medico> findMedicosByNombre(String nombre) throws Exception {
        // Similar al anterior
        List<Medico> todos = findAllMedicos();
        return todos.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public List<Farmaceuta> findFarmaceutasByNombre(String nombre) throws Exception {
        List<Farmaceuta> todos = findAllFarmaceutas();
        return todos.stream()
                .filter(f -> f.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public List<Paciente> findPacientesByTelefono(String telefono) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    // Métodos de mensajería (si los usas)
    public void enviarMensaje(Mensaje mensaje) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public List<Mensaje> findMensajesByDestinatario(String destinatarioId) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public List<Mensaje> findMensajesNoLeidos(String destinatarioId) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public void marcarMensajeComoLeido(int id) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public void deleteMensaje(int id) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }

    public void updateDetalle(RecipeDetails detalle) throws Exception {
        throw new Exception("NO IMPLEMENTADO");
    }
}