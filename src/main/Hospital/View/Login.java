package View;

import Controller.*;
import Model.*;
import Services.ServiceLogin;
import Entidades.TipoUsuario;
import Entidades.UsuarioBase;

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
                UsuarioBase usuario = ManejoListas.Factory.get().usuario().obtenerPorId(id);
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
                ServiceLogin service = new ServiceLogin(); //para llamar metodo de login (logica)
                UsuarioBase user; //UsuarioBase porque no sabemos que tipo es

                String id = textField1.getText().trim();
                String clave = new String(passwordField1.getPassword()).trim();
                String leerCaracter = id.substring(0, 3).toUpperCase();
                TipoUsuario tipo = TipoUsuario.ingreso(leerCaracter); //Esto va a devolver ADM, MED o FAR

                if (id.isEmpty() || clave.isEmpty()) {
                    JOptionPane.showMessageDialog(Login, "Ingrese su ID completo (ADM-xxx / MED-xxx / FAR-xxx) y su clave");
                    return;
                } else if (tipo == null) {
                    JOptionPane.showMessageDialog(Login, "Rol invalido");
                    return;
                }

                try {
                    user = service.loginPorId(id, clave);
                } catch (Exceptions.DataAccessException ex) {
                    JOptionPane.showMessageDialog(Login, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

                        ModelDetails modelDetails = new ModelDetails();
                        new DetailsController(ventanaHistorial, modelDetails);

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
                        Dashboard ventanaDashboard2 = new Dashboard();
                        Historial ventanaHistorial2 = new Historial();
                        AcercaDe ventanaAcerdaDe2 = new AcercaDe();
                        Prescripcion ventanaPrescribir = new Prescripcion();

                        ModelDetails modelDetails = new ModelDetails();
                        new DetailsController(ventanaHistorial2, modelDetails);

                        tabs.addTab("Prescribir", ventanaPrescribir.getPrescrip());
                        tabs.addTab("Dashboard", ventanaDashboard2.getDashboard());
                        tabs.addTab("Historial", ventanaHistorial2.getHistorial());
                        tabs.addTab("Acerca de", ventanaAcerdaDe2.getPanel());
                        break;
                    }
                    case "FAR": {
                        Dashboard ventanaDashboard3 = new Dashboard();
                        Historial ventanaHistorial3 = new Historial();
                        AcercaDe ventanaAcerdaDe3 = new AcercaDe();
                        Despacho ventanaDespacho = new Despacho();

                        ModelDetails modelDetails = new ModelDetails();
                        new DetailsController(ventanaHistorial3, modelDetails);

                        tabs.addTab("Despacho", ventanaDespacho.getDespacho());
                        tabs.addTab("Dashboard", ventanaDashboard3.getDashboard());
                        tabs.addTab("Historial", ventanaHistorial3.getHistorial());
                        tabs.addTab("Acerca de", ventanaAcerdaDe3.getPanel());
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(Login, "Rol invalido. ADM / MED / FAR");
                        return;
                }

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
