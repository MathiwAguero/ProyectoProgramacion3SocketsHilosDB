package Entities;

public class Medico {
    private int id;
    private String name;
    private int clave;
    private String especialidad;

    Medico(int id, String name, int clave, String especialidad) {
        this.id = id;
        this.name = name;
        this.clave = clave;
        this.especialidad = especialidad;
    }

    Medico() {
        this.id = 0;
        this.name = "";
        this.clave = 0;
        this.especialidad = "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId () {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

}
