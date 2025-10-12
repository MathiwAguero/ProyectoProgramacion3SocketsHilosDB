package Data;

import Logic.Entities.RecipeDetails;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsDao {
    Database db;

    public RecipeDetailsDao() {
        db = Database.getDatabase();
    }

    /**
     * Crea un detalle de receta asociado a una receta específica
     */
    public void create(RecipeDetails detalle, String recetaId) throws Exception {
        String sql = "INSERT INTO receta_detalles (receta_id, codigoMedicamento, nombre, cantidad, indicaciones, duracionTratamiento) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, recetaId);
        stm.setString(2, detalle.getCodigoMedicamento());
        stm.setString(3, detalle.getNombre());
        stm.setInt(4, detalle.getCantidad());
        stm.setString(5, detalle.getIndicaciones());
        stm.setInt(6, detalle.getDuracionTratamiento());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo crear el detalle de receta");
        }
    }
    public void update(RecipeDetails detalle) throws Exception {
        String sql = "UPDATE receta_detalles SET receta_id=?, codigoMedicamento=?,nombre=?," +
                "cantidad=?,indicaciones=?,duracionTratamiento=? WHERE receta_id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, detalle.getCodigoMedicamento());
        stm.setString(2, detalle.getNombre());
        stm.setInt(3, detalle.getCantidad());
        stm.setString(4, detalle.getIndicaciones());
        stm.setInt(5, detalle.getDuracionTratamiento());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar el detalle de receta");
        }
    }

    /**
     * Lee un detalle específico por su ID
     */
    public RecipeDetails read(int id) throws Exception {
        String sql = "SELECT * FROM receta_detalles rd WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "rd");
        } else {
            throw new Exception("Detalle de receta no encontrado");
        }
    }

    /**
     * Encuentra todos los detalles asociados a una receta específica
     */
    public List<RecipeDetails> findByRecetaId(String recetaId) throws Exception {
        List<RecipeDetails> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM receta_detalles rd WHERE receta_id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, recetaId);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "rd"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando detalles de receta: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * Elimina todos los detalles asociados a una receta
     * Útil antes de actualizar o eliminar una receta
     */
    public void deleteByRecetaId(String recetaId) throws Exception {
        String sql = "DELETE FROM receta_detalles WHERE receta_id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, recetaId);
        db.executeUpdate(stm);
        // No lanzamos excepción si count es 0, ya que puede no haber detalles
    }

    /**
     * Encuentra todos los detalles en el sistema
     */
    public List<RecipeDetails> findAll() throws Exception {
        List<RecipeDetails> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM receta_detalles rd";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "rd"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los detalles: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * Encuentra detalles por código de medicamento
     */
    public List<RecipeDetails> findByMedicamento(String codigoMedicamento) throws Exception {
        List<RecipeDetails> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM receta_detalles rd WHERE codigoMedicamento = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, codigoMedicamento);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "rd"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando detalles por medicamento: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * Mapea un ResultSet a un objeto RecipeDetails
     */
    public RecipeDetails from(ResultSet rs, String alias) throws SQLException {
        try {
            RecipeDetails detalle = new RecipeDetails();
            detalle.setCodigoMedicamento(rs.getString(alias + ".codigoMedicamento"));
            detalle.setNombre(rs.getString(alias + ".nombre"));
            detalle.setCantidad(rs.getInt(alias + ".cantidad"));
            detalle.setIndicaciones(rs.getString(alias + ".indicaciones"));
            detalle.setDuracionTratamiento(rs.getInt(alias + ".duracionTratamiento"));
            return detalle;
        } catch (SQLException e) {
            return null;
        }
    }
    public boolean exists(RecipeDetails detalle) throws Exception {
        try{
            String sql = "SELECT * FROM receta_detalles rd WHERE codigoMedicamento = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, detalle.getCodigoMedicamento());
            ResultSet rs = db.executeQuery(stm);
            if (rs.next()) {
                return rs.getInt(1)>0;
            }

        }catch(SQLException e){}
        return false;
    }

}