package DataAccessObject;

public final class DAOFactory {

    private static DAOFactory instancia; //Esto es para el singleton

    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
    private final RecetaDAO recetaDAO = new RecetaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final FarmaceutaDAO farmaceutaDAO = new FarmaceutaDAO();

    private DAOFactory() {}

    public static DAOFactory get() {
        if (instancia == null) {
            instancia = new DAOFactory();
        }
        return instancia;
    }

    public MedicoDAO medico() { return medicoDAO; }
    public PacienteDAO paciente() { return pacienteDAO; }
    public MedicamentoDAO medicamento() { return medicamentoDAO; }
    public RecetaDAO receta() { return recetaDAO; }
    public UsuarioDAO usuario() { return usuarioDAO; }
    public AdminDAO admin() { return adminDAO; }
    public FarmaceutaDAO farmaceuta() { return farmaceutaDAO; }

}
