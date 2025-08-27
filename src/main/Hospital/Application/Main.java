package Application;

import javax.swing.*;

import View.Login;
import Controller.LoginController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            Login loginPanel = new Login();
            new LoginController(loginPanel);
            frame.setContentPane(loginPanel.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}