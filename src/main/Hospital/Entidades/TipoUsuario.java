package Entidades;

public enum TipoUsuario {
    MEDICO,
    FARMECEUTA,
    ADMINISTRADOR;

    public static TipoUsuario ingreso(String rol){
        if (rol == null) {
            return null;
        }

        rol = rol.trim().toUpperCase();

        switch (rol){
            case "ADM": return TipoUsuario.ADMINISTRADOR;
            case "MED": return TipoUsuario.MEDICO;
            case "FAR": return TipoUsuario.FARMECEUTA;
            default:    return null;
        }
    }

}
