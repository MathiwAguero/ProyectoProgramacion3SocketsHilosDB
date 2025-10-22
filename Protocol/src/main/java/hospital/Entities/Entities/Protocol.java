package hospital.Entities.Entities;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 5000;

    // Códigos de error
    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_ERROR = 1;

    // Operaciones de PACIENTES
    public static final int PACIENTE_CREATE = 101;
    public static final int PACIENTE_READ = 102;
    public static final int PACIENTE_UPDATE = 103;
    public static final int PACIENTE_DELETE = 104;
    public static final int PACIENTE_SEARCH = 105;
    public static final int PACIENTE_SEARCH_ALL = 106;

    // Operaciones de MEDICOS
    public static final int MEDICO_CREATE = 201;
    public static final int MEDICO_READ = 202;
    public static final int MEDICO_UPDATE = 203;
    public static final int MEDICO_DELETE = 204;
    public static final int MEDICO_SEARCH = 205;
    public static final int MEDICO_SEARCH_ALL = 206;

    // Operaciones de FARMACEUTAS
    public static final int FARMACEUTA_CREATE = 301;
    public static final int FARMACEUTA_READ = 302;
    public static final int FARMACEUTA_UPDATE = 303;
    public static final int FARMACEUTA_DELETE = 304;
    public static final int FARMACEUTA_SEARCH = 305;
    public static final int FARMACEUTA_SEARCH_ALL = 306;

    // Operaciones de MEDICAMENTOS
    public static final int MEDICAMENTO_CREATE = 401;
    public static final int MEDICAMENTO_READ = 402;
    public static final int MEDICAMENTO_UPDATE = 403;
    public static final int MEDICAMENTO_DELETE = 404;
    public static final int MEDICAMENTO_SEARCH = 405;
    public static final int MEDICAMENTO_SEARCH_ALL = 406;

    // Operaciones de RECETAS
    public static final int RECETA_CREATE = 501;
    public static final int RECETA_READ = 502;
    public static final int RECETA_UPDATE = 503;
    public static final int RECETA_DELETE = 504;
    public static final int RECETA_SEARCH = 505;
    public static final int RECETA_SEARCH_ALL = 506;
    public static final int RECETA_SEARCH_BY_PACIENTE = 507;
    public static final int RECETA_SEARCH_BY_ESTADO = 508;

    // Operaciones de USUARIOS
    public static final int USUARIO_LOGIN = 601;
    public static final int USUARIO_LOGOUT = 602;
    public static final int USUARIO_CHANGE_PASSWORD = 603;
    public static final int USUARIO_SEARCH_ACTIVE = 604;
    public static final int USUARIO_SET_ACTIVE = 605;

    // Operaciones de MENSAJES
    public static final int MENSAJE_SEND = 701;
    public static final int MENSAJE_GET_UNREAD = 702;
    public static final int MENSAJE_MARK_READ = 703;
    public static final int MENSAJE_GET_BY_USER = 704;

    // Operaciones de ADMINS
    public static final int ADMIN_SEARCH_ALL = 801;

    // Operaciones de DASHBOARD
    public static final int DASHBOARD_GET_STATS = 901;

    // Control de conexión
    public static final int DISCONNECT = 999;

    // Notificaciones asíncronas (del servidor a los clientes)
    public static final int NOTIFICATION_USER_LOGIN = 1001;
    public static final int NOTIFICATION_USER_LOGOUT = 1002;
    public static final int NOTIFICATION_MESSAGE = 1003;
}
