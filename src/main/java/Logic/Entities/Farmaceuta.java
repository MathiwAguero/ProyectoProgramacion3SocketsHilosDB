package Logic.Entities;

public class Farmaceuta extends UsuarioBase {

    public Farmaceuta() {
        super();
    }

    public Farmaceuta(String id, String clave, String nombre) {
        super(id, clave, nombre, TipoUsuario.FARMECEUTA);
    }
}

