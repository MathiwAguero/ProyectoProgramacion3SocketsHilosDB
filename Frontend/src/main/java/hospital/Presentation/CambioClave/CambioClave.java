package hospital.Presentation.CambioClave;

import javax.swing.*;
import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.DataAccessException;
import hospital.Logic.Services.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CambioClave {

    private JPanel CambioClave;
    private JButton button1;
    private JButton button2;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JPasswordField passwordField3;

    private JDialog dialog;
    private ServiceLogin service;
    private UsuarioBase usuario;
    private boolean changed = false;

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
                String actual = new String(passwordField1.getPassword()).trim();
                String nueva  = new String(passwordField2.getPassword()).trim();
                String confirmar = new String(passwordField3.getPassword()).trim();

                if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
                    JOptionPane.showMessageDialog(CambioClave, "Complete todos los campos");
                    return;
                }
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
                    service.cambioClave(usuario.getId(), nueva);
                    usuario.setClave(nueva);

                    JOptionPane.showMessageDialog(CambioClave, "Se cambio la clave correctamente");
                    changed = true;
                    dialog.dispose();

                } catch (DataAccessException x) {
                    JOptionPane.showMessageDialog(CambioClave, x.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void initialization(ServiceLogin service, UsuarioBase usuario) {
        this.service = service;
        this.usuario = usuario;
    }

    public static boolean open(java.awt.Window parent, ServiceLogin service, UsuarioBase usuario) {
        CambioClave cc = new CambioClave();
        cc.initialization(service, usuario);

        cc.dialog = new JDialog(parent, "Cambiar clave", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        cc.dialog.setContentPane(cc.CambioClave);
        cc.dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        cc.dialog.getRootPane().setDefaultButton(cc.button1);
        cc.dialog.pack();
        cc.dialog.setLocationRelativeTo(parent);
        cc.dialog.setVisible(true);

        return cc.changed;
    }

    public JPanel getPanel() { return CambioClave; }
}
