package hospital.Data;

import Logic.Entities.Paciente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    Database db;

    public PacienteDao() {
        db = Database.getDatabase();
    }

    public void crate(Paciente p) throws Exception {
        String sql = "INSERT INTO pacientes (id, nombre, fechaNacimiento, numeroTelefono) VALUES (?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getNombre());
        stm.setString(3, p.getFechaNacimiento());
        stm.setString(4, p.getNumeroTelefonico());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Paciente ya existe");
        }
    }

    public void delete(Paciente p) throws Exception {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Paciente no existe");
        }
    }

    public void update(Paciente p) throws Exception {
        String sql = "UPDATE pacientes SET nombre = ?, fechaNacimiento = ?, numeroTelefono = ? WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getNombre());
        stm.setString(2, p.getFechaNacimiento());
        stm.setString(3, p.getNumeroTelefonico());
        stm.setString(4, p.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Paciente no existe");
        }
    }

    public Paciente read(String id) throws Exception {
        String sql = "SELECT * FROM pacientes p WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "p");
        } else {
            throw new Exception("Paciente no encontrado");
        }
    }

    public List<Paciente> findAll() throws Exception {
        List<Paciente> pacientes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM pacientes p";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                pacientes.add(from(rs, "p"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    public Paciente from(ResultSet rs, String alias) throws Exception {
        try {
            Paciente p = new Paciente();
            p.setId(rs.getString(alias + ".id"));
            p.setNombre(rs.getString(alias + ".nombre"));
            p.setFechaNacimiento(rs.getString(alias + ".fechaNacimiento"));
            p.setNumeroTelefonico(rs.getString(alias + ".numeroTelefono"));
            return p;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean exists(String id) throws Exception {
        try {
            String sql = "SELECT COUNT(*) FROM pacientes WHERE id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet rs = db.executeQuery(stm);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando paciente: " + e.getMessage());
        }
        return false;
    }

    public List<Paciente> searchByName(String name) throws Exception {
        List<Paciente> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM pacientes p WHERE nombre LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + name + "%");
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs, "p"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando paciente por nombre: " + e.getMessage());
        }
        return resultado;
    }

    public List<Paciente> searchById(String telefono) throws Exception {
        List<Paciente> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM pacientes p WHERE numeroTelefono LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + telefono + "%");
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs, "p"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando paciente por teléfono: " + e.getMessage());
        }
        return resultado;
    }
}