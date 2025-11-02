package hospital.Entities.Entities;

import java.io.Serializable;

/**
 * Clase auxiliar para mostrar usuarios con contador de mensajes
 */
public class UsuarioConMensajes implements Serializable {
    private UsuarioBase usuario;
    private int cantidadMensajes;

    public UsuarioConMensajes() {
        this.usuario = new UsuarioBase();
        this.cantidadMensajes = 0;
    }

    public UsuarioConMensajes(UsuarioBase usuario, int cantidadMensajes) {
        this.usuario = usuario;
        this.cantidadMensajes = cantidadMensajes;
    }

    public UsuarioBase getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBase usuario) {
        this.usuario = usuario;
    }

    public int getCantidadMensajes() {
        return cantidadMensajes;
    }

    public void setCantidadMensajes(int cantidadMensajes) {
        this.cantidadMensajes = cantidadMensajes;
    }

    public String getId() {
        return usuario != null ? usuario.getId() : "";
    }

    public String getNombre() {
        return usuario != null ? usuario.getNombre() : "";
    }

    @Override
    public String toString() {
        return (usuario != null ? usuario.getNombre() : "Sin nombre") +
                " (" + cantidadMensajes + " mensajes)";
    }
}