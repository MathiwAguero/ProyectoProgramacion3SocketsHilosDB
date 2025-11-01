package hospital.Entities.Entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class Mensaje implements Serializable {
    private String mensaje;

    public Mensaje() {}

    public Mensaje(String destinatarioId) {

        this.mensaje = mensaje;

    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}