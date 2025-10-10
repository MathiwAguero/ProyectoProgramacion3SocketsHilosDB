package Data;

import Logic.Entities.Medicamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDao {
    Database db;

    public MedicamentoDao() {
        db = Database.getDatabase();
    }

    public void create(Medicamento m) throws Exception {
        String sql = "INSERT INTO medicamentos (codigo, nombre, presentacion) VALUES (?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getCodigo());
        stm.setString(2, m.getNombre());
        stm.setString(3, m.getPresentacion());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo crear el medicamento");
        }
    }

    public Medicamento read(String codigo) throws Exception {
        String sql = "SELECT * FROM medicamentos m WHERE codigo = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "m");
        } else {
            throw new Exception("Medicamento no encontrado");
        }
    }

    public void update(Medicamento m) throws Exception {
        String sql = "UPDATE medicamentos SET nombre = ?, presentacion = ? WHERE codigo = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getNombre());
        stm.setString(2, m.getPresentacion());
        stm.setString(3, m.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar el medicamento");
        }
    }

    public void delete(Medicamento m) throws Exception {
        String sql = "DELETE FROM medicamentos WHERE codigo = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo eliminar el medicamento");
        }
    }

    public List<Medicamento> findAll() throws Exception {
        List<Medicamento> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM medicamentos m";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs, "m"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los medicamentos: " + e.getMessage());
        }
        return resultado;
    }

    public boolean exists(String codigo) throws Exception {
        try {
            String sql = "SELECT COUNT(*) FROM medicamentos WHERE codigo = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, codigo);
            ResultSet rs = db.executeQuery(stm);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando existencia del medicamento: " + e.getMessage());
        }
        return false;
    }

    public List<Medicamento> findByNombre(String nombre) throws Exception {
        List<Medicamento> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM medicamentos m WHERE nombre LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + nombre + "%");
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs, "m"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando medicamentos por nombre: " + e.getMessage());
        }
        return resultado;
    }

    public Medicamento from(ResultSet rs, String alias) throws SQLException {
        try {
            Medicamento m = new Medicamento();
            m.setCodigo(rs.getString(alias + ".codigo"));
            m.setNombre(rs.getString(alias + ".nombre"));
            m.setPresentacion(rs.getString(alias + ".presentacion"));
            return m;
        } catch (SQLException e) {
            return null;
        }
    }
}