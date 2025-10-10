package Data;
import Logic.Entities.Medico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class MedicoDao {
    Database db = new Database();

    public MedicoDao(){
        db=Database.getDatabase();
    }

    public void create(Medico m) throws Exception{
        String sql="INSERT INTO medicos (id, clave, nombre, especialidad) "+ "VALUES (?, ?, ?, ?)";
        PreparedStatement stm= db.prepareStatement(sql);
        stm.setString(1,m.getId());
        stm.setString(2,m.getClave());
        stm.setString(3,m.getNombre());
        stm.setString(4,m.getEspecialidad());
        int count = db.executeUpdate(stm);
        if(count==0){
            throw new Exception("No se puede crear el medico");
        }
    }
    public Medico read(String id) throws Exception{
        String sql="SELECT * FROM medicos m WHERE id=?";
        PreparedStatement stm= db.prepareStatement(sql);
        stm.setString(1,id);
        ResultSet rs= db.executeQuery(stm);
        if(rs.next()){
            return from(rs,"m");
        }else{
            throw new Exception("No se encontro el medico");
        }
    }
    public void update(Medico m) throws Exception{
        String sql=  "UPDATE medicos SET clave = ?, nombre = ?, especialidad = ? WHERE id = ?";
        PreparedStatement stm= db.prepareStatement(sql);
        stm.setString(1,m.getClave());
        stm.setString(2,m.getNombre());
        stm.setString(3,m.getEspecialidad());
        stm.setString(4,m.getId());
        stm.setString(5,m.getId());
        int count = db.executeUpdate(stm);
        if(count==0){

            throw new Exception("No se puede actualizar el medico");
        }
    }
    public void delete(Medico m) throws Exception{
        String sql= "DELETE FROM medicos m WHERE id = ?";
        PreparedStatement stm= db.prepareStatement(sql);
        stm.setString(1,m.getId());
        int count = db.executeUpdate(stm);
        if(count==0){
            throw new Exception("No se puede eliminar el medico");
        }
    }
    public List<Medico>findAll() throws Exception{
        List<Medico> medicos= new ArrayList<>();
        try{
        String sql="SELECT * FROM medicos m";
        PreparedStatement stm= db.prepareStatement(sql);
        ResultSet rs=stm.executeQuery();
        while(rs.next()){
            medicos.add(from(rs,"m"));
        }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return medicos;
    }
    public boolean exists(String id) throws Exception{
        try{
            String sql="SELECT COUNT(*) FROM medicos m WHERE id = ?";
            PreparedStatement stm= db.prepareStatement(sql);
            stm.setString(1,id);
            ResultSet rs=db.executeQuery(stm);
            if(rs.next()){
                return rs.getInt(1)>0;
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }
    public List<Medico> searchByName(String nombre) throws Exception{
        List<Medico> medicos= new ArrayList<>();
        try{
            String sql="SELECT * FROM medicos m WHERE nombre LIKE ?";
            PreparedStatement stm= db.prepareStatement(sql);
            stm.setString(1,nombre);
            ResultSet rs=db.executeQuery(stm);
            while(rs.next()){
                medicos.add(from(rs,"m"));
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return medicos;
    }

    private Medico from(ResultSet rs, String alias) throws Exception{
        try{
            Medico m = new Medico();
            m.setId(rs.getString(alias+".id"));
            m.setClave(rs.getString(alias+".clave"));
            m.setNombre(rs.getString(alias+".nombre"));
            m.setEspecialidad(rs.getString(alias+".especialidad"));
            return m;
        }catch(SQLException e){
            return null;
        }
    }
}
