package Model;

public class Admin {
    private String nombre;
    private int clave;
    private int id;

    Admin() {
        this.nombre = "";
        this.clave = 0;
        this.id = 0;
    }

    Admin(String nombre, int clave, int id) {
        this.nombre = nombre;
        this.clave = clave;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
