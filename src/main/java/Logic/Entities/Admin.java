package Logic.Entities;


public class Admin extends UsuarioBase {

    public Admin() {
        super();
    }

    public Admin(String id, String clave, String nombre) {
        super(id, clave, nombre, TipoUsuario.ADMINISTRADOR);
    }
}