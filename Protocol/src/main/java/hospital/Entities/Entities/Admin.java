package hospital.Entities.Entities;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Admin extends UsuarioBase implements Serializable {
    public Admin() { super(); }
    public Admin(String id, String clave, String nombre) {
        super(id, clave, nombre, TipoUsuario.ADMINISTRADOR);
    }

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
