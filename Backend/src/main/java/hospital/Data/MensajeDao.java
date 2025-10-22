package hospital.Data;

import Logic.Entities.Mensaje;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MensajeDao {
    Database db;

    public MensajeDao() {
        db = Database.getDatabase();
    }

    public void create(Mensaje m) throws Exception {
        String sql = "INSERT INTO mensajes (remitente_id, destinatario_id, mensaje, fecha, leido) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getRemitenteId());
        stm.setString(2, m.getDestinatarioId());
        stm.setString(3, m.getMensaje());
        stm.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        stm.setBoolean(5, false);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo enviar el mensaje");
        }
    }

    public Mensaje read(int id) throws Exception {
        String sql = "SELECT * FROM mensajes m WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "m");
        } else {
            throw new Exception("Mensaje no encontrado");
        }
    }

    public List<Mensaje> findByDestinatario(String destinatarioId) throws Exception {
        List<Mensaje> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM mensajes m WHERE destinatario_id = ? ORDER BY fecha DESC";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, destinatarioId);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "m"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando mensajes del destinatario: " + e.getMessage());
        }
        return resultado;
    }

    public List<Mensaje> findNoLeidos(String destinatarioId) throws Exception {
        List<Mensaje> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM mensajes m WHERE destinatario_id = ? AND leido = FALSE ORDER BY fecha DESC";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, destinatarioId);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "m"));
            }
        } catch (SQLException e) {
            System.err.println("Error buscando mensajes no leídos: " + e.getMessage());
        }
        return resultado;
    }

    public void marcarComoLeido(int id) throws Exception {
        String sql = "UPDATE mensajes SET leido = TRUE WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, id);
        db.executeUpdate(stm);
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM mensajes WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, id);
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo eliminar el mensaje");
        }
    }

    public Mensaje from(ResultSet rs, String alias) throws SQLException {
        try {
            Mensaje m = new Mensaje();
            m.setId(rs.getInt(alias + ".id"));
            m.setRemitenteId(rs.getString(alias + ".remitente_id"));
            m.setDestinatarioId(rs.getString(alias + ".destinatario_id"));
            m.setMensaje(rs.getString(alias + ".mensaje"));
            m.setFecha(rs.getTimestamp(alias + ".fecha"));
            m.setLeido(rs.getBoolean(alias + ".leido"));
            return m;
        } catch (SQLException e) {
            return null;
        }
    }
}