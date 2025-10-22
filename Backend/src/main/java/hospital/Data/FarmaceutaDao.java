package hospital.Data;
import Logic.Entities.Farmaceuta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class FarmaceutaDao {
    Database db = new Database();

    public FarmaceutaDao() {
        db= Database.getDatabase();
    }

    public void create(Farmaceuta farmaceuta) throws Exception {
            String sql= "insert into farmaceutas (id,clave,nombre)"+"values(?,?,?)";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, farmaceuta.getId());
            stm.setString(2, farmaceuta.getClave());
            stm.setString(3, farmaceuta.getNombre());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Farmaceuta Already Exists");
            }
    }
    public Farmaceuta read(String id) throws Exception {
        String sql= "select * from farmaceutas f where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            return from(rs,"f");
        }else{
            throw new Exception("Farmaceuta Not Found");
        }
    }
    public void update(Farmaceuta farmaceuta) throws Exception {
        String sql= "update farmaceutas set clave=?,nombre=? where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, farmaceuta.getClave());
        stm.setString(2, farmaceuta.getNombre());
        stm.setString(3, farmaceuta.getId());
        int count = stm.executeUpdate();
        if (count == 0) {
            throw new Exception("Farmaceuta Already Exists");
        }
    }

    public void delete(Farmaceuta farmaceuta) throws Exception {
        String sql= "delete from farmaceutas f where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, farmaceuta.getId());
        int count = stm.executeUpdate();
        if (count == 0) {
            throw new Exception("Farmaceuta Already Exists");
        }
    }
    public List<Farmaceuta> findAll() throws Exception {
        List<Farmaceuta> result = new ArrayList<>();
        try{
            String sql= "select * from farmaceutas f";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                result.add(from(rs,"f"));
            }
        }catch(SQLException e){}
        return result;
    }
    public boolean exists(String id) throws Exception {
        try {
            String sql = "select * from farmaceutas f where id=?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet rs = db.executeQuery(stm);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }catch(SQLException e) {
            System.err.println("Farmaceuta Not Found");
        }
        return false;
    }
    public List<Farmaceuta> findByNombre(String nombre) throws Exception {
        List<Farmaceuta> result = new ArrayList<>();
        try{
            String sql= "select * from farmaceutas f where nombre LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+nombre+"%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                result.add(from(rs,"f"));
            }
        }catch(SQLException e) {
            System.err.println("Farmaceuta Not Found");
        }
        return result;
    }


    public Farmaceuta from(ResultSet rs,String clave) throws SQLException {
        try{
            Farmaceuta farmaceuta = new Farmaceuta();
            farmaceuta.setId(rs.getString(clave+".id"));
            farmaceuta.setNombre(rs.getString(clave+".nombre"));
            farmaceuta.setClave(rs.getString(clave+".clave"));
            return farmaceuta;
        }catch(SQLException e){
            return null;
        }
    }
}
