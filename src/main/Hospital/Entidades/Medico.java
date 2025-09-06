package Entidades;

public class Medico extends UsuarioBase {
    private String especialidad;

    public Medico() {
        super();
    }

    public Medico(String id, String clave, String nombre, String especialidad) {
        super(id, clave, nombre, TipoUsuario.MEDICO);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
