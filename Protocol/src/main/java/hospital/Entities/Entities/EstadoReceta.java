package hospital.Entities.Entities;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum EstadoReceta {
    CONFECCIONADA, PROCESO, LISTA, ENTREGADA
}
