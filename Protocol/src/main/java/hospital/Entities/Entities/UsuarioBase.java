package hospital.Entities.Entities;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Admin.class, Medico.class, Farmaceuta.class})
public class UsuarioBase implements Serializable {
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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

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
