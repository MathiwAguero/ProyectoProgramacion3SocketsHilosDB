package ManejoListas;

public final class Factory {

    private static Factory instancia; //Esto es para el singleton

    private final MedicoList medicoList = new MedicoList();
    private final PacienteList pacienteList = new PacienteList();
    private final MedicamentoList medicamentoList = new MedicamentoList();
    private final RecetaList recetaList = new RecetaList();
    private final ListUsers listUsers = new ListUsers();
    private final AdminList adminList = new AdminList();
    private final FarmaceutaList farmaceutaList = new FarmaceutaList();
    private final DetailsList detailsList = new DetailsList();

    private Factory() {}

    public static Factory get() {
        if (instancia == null) {
            instancia = new Factory();
        }
        return instancia;
    }

    public MedicoList medico() { return medicoList; }
    public DetailsList details() { return detailsList; }
    public PacienteList paciente() { return pacienteList; }
    public MedicamentoList medicamento() { return medicamentoList; }
    public RecetaList receta() { return recetaList; }
    public ListUsers usuario() { return listUsers; }
    public AdminList admin() { return adminList; }
    public FarmaceutaList farmaceuta() { return farmaceutaList; }

}
