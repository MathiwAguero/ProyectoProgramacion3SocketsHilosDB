package hospital.Entities.Entities;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Farmaceuta extends UsuarioBase implements Serializable {
    public Farmaceuta() { super(); }
    public Farmaceuta(String id, String clave, String nombre) {
        super(id, clave, nombre, TipoUsuario.FARMECEUTA);
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || getClass() != object.getClass()) return false;
        Farmaceuta that = (Farmaceuta) object;
        return Objects.equals(getId(), that.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
