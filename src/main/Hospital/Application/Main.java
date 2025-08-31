package Application;

import javax.swing.*;

import View.Login;
import Controller.LoginController;
import View.Medicos;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { //Esto era para probar el frame de medicos y ver que sirva
            JFrame f = new JFrame("Medicos");
            Medicos p = new Medicos();
            f.setContentPane(p.getMedico());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}