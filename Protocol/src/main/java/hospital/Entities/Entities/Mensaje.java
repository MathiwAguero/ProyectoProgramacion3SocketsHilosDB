package hospital.Entities.Entities;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private String mensaje;

    public Mensaje() {}


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}