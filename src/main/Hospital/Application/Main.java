package Application;

import javax.swing.*;

import Controller.MedicoController;
import Model.ModelMedico;
import TableModel.TableModelMedicos;
import View.Login;
import Controller.LoginController;
import View.Medicos;
import HospitalData.HospitalData;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
            SwingUtilities.invokeLater(() -> {
                HospitalData.incializarDatos();
                System.out.println("Datos del Hospital inicializados");

                JFrame ventana = new JFrame("Login");
                Login loginView = new Login();
                ventana.setContentPane(loginView.getLogin());
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventana.pack();
                ventana.setLocationRelativeTo(null);
                ventana.setVisible(true);
            });

    }
    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);

}

