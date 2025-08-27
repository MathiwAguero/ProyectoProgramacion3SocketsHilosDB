package Model;

public class UsuarioBase {
    private String id;
    private String clave;
    private String nombre;
    private TipoUsuario tipo;

    public UsuarioBase() {}

    public UsuarioBase(String id, String clave, String nombre, TipoUsuario tipo) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoUsuario getTipo() { return tipo;
    }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo;
    }
}
