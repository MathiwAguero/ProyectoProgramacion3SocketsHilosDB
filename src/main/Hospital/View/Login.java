package View;

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
    private JTextField textField2;

    public Login() {
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setText("");
                textField2.setText("");
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Medicos ventanaMed = new Medicos(); //creacion

                JFrame ventana = new JFrame("Medicos");
                ventana.setContentPane(ventanaMed.getMedico());
                ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ventana.pack();
                ventana.setLocationRelativeTo(null); //lo centra
                ventana.setVisible(true);

                java.awt.Window w = SwingUtilities.getWindowAncestor(Login);
                if (w != null) w.dispose();
            }
        });
    }

    public JPanel getLogin() {
        return Login;
    }
}

