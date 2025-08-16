package Model;

public class Receta {
    private String fechaConfecc;
    private String fechaRetiro;
    private String estado;

    Receta() {
        this.fechaConfecc = "";
        this.fechaRetiro = "";
        this.estado = "";
    }

    Receta(String fechaConfecc, String fechaRetiro, String estado) {
        this.fechaConfecc = fechaConfecc;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(String fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public String getFechaConfecc() {
        return fechaConfecc;
    }

    public void setFechaConfecc(String fechaConfecc) {
        this.fechaConfecc = fechaConfecc;
    }
}
