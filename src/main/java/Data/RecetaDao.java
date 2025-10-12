package Data;

import Logic.Entities.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    Database db;

    public RecetaDao() {
        db = Database.getDatabase();
    }

    public void create(Receta r) throws Exception {
        String sql = "INSERT INTO recetas (id, paciente_id, medico_id, fechaConfeccion, fechaRetiro, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getId());
        stm.setString(2, r.getPaciente().getId());
        stm.setString(3, r.getMedico().getId());
        stm.setString(4, r.getFechaConfeccion());
        stm.setString(5, r.getFechaRetiro());
        stm.setString(6, r.getEstado().name());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo crear la receta");
        }

        // Guardar los detalles de la receta
        if (r.getDetalles() != null && !r.getDetalles().isEmpty()) {
            RecipeDetailsDao detallesDao = new RecipeDetailsDao();
            for (RecipeDetails detalle : r.getDetalles()) {
                detallesDao.create(detalle, r.getId());
            }
        }
    }

    public Receta read(String id) throws Exception {
        String sql = "SELECT * FROM recetas r WHERE r.id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Receta r = from(rs, "r");

            // Cargar paciente
            PacienteDao pacienteDao = new PacienteDao();
            r.setPaciente(pacienteDao.read(rs.getString("r.paciente_id")));

            // Cargar médico
            MedicoDao medicoDao = new MedicoDao();
            r.setMedico(medicoDao.read(rs.getString("r.medico_id")));

            // Cargar detalles
            RecipeDetailsDao detallesDao = new RecipeDetailsDao();
            r.setDetalles(detallesDao.findByRecetaId(r.getId()));

            return r;
        } else {
            throw new Exception("Receta no encontrada");
        }
    }

    public void update(Receta r) throws Exception {
        String sql = "UPDATE recetas SET paciente_id = ?, medico_id = ?, fechaConfeccion = ?, " +
                "fechaRetiro = ?, estado = ? WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getPaciente().getId());
        stm.setString(2, r.getMedico().getId());
        stm.setString(3, r.getFechaConfeccion());
        stm.setString(4, r.getFechaRetiro());
        stm.setString(5, r.getEstado().name());
        stm.setString(6, r.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar la receta");
        }

        // Actualizar detalles: eliminar los viejos e insertar los nuevos
        RecipeDetailsDao detallesDao = new RecipeDetailsDao();
        detallesDao.deleteByRecetaId(r.getId());
        if (r.getDetalles() != null && !r.getDetalles().isEmpty()) {
            for (RecipeDetails detalle : r.getDetalles()) {
                detallesDao.create(detalle, r.getId());
            }
        }
    }

    public void delete(Receta r) throws Exception {
        // Primero eliminar los detalles (por la restricción de FK)
        RecipeDetailsDao detallesDao = new RecipeDetailsDao();
        detallesDao.deleteByRecetaId(r.getId());

        String sql = "DELETE FROM recetas WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo eliminar la receta");
        }
    }

    public List<Receta> findAll() throws Exception {
        List<Receta> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM recetas r";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            PacienteDao pacienteDao = new PacienteDao();
            MedicoDao medicoDao = new MedicoDao();
            RecipeDetailsDao detallesDao = new RecipeDetailsDao();

            while (rs.next()) {
                Receta r = from(rs, "r");
                r.setPaciente(pacienteDao.read(rs.getString("r.paciente_id")));
                r.setMedico(medicoDao.read(rs.getString("r.medico_id")));
                r.setDetalles(detallesDao.findByRecetaId(r.getId()));
                resultado.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todas las recetas: " + e.getMessage());
            e.printStackTrace();
        }
        return resultado;
    }

    public boolean exists(String id) throws Exception {
        try {
            String sql = "SELECT COUNT(*) FROM recetas WHERE id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet rs = db.executeQuery(stm);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando existencia de la receta: " + e.getMessage());
        }
        return false;
    }

    public List<Receta> findByPacienteId(String pacienteId) throws Exception {
        List<Receta> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM recetas r WHERE r.paciente_id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, pacienteId);
            ResultSet rs = db.executeQuery(stm);

            PacienteDao pacienteDao = new PacienteDao();
            MedicoDao medicoDao = new MedicoDao();
            RecipeDetailsDao detallesDao = new RecipeDetailsDao();

            while (rs.next()) {
                Receta r = from(rs, "r");
                r.setPaciente(pacienteDao.read(rs.getString("r.paciente_id")));
                r.setMedico(medicoDao.read(rs.getString("r.medico_id")));
                r.setDetalles(detallesDao.findByRecetaId(r.getId()));
                resultado.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando recetas por paciente: " + e.getMessage());
        }
        return resultado;
    }

    public List<Receta> findByMedicoId(String medicoId) throws Exception {
        List<Receta> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM recetas r WHERE r.medico_id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, medicoId);
            ResultSet rs = db.executeQuery(stm);

            PacienteDao pacienteDao = new PacienteDao();
            MedicoDao medicoDao = new MedicoDao();
            RecipeDetailsDao detallesDao = new RecipeDetailsDao();

            while (rs.next()) {
                Receta r = from(rs, "r");
                r.setPaciente(pacienteDao.read(rs.getString("r.paciente_id")));
                r.setMedico(medicoDao.read(rs.getString("r.medico_id")));
                r.setDetalles(detallesDao.findByRecetaId(r.getId()));
                resultado.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando recetas por médico: " + e.getMessage());
        }
        return resultado;
    }

    public List<Receta> findByEstado(EstadoReceta estado) throws Exception {
        List<Receta> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM recetas r WHERE r.estado = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, estado.name());
            ResultSet rs = db.executeQuery(stm);

            PacienteDao pacienteDao = new PacienteDao();
            MedicoDao medicoDao = new MedicoDao();
            RecipeDetailsDao detallesDao = new RecipeDetailsDao();

            while (rs.next()) {
                Receta r = from(rs, "r");
                r.setPaciente(pacienteDao.read(rs.getString("r.paciente_id")));
                r.setMedico(medicoDao.read(rs.getString("r.medico_id")));
                r.setDetalles(detallesDao.findByRecetaId(r.getId()));
                resultado.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando recetas por estado: " + e.getMessage());
        }
        return resultado;
    }

    public Receta from(ResultSet rs, String alias) throws SQLException {
        try {
            Receta r = new Receta();
            r.setId(rs.getString(alias + ".id"));
            r.setFechaConfeccion(rs.getString(alias + ".fechaConfeccion"));
            r.setFechaRetiro(rs.getString(alias + ".fechaRetiro"));
            String estadoStr = rs.getString(alias + ".estado");
            r.setEstado(EstadoReceta.valueOf(estadoStr));
            return r;
        } catch (SQLException e) {
            return null;
        }
    }
}