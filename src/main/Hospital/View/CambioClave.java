package View;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Exceptions.DataAccessException;
import Services.ServiceLogin;
import Model.UsuarioBase;


public class CambioClave {
    private UsuarioBase usuario;
    private ServiceLogin service;
    private JPanel CambioClave;
    private JButton button1;
    private JButton button2;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JPasswordField passwordField3;

    public void initialization(ServiceLogin service, UsuarioBase usuario) {
        this.usuario =  usuario;
        this.service = service;
    }

    public CambioClave() {
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField1.setText("");
                passwordField2.setText("");
                passwordField3.setText("");
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actual = new String(passwordField1.getPassword());
                String nueva = new String(passwordField2.getPassword());
                String confirmar = new String(passwordField3.getPassword());

                if (actual.equals(nueva)) {
                    JOptionPane.showMessageDialog(CambioClave, "La clave actual no puede ser igual a la nueva");
                    return;
                }
                if (!nueva.equals(confirmar)) {
                    JOptionPane.showMessageDialog(CambioClave, "La clave nueva no coincide con la confirmacion");
                    return;
                }
                try {

                    service.login(usuario.getId(), actual, usuario.getNombre(), usuario.getTipo());

                    // Cambiar la clave
                    service.cambioClave(usuario.getId(), nueva);
                    usuario.setClave(nueva);
                    JOptionPane.showMessageDialog(CambioClave, "Se cambio la clave correctamente");

                    //Esto es para cerrar la ventana luego de y volver al de login
                    java.awt.Window w = SwingUtilities.getWindowAncestor(CambioClave);
                    if (w != null) w.dispose();

                } catch (Exceptions.DataAccessException x) {
                    JOptionPane.showMessageDialog(CambioClave, x.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    public JPanel getPanel() {
        return CambioClave;
    }
}
