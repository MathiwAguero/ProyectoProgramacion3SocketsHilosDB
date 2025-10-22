package hospital.Data;

import hospital.Entities.Entities.*;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    Database db=new Database();

    public AdminDao() {
       db= Database.getDatabase();
    }
    public void create(Admin admin)throws Exception {
        String sql="insert into admin (id,clave,nombre)"+" values(?,?,?)";
        PreparedStatement stm= db.prepareStatement(sql);
        stm.setString(1, admin.getId());
        stm.setString(2, admin.getClave());
        stm.setString(3, admin.getNombre());
        int count = stm.executeUpdate();
        if(count==0){
            throw new Exception("No se pudo agregar el admin");
        }
    }
    public Admin read(String id)throws Exception {
        String sql="select * from admin a where id=?";
        PreparedStatement stm= db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs= db.executeQuery(stm);

        if(rs.next()){
            return from(rs,"a");
        }else{
            throw new Exception("No se pudo encontrar el admin");
        }
    }
    public List<Admin> findAll()throws Exception {
        List<Admin> ds=new ArrayList<Admin>();
        try{
            String sql="select * from admin a";
            PreparedStatement stm= db.prepareStatement(sql);
            ResultSet rs= db.executeQuery(stm);
            while(rs.next()){
                ds.add(from(rs,"a"));
            }
        }catch(SQLException e){

        }
        return ds;
    }
    public List <Admin> findByClave(String clave)throws Exception {
            List<Admin> ds=new ArrayList<>();
            try{
                String sql="select * from admin a  where lower (a.clave) like ?";
                PreparedStatement stm= db.prepareStatement(sql);
                stm.setString(1, clave);
                ResultSet rs=db.executeQuery(stm);
                while(rs.next()){
                    ds.add(from(rs,"a"));
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return ds;
    }

    public Admin from(ResultSet rs, String alias)throws Exception {
        try{
            Admin admin=new Admin();
            admin.setId(rs.getString(alias+ ".id"));
            admin.setClave(rs.getString(alias+ ".clave"));
            admin.setNombre(rs.getString(alias+ ".nombre"));
            return admin;
        }catch(SQLException e){
            return null;
        }
    }


}
