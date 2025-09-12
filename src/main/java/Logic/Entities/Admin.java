package Logic.Entities;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Admin extends UsuarioBase {
    public Admin() { super(); }
    public Admin(String id, String clave, String nombre) {
        super(id, clave, nombre, TipoUsuario.ADMINISTRADOR);
    }
}
