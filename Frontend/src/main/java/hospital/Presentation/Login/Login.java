package hospital.Presentation.Login;

import hospital.Logic.Listas.Factory;
import hospital.Logic.Service;
import hospital.Presentation.CambioClave.CambioClave;
import hospital.Presentation.Despacho.Despacho;
import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.DataAccessException;
import hospital.Logic.Services.ServiceLogin;
import hospital.Presentation.AcercaDe.AcercaDe;
import hospital.Presentation.Dashboard.Dashboard;
import hospital.Presentation.Dashboard.ModelMedicamentosResumen;
import hospital.Presentation.Detalles.ModelDetails;
import hospital.Presentation.Farmaceutas.FarmaceutaController;
import hospital.Presentation.Farmaceutas.Farmaceutas;
import hospital.Presentation.Farmaceutas.ModelFarmaceuta;
import hospital.Presentation.Medicamentos.Medicamentos;
import hospital.Presentation.Medicos.Medicos;
import hospital.Presentation.Medicos.ModelMedico;
import  hospital.Presentation.Medicos.MedicoController;
import  hospital.Presentation.Pacientes.PacienteController;
import hospital. Presentation.Pacientes.ModelPaciente;
import hospital. Presentation.Medicamentos.ModelMedicamentos;
import hospital. Presentation.Medicamentos.MedicamentoController;
import hospital. Presentation.Pacientes.Pacientes;
import hospital. Presentation.Prescripcion.PrescribirController;
import  hospital.Presentation.Prescripcion.PrescribirMed;
import hospital.Presentation.Recetas.Historial;
import hospital. Presentation.Recetas.ModelReceta;
import hospital. Presentation.Recetas.RecetaController;
import hospital.Presentation.Usuarios.ControllerUsuariosMensaje;
import hospital.Presentation.Usuarios.ModelUsuariosMensaje;
import hospital.Presentation.Usuarios.Usuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    private JPanel Login;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTextField textField1;
    private JPasswordField passwordField1;

    UsuariosController controller;
    ModelUsuarios model;
    UsuarioBase usuarioLogged;

    public Login() {

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField1.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(Login, "Escriba primero su ID");
                    return;
                }
                ServiceLogin service = new ServiceLogin();
                UsuarioBase usuario = null;
                try {
                    usuario = Service.getInstance().readUsuario(id);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if (usuario == null) {
                    JOptionPane.showMessageDialog(Login, "No existe un usuario con ese ID.");
                    return;
                }
                boolean huboCambio = CambioClave.open(SwingUtilities.getWindowAncestor(Login), service, usuario);
                if (huboCambio) {
                    JOptionPane.showMessageDialog(Login, "Clave actualizada.");
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setText("");
                passwordField1.setText("");
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServiceLogin service = new ServiceLogin();
                String id = textField1.getText().trim();
                String clave = new String(passwordField1.getPassword()).trim();

                if (id.isEmpty() || clave.isEmpty()) {
                    JOptionPane.showMessageDialog(Login, "Ingrese su ID completo (ADM-xxx / MED-xxx / FAR-xxx) y su clave");
                    return;
                }
                if (id.length() < 3) {
                    JOptionPane.showMessageDialog(Login, "ID inválido. Debe iniciar con ADM / MED / FAR");
                    return;
                }

                try {
                    usuarioLogged = service.loginPorId(id, clave);
                    if (usuarioLogged == null) {
                        JOptionPane.showMessageDialog(Login, "ID o clave incorrectos.");
                        return;
                    }
                } catch (DataAccessException ex) {
                    JOptionPane.showMessageDialog(Login, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String leerCaracter = id.substring(0, 3).toUpperCase();
                TipoUsuario tipo = TipoUsuario.ingreso(leerCaracter);
                if (tipo == null) {
                    JOptionPane.showMessageDialog(Login, "Rol invalido");
                    return;
                }

                // ========== CREAR PANEL DE MENSAJERÍA ==========
                Usuarios ventanaUsuarios = new Usuarios();
                ModelUsuariosMensaje modelUsuarios = new ModelUsuariosMensaje();
                new ControllerUsuariosMensaje(ventanaUsuarios, modelUsuarios);

                // ========== CREAR TABS SEGÚN EL ROL ==========
                JTabbedPane tabs = new JTabbedPane();

                switch (leerCaracter) {
                    case "ADM": {
                        Medicos ventanaMed = new Medicos();
                        Farmaceutas ventanaFarmaceuta = new Farmaceutas();
                        Pacientes ventanaPacientes = new Pacientes();
                        Medicamentos ventanaMedicamentos = new Medicamentos();
                        Dashboard ventanaDashboard = new Dashboard();
                        Historial ventanaHistorial = new Historial();
                        AcercaDe ventanaAcerdaDe = new AcercaDe();

                        ModelMedico modelMed = new ModelMedico();
                        new MedicoController(ventanaMed, modelMed);

                        ModelFarmaceuta modelFar = new ModelFarmaceuta();
                        new FarmaceutaController(ventanaFarmaceuta, modelFar);

                        ModelPaciente modelPac = new ModelPaciente();
                        new PacienteController(ventanaPacientes, modelPac);

                        ModelMedicamentos modelMedicam = new ModelMedicamentos();
                        new MedicamentoController(ventanaMedicamentos, modelMedicam);

                        ModelReceta modelRecetaDashboard = new ModelReceta();
                        ModelMedicamentosResumen modelMedicamentosResumen = new ModelMedicamentosResumen();
                        ventanaDashboard.setModelR(modelMedicamentosResumen);
                        RecetaController rc = new RecetaController(ventanaDashboard, modelRecetaDashboard);
                        ventanaDashboard.setController(rc);

                        ModelReceta modelRecetaHistorial = new ModelReceta();
                        new RecetaController(ventanaHistorial, modelRecetaHistorial);

                        tabs.addTab("Medicos", ventanaMed.getMedico());
                        tabs.addTab("Farmaceutas", ventanaFarmaceuta.getFarmaceutas());
                        tabs.addTab("Pacientes", ventanaPacientes.getPacientes());
                        tabs.addTab("Medicamentos", ventanaMedicamentos.getMedicamentos());
                        tabs.addTab("Dashboard", ventanaDashboard.getDashboard());
                        tabs.addTab("Historial", ventanaHistorial.getHistorial());
                        tabs.addTab("Acerca de", ventanaAcerdaDe.getPanel());
                        break;
                    }

                    case "MED": {
                        Medico ingresado = null;
                        try {
                            ingresado = Service.getInstance().readMedico(usuarioLogged.getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }

                        Dashboard ventanaDashboard = new Dashboard();
                        PrescribirMed ventanaPrescribir = new PrescribirMed();
                        Historial ventanaHistorial = new Historial();
                        AcercaDe ventanaAcercaDe = new AcercaDe();

                        ModelDetails modelDetailsPrescribir = new ModelDetails();
                        ModelReceta modelRecetaDashboard = new ModelReceta();
                        ModelReceta modelRecetaHistorial = new ModelReceta();
                        ModelMedicamentosResumen modelMedicamentosResumen = new ModelMedicamentosResumen();

                        new PrescribirController(ventanaPrescribir, modelDetailsPrescribir);
                        new RecetaController(ventanaHistorial, modelRecetaHistorial);

                        ventanaDashboard.setModelR(modelMedicamentosResumen);
                        RecetaController rc = new RecetaController(ventanaDashboard, modelRecetaDashboard);
                        ventanaDashboard.setController(rc);

                        ventanaPrescribir.setMedicoActual(ingresado);

                        tabs.addTab("Prescribir", ventanaPrescribir.getPrescribirMed());
                        tabs.addTab("Dashboard", ventanaDashboard.getDashboard());
                        tabs.addTab("Historial", ventanaHistorial.getHistorial());
                        tabs.addTab("Acerca de", ventanaAcercaDe.getPanel());
                        break;
                    }

                    case "FAR": {
                        Despacho ventanaDespacho = new Despacho();
                        Dashboard ventanaDashboard = new Dashboard();
                        Historial ventanaHistorial = new Historial();
                        AcercaDe ventanaAcercaDe = new AcercaDe();

                        ModelReceta modelRecetaDashboard = new ModelReceta();
                        ModelMedicamentosResumen modelMedicamentosResumen = new ModelMedicamentosResumen();
                        ventanaDashboard.setModelR(modelMedicamentosResumen);
                        RecetaController rc = new RecetaController(ventanaDashboard, modelRecetaDashboard);
                        ventanaDashboard.setController(rc);

                        ModelReceta modelRecetaHist = new ModelReceta();
                        new RecetaController(ventanaHistorial, modelRecetaHist);

                        ModelReceta modelRecetaDespacho = new ModelReceta();
                        new RecetaController(ventanaDespacho, modelRecetaDespacho);

                        tabs.addTab("Despacho", ventanaDespacho.getDespacho());
                        tabs.addTab("Dashboard", ventanaDashboard.getDashboard());
                        tabs.addTab("Historial", ventanaHistorial.getHistorial());
                        tabs.addTab("Acerca de", ventanaAcercaDe.getPanel());
                        break;
                    }

                    default:
                        JOptionPane.showMessageDialog(Login, "Rol invalido. ADM / MED / FAR");
                        return;
                }

                // ========== CREAR VENTANA CON LAYOUT DIVIDIDO ==========
                JFrame ventanaPrincipal = new JFrame("Hospital - " + leerCaracter + " - " + usuarioLogged.getNombre());
                ventanaPrincipal.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Crear JSplitPane para dividir la ventana
                JSplitPane splitPane = new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        tabs,                           // Lado izquierdo: tabs
                        ventanaUsuarios.getPanel()      // Lado derecho: mensajería
                );

                // Configurar el divisor
                splitPane.setDividerLocation(800);  // Posición inicial del divisor
                splitPane.setDividerSize(8);        // Grosor del divisor
                splitPane.setResizeWeight(0.65);    // 65% para tabs, 35% para mensajería
                splitPane.setContinuousLayout(true); // Redibuja mientras se arrastra

                ventanaPrincipal.setContentPane(splitPane);
                ventanaPrincipal.setSize(1400, 800);  // Ventana más grande para acomodar ambos paneles
                ventanaPrincipal.setLocationRelativeTo(null);
                ventanaPrincipal.setVisible(true);

                Window w = SwingUtilities.getWindowAncestor(Login);
                if (w != null) w.dispose();
            }
        });
    }

    public JPanel getLogin() { return Login; }

    public void setController(UsuariosController controller) { this.controller = controller; }
    public void setModel(ModelUsuarios model) { this.model = model; }
}
