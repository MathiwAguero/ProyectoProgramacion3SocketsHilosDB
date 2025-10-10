package Data;

import Logic.Entities.TipoUsuario;
import Logic.Entities.UsuarioBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    Database db;

    public UsuarioDao() {
        db = Database.getDatabase();
    }

    public void create(UsuarioBase u) throws Exception {
        String sql = "INSERT INTO usuarios (id, clave, nombre, tipo, activo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getId());
        stm.setString(2, u.getClave());
        stm.setString(3, u.getNombre());
        stm.setString(4, u.getTipo().name());
        stm.setBoolean(5, false);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo crear el usuario");
        }
    }

    public UsuarioBase read(String id) throws Exception {
        String sql = "SELECT * FROM usuarios u WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "u");
        } else {
            throw new Exception("Usuario no encontrado");
        }
    }

    public void update(UsuarioBase u) throws Exception {
        String sql = "UPDATE usuarios SET clave = ?, nombre = ?, tipo = ? WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getClave());
        stm.setString(2, u.getNombre());
        stm.setString(3, u.getTipo().name());
        stm.setString(4, u.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar el usuario");
        }
    }

    public void delete(UsuarioBase u) throws Exception {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo eliminar el usuario");
        }
    }

    public List<UsuarioBase> findAll() throws Exception {
        List<UsuarioBase> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM usuarios u";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "u"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los usuarios: " + e.getMessage());
        }
        return resultado;
    }

    public UsuarioBase authenticate(String id, String clave) throws Exception {
        String sql = "SELECT * FROM usuarios u WHERE id = ? AND clave = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        stm.setString(2, clave);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "u");
        } else {
            throw new Exception("Credenciales inválidas");
        }
    }

    public void setActivo(String id, boolean activo) throws Exception {
        String sql = "UPDATE usuarios SET activo = ? WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setBoolean(1, activo);
        stm.setString(2, id);
        db.executeUpdate(stm);
    }

    public List<UsuarioBase> findActivos() throws Exception {
        List<UsuarioBase> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM usuarios u WHERE activo = TRUE";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "u"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuarios activos: " + e.getMessage());
        }
        return resultado;
    }

    public void updateClave(String id, String nuevaClave) throws Exception {
        String sql = "UPDATE usuarios SET clave = ? WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, nuevaClave);
        stm.setString(2, id);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar la clave");
        }
    }

    public UsuarioBase from(ResultSet rs, String alias) throws SQLException {
        try {
            UsuarioBase u = new UsuarioBase();
            u.setId(rs.getString(alias + ".id"));
            u.setClave(rs.getString(alias + ".clave"));
            u.setNombre(rs.getString(alias + ".nombre"));
            String tipoStr = rs.getString(alias + ".tipo");
            u.setTipo(TipoUsuario.valueOf(tipoStr));
            return u;
        } catch (SQLException e) {
            return null;
        }
    }
}