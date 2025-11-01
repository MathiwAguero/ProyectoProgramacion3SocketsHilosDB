package hospital.Presentation.Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Usuarios implements PropertyChangeListener {
    private JPanel panelUsuarios;
    private JTable TablaUsuarios;
    private JButton enviarButton;
    private JButton recibirButton;

    public Usuarios() {
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        recibirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
