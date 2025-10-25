package hospital.Entities.Entities;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Medico extends UsuarioBase implements Serializable {
    private String especialidad;

    public Medico() { super(); }

    public Medico(String id, String clave, String nombre, String especialidad) {
        super(id, clave, nombre, TipoUsuario.MEDICO);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null||getClass()!=obj.getClass()) return false;
        Admin other = (Admin) obj;
        return Objects.equals(this.getId(), other.getId());
    }
}
