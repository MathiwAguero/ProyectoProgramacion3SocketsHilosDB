package Application;

import javax.swing.*;

import View.Login;
import Controller.LoginController;
import View.Medicos;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { //Esto era para probar el frame de momento
            JFrame f = new JFrame("Login");
            Login log = new Login();
            f.setContentPane(log.getLogin());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}