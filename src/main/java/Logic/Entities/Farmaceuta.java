package Logic.Entities;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Farmaceuta extends UsuarioBase {
    public Farmaceuta() { super(); }
    public Farmaceuta(String id, String clave, String nombre) {
        super(id, clave, nombre, TipoUsuario.FARMECEUTA);
    }
}
