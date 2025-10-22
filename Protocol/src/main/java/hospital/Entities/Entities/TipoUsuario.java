package hospital.Entities.Entities;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum TipoUsuario {
    MEDICO, FARMECEUTA, ADMINISTRADOR;

    public static TipoUsuario ingreso(String rol){
        if (rol == null) return null;
        rol = rol.trim().toUpperCase();
        switch (rol){
            case "ADM": return ADMINISTRADOR;
            case "MED": return MEDICO;
            case "FAR": return FARMECEUTA;
            default:    return null;
        }
    }
}
