package hospital.Logic;

import hospital.Data.*;
import hospital.Entities.Entities.*;

import java.util.List;


public class Service {
    private static Service instance;

    public static synchronized Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    // DAOs
    private PacienteDao pacienteDao;
    private FarmaceutaDao farmaceutaDao;
    private MedicoDao medicoDao;
    private AdminDao adminDao;
    private MedicamentoDao medicamentoDao;
    private RecetaDao recetaDao;
    private RecipeDetailsDao detallesDao;
    private UsuarioDao usuarioDao;
    private MensajeDao mensajeDao;

    private Service() {
        try {
            pacienteDao = new PacienteDao();
            farmaceutaDao = new FarmaceutaDao();
            medicoDao = new MedicoDao();
            adminDao = new AdminDao();
            medicamentoDao = new MedicamentoDao();
            recetaDao = new RecetaDao();
            detallesDao = new RecipeDetailsDao();
            usuarioDao = new UsuarioDao();
            mensajeDao = new MensajeDao();
        } catch (Exception e) {
            System.err.println("Error inicializando Service: " + e.getMessage());
            System.exit(-1);
        }
    }

    public void stop() {
        try {
            Database.getDatabase().close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ============ PACIENTES ============
    public void create(Paciente paciente) throws Exception {
        pacienteDao.crate(paciente);
    }

    public Paciente readPaciente(String id) throws Exception {
        return pacienteDao.read(id);
    }

    public void update(Paciente paciente) throws Exception {
        pacienteDao.update(paciente);
    }

    public void delete(Paciente paciente) throws Exception {
        pacienteDao.delete(paciente);
    }

    public List<Paciente> findAllPacientes() throws Exception {
        return pacienteDao.findAll();
    }

    public boolean existsPaciente(String id) throws Exception {
        return pacienteDao.exists(id);
    }

    public List<Paciente> findPacientesByNombre(String nombre) throws Exception {
        return pacienteDao.searchByName(nombre);
    }

    public List<Paciente> findPacientesByTelefono(String telefono) throws Exception {
        return pacienteDao.searchById(telefono);
    }

    // ============ FARMACEUTAS ============
    public void create(Farmaceuta farmaceuta) throws Exception {
        farmaceutaDao.create(farmaceuta);
    }

    public Farmaceuta readFarmaceuta(String id) throws Exception {
        return farmaceutaDao.read(id);
    }

    public void update(Farmaceuta farmaceuta) throws Exception {
        farmaceutaDao.update(farmaceuta);
    }

    public void delete(Farmaceuta farmaceuta) throws Exception {
        farmaceutaDao.delete(farmaceuta);
    }

    public List<Farmaceuta> findAllFarmaceutas() throws Exception {
        return farmaceutaDao.findAll();
    }

    public List<Farmaceuta> findFarmaceutasByNombre(String nombre) throws Exception {
        return farmaceutaDao.findByNombre(nombre);
    }

    public boolean existsFarmaceuta(String id) throws Exception {
        return farmaceutaDao.exists(id);
    }

    // ============ MEDICOS ============
    public void create(Medico medico) throws Exception {
        medicoDao.create(medico);
    }

    public Medico readMedico(String id) throws Exception {
        return medicoDao.read(id);
    }

    public void update(Medico medico) throws Exception {
        medicoDao.update(medico);
    }

    public void delete(Medico medico) throws Exception {
        medicoDao.delete(medico);
    }

    public List<Medico> findAllMedicos() throws Exception {
        return medicoDao.findAll();
    }

    public List<Medico> findMedicosByNombre(String nombre) throws Exception {
        return medicoDao.searchByName(nombre);
    }

    public boolean existsMedico(String id) throws Exception {
        return medicoDao.exists(id);
    }

    // ============ ADMINISTRADORES ============
    public void create(Admin admin) throws Exception {
        adminDao.create(admin);
    }

    public Admin readAdmin(String id) throws Exception {
        return adminDao.read(id);
    }

    public List<Admin> findAllAdmins() throws Exception {
        return adminDao.findAll();
    }

    public List<Admin> findAdminsByClave(String clave) throws Exception {
        return adminDao.findByClave(clave);
    }

    // ============ MEDICAMENTOS ============
    public void create(Medicamento medicamento) throws Exception {
        medicamentoDao.create(medicamento);
    }

    public Medicamento readMedicamento(String codigo) throws Exception {
        return medicamentoDao.read(codigo);
    }

    public void update(Medicamento medicamento) throws Exception {
        medicamentoDao.update(medicamento);
    }

    public void delete(Medicamento medicamento) throws Exception {
        medicamentoDao.delete(medicamento);
    }

    public List<Medicamento> findAllMedicamentos() throws Exception {
        return medicamentoDao.findAll();
    }

    public List<Medicamento> findMedicamentosByNombre(String nombre) throws Exception {
        return medicamentoDao.findByNombre(nombre);
    }

    public boolean existsMedicamento(String codigo) throws Exception {
        return medicamentoDao.exists(codigo);
    }

    // ============ RECETAS ============
    public void create(Receta receta) throws Exception {
        if (existsReceta(receta.getId())) {
            throw new Exception("RECETA DUPLICADA: " + receta.getId());
        }
        recetaDao.create(receta);
    }

    public Receta readReceta(String id) throws Exception {
        return recetaDao.read(id);
    }

    public void update(Receta receta) throws Exception {
        recetaDao.update(receta);
    }

    public void delete(Receta receta) throws Exception {
        recetaDao.delete(receta);
    }

    public List<Receta> findAllRecetas() throws Exception {
        return recetaDao.findAll();
    }

    public List<Receta> findRecetasByPaciente(String pacienteId) throws Exception {
        return recetaDao.findByPacienteId(pacienteId);
    }

    public List<Receta> findRecetasByEstado(EstadoReceta estado) throws Exception {
        return recetaDao.findByEstado(estado);
    }

    public boolean existsReceta(String id) throws Exception {
        return recetaDao.exists(id);
    }

    // ============ DETALLES DE RECETAS ============
    public void createDetalle(RecipeDetails detalle, String recetaId) throws Exception {
        detallesDao.create(detalle, recetaId);
    }

    public void updateDetalle(RecipeDetails detalle) throws Exception {
        detallesDao.update(detalle);
    }

    public List<RecipeDetails> findDetallesByReceta(String recetaId) throws Exception {
        return detallesDao.findByRecetaId(recetaId);
    }

    public List<RecipeDetails> findAllDetalles() throws Exception {
        return detallesDao.findAll();
    }

    // ============ USUARIOS ============
    public void create(UsuarioBase usuario) throws Exception {
        usuarioDao.create(usuario);
    }

    public UsuarioBase readUsuario(String id) throws Exception {
        return usuarioDao.read(id);
    }

    public void update(UsuarioBase usuario) throws Exception {
        usuarioDao.update(usuario);
    }

    public void delete(UsuarioBase usuario) throws Exception {
        usuarioDao.delete(usuario);
    }

    public List<UsuarioBase> findAllUsuarios() throws Exception {
        return usuarioDao.findAll();
    }

    public UsuarioBase authenticate(String id, String clave) throws Exception {
        return usuarioDao.authenticate(id, clave);
    }

    public void setUsuarioActivo(String id, boolean activo) throws Exception {
        usuarioDao.setActivo(id, activo);
    }

    public List<UsuarioBase> findUsuariosActivos() throws Exception {
        return usuarioDao.findActivos();
    }

    public void updateClave(String id, String nuevaClave) throws Exception {
        usuarioDao.updateClave(id, nuevaClave);
    }

    // ============ MENSAJES ============
    public void enviarMensaje(Mensaje mensaje) throws Exception {
        mensajeDao.create(mensaje);
    }

    public List<Mensaje> findMensajesByDestinatario(String destinatarioId) throws Exception {
        return mensajeDao.findByDestinatario(destinatarioId);
    }

    public List<Mensaje> findMensajesNoLeidos(String destinatarioId) throws Exception {
        return mensajeDao.findNoLeidos(destinatarioId);
    }

    public void marcarMensajeComoLeido(int id) throws Exception {
        mensajeDao.marcarComoLeido(id);
    }

    public void deleteMensaje(int id) throws Exception {
        mensajeDao.delete(id);
    }

    // ============ RECETAS - BÚSQUEDAS ADICIONALES ============
    public List<Receta> findRecetasByMedico(String medicoId) throws Exception {
        return recetaDao.findByMedicoId(medicoId);
    }

    // ============ DETALLES - OPERACIONES COMPLETAS ============
    public void updateDetalle(RecipeDetails detalle, String recetaId) throws Exception {
        // Primero obtenemos la receta
        Receta receta = recetaDao.read(recetaId);
        if (receta == null) {
            throw new Exception("Receta no encontrada");
        }

        // Buscamos el detalle a actualizar
        List<RecipeDetails> detalles = receta.getDetalles();
        boolean encontrado = false;

        for (int i = 0; i < detalles.size(); i++) {
            if (detalles.get(i).getCodigoMedicamento().equals(detalle.getCodigoMedicamento())) {
                detalles.set(i, detalle);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new Exception("Detalle no encontrado en la receta");
        }

        // Actualizamos la receta completa
        receta.setDetalles(detalles);
        recetaDao.update(receta);
    }

    public void deleteDetalle(String codigoMedicamento, String recetaId) throws Exception {
        Receta receta = recetaDao.read(recetaId);
        if (receta == null) {
            throw new Exception("Receta no encontrada");
        }

        List<RecipeDetails> detalles = receta.getDetalles();
        boolean removed = detalles.removeIf(d ->
                d.getCodigoMedicamento().equals(codigoMedicamento)
        );

        if (!removed) {
            throw new Exception("Detalle no encontrado en la receta");
        }

        receta.setDetalles(detalles);
        recetaDao.update(receta);
    }

    // ============ VALIDACIONES ============
    public boolean validarUsuario(String id, String clave) throws Exception {
        try {
            UsuarioBase usuario = usuarioDao.authenticate(id, clave);
            return usuario != null;
        } catch (Exception e) {
            return false;
        }
    }
}