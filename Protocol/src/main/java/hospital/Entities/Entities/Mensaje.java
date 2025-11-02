package hospital.Entities.Entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class Mensaje implements Serializable {
    private int id;
    private String remitenteId;
    private String destinatarioId;
    private String mensaje;
    private Timestamp fecha;
    private boolean leido;

    public Mensaje() {}

    public Mensaje(String remitenteId, String destinatarioId, String mensaje) {
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.mensaje = mensaje;
        this.leido = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemitenteId() {
        return remitenteId;
    }

    public void setRemitenteId(String remitenteId) {
        this.remitenteId = remitenteId;
    }

    public String getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(String destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "remitente='" + remitenteId + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}