package Controller;

import View.Login;
import View.CambioClave;

import javax.swing.*;

public class LoginController {
    private final Login view;

    public LoginController(Login v) {
        this.view = v;
        view.getButton1().addActionListener(e -> onIngresar());
        view.getButton2().addActionListener(e -> onCancelar());
        view.getButton3().addActionListener(e -> onCambiarClave());
    }

    private void onIngresar() {
        String clave = new String(view.getTextField2().getText());
        String id = new String(view.getTextField1().getText());
        JOptionPane.showMessageDialog(view.getPanel(), "Ingresando con: " + id + " | " + clave);
    }

    private void onCancelar() {
        view.getTextField1().setText("");
        view.getTextField2().setText("");
    }

    private void onCambiarClave() {
        JFrame frame = new JFrame("Cambiar clave");
        CambioClave cc = new CambioClave();
        frame.setContentPane(cc.getPanel());
        frame.pack();
        frame.setLocationRelativeTo(view.getPanel());
        frame.setVisible(true);
    }
}