package Data;

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
    public void crate(Paciente p) throws Exception{
        String sql = "insert into Paciente(id,nombre,fechaNacimiento,numeroTelefono)" + "values(?,?,?,?)";
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
    public void delete(Paciente p) throws Exception{
            String sql = "delete from Paciente where id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, p.getId());
            int count = db.executeUpdate(stm);
            if (count == 0) {
                throw new Exception("Paciente no existe");
            }
    }
    public void update(Paciente p) throws Exception{
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

    public Paciente read(String id) throws Exception{
        String sql = "select * from Paciente p where id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return from(rs,"p");
        }   else{
            throw new Exception("Paciente no encontrado");
        }
    }
    public List<Paciente> findAll() throws Exception {
        List<Paciente> pacientes = new ArrayList<Paciente>();
        try {
            String sql = "select * from Paciente p";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                pacientes.add(from(rs, "p"));
            }
        } catch (SQLException e) {}
            return pacientes;
        }

    public Paciente from(ResultSet rs, String id) throws Exception{
        try {
            Paciente p = new Paciente();
            p.setId(rs.getString(id+".id"));
            p.setNombre(rs.getString(id+".nombre"));
            p.setFechaNacimiento(rs.getString(id+".fechaNacimiento"));
            p.setFechaNacimiento(rs.getString(id+".fechaNacimiento"));
            return p;
        }catch(SQLException e) {
            return null;
        }
    }
    public boolean exists(String id) throws Exception{
        try{
            String sql = "select count(*) from Paciente p where id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet rs = db.executeQuery(stm);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }catch(SQLException e) {
            System.err.println("error verificando paciente");
        }
        return false;
    }
    public List<Paciente> searchByName(String name) throws Exception{
        List<Paciente> resultado= new ArrayList<>();
        try{
            String sql = "select * from Paciente p where nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+name+"%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                resultado.add(from(rs, "p"));
            }
        }catch(SQLException e) {
            System.err.println("error verificando paciente");
        }
        return resultado;
    }
    public List<Paciente> searchById(String telefono) throws Exception{
        List<Paciente> resultado= new ArrayList<>();
        try{
            String sql = "select * from Paciente p where numeroTelefonico like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+telefono+"%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                resultado.add(from(rs, "p"));
            }
        }catch(SQLException e) {
            System.err.println("error verificando paciente");
        }
        return resultado;
    }

}
