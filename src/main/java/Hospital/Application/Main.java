package Hospital.Application;

import javax.swing.*;

import Hospital.Controller.RecetaController;
import Hospital.Entidades.Paciente;
import  Hospital.Entidades.Receta;
import  Hospital.Model.ModelMedicamentos;
import Hospital.Model.ModelPaciente;
import  Hospital.Model.ModelReceta;
import Hospital.View.Dashboard;
import Hospital.View.Despacho;
import  Hospital.View.Historial;
import  Hospital.View.Login;
import Hospital.HospitalData.HospitalData;

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

