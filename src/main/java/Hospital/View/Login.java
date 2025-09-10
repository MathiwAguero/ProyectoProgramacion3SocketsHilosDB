package Hospital.View;

import  Hospital.Controller.*;
import Hospital.Entidades.Medico;
import Hospital.Exceptions.DataAccessException;
import  Hospital.Model.*;
import Hospital.Services.ServiceLogin;
import Hospital.Entidades.TipoUsuario;
import Hospital.Entidades.UsuarioBase;
import Hospital.ManejoListas.*;
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
                UsuarioBase usuario = Factory.get().usuario().obtenerPorId(id);
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
                        new RecetaController(ventanaDashboard, modelRecetaDashboard);

                        ModelReceta modelRecetaHistorial =  new  ModelReceta();
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

                        Medico ingresado = Factory.get().medico().obtenerPorId(usuarioLogged.getId());
                        Dashboard ventanaDashboard = new Dashboard();
                        PrescribirMed ventanaPrescribir = new PrescribirMed();
                        Historial ventanaHistorial = new Historial();
                        AcercaDe ventanaAcercaDe = new AcercaDe();

                        ModelDetails modelDetailsPrescribir = new ModelDetails();
                        ModelReceta modelRecetaDashboard = new ModelReceta();
                        ModelReceta modelRecetaHistorial = new ModelReceta();

                        new PrescribirController(ventanaPrescribir, modelDetailsPrescribir);
                        new RecetaController(ventanaHistorial, modelRecetaHistorial);
                        new RecetaController(ventanaDashboard, modelRecetaDashboard);

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
                        new RecetaController(ventanaDashboard, modelRecetaDashboard);

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
                //usuarioLogged = //medico logueado
                JFrame ventanaPrincipal = new JFrame("Hospital - " + leerCaracter);
                ventanaPrincipal.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ventanaPrincipal.setContentPane(tabs);
                ventanaPrincipal.pack();
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
