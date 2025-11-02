package hospital.Presentation.Usuarios;

import hospital.Entities.Entities.Mensaje;
import hospital.Entities.Entities.UsuarioBase;
import hospital.Logic.Service;
import hospital.Presentation.TableModel.TableModelUsuarios;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Usuarios implements PropertyChangeListener {
    private JPanel panelUsuarios;
    private JTable TablaUsuarios;
    private JButton enviarButton;
    private JButton recibirButton;


    private ControllerUsuariosMensaje controller;
    private ModelUsuariosMensaje model;

    public Usuarios() {

        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioBase seleccionado = getUsuarioSeleccionado();

                if (seleccionado == null) {
                    JOptionPane.showMessageDialog(panelUsuarios,
                            "Seleccione un usuario de la lista",
                            "Atención",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String mensaje = JOptionPane.showInputDialog(
                        panelUsuarios,
                        "Escriba su mensaje para " + seleccionado.getNombre() + ":",
                        "Enviar Mensaje",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (mensaje != null && !mensaje.trim().isEmpty()) {
                    try {
                        controller.enviarMensaje(seleccionado.getId(), mensaje);
                        JOptionPane.showMessageDialog(panelUsuarios,
                                "Mensaje enviado correctamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panelUsuarios,
                                "Error al enviar mensaje: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        recibirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioBase seleccionado = getUsuarioSeleccionado();

                if (seleccionado == null) {
                    JOptionPane.showMessageDialog(panelUsuarios,
                            "Seleccione un usuario de la tabla",
                            "Atención",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    // ✅ OBTENER SOLO EL PRIMER MENSAJE NO LEÍDO
                    Mensaje mensaje = Service.getInstance()
                            .obtenerPrimerMensajeDe(seleccionado.getId());

                    if (mensaje == null) {
                        JOptionPane.showMessageDialog(panelUsuarios,
                                "No hay más mensajes de " + seleccionado.getNombre(),
                                "Sin mensajes",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }


                    String mensajeFormateado =

                                    "De: " + seleccionado.getNombre() + "\n" +

                                    mensaje.getMensaje();


                    JTextArea textArea = new JTextArea(mensajeFormateado);
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                    textArea.setMargin(new java.awt.Insets(10, 10, 10, 10));

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 200));

                    JOptionPane.showMessageDialog(
                            panelUsuarios,
                            scrollPane,
                            "Mensaje de " + seleccionado.getNombre(),
                            JOptionPane.PLAIN_MESSAGE
                    );

                    // Opcional: Mostrar si hay más mensajes
                    System.out.println("✓ Mensaje mostrado. Presione 'Recibir' nuevamente si hay más.");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelUsuarios,
                            "Error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }


    private UsuarioBase getUsuarioSeleccionado() {
        int fila = TablaUsuarios.getSelectedRow();
        if (fila < 0) return null;

        int modelRow = TablaUsuarios.convertRowIndexToModel(fila);
        if (model == null || model.getList() == null) return null;

        return model.getList().get(modelRow);
    }

    public void setController(ControllerUsuariosMensaje controller) {
        this.controller = controller;
    }

    public void setModel(ModelUsuariosMensaje model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public JPanel getPanel() {
        return panelUsuarios;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelUsuariosMensaje.LIST: {

                int[] cols = {TableModelUsuarios.ID};
                TablaUsuarios.setModel(new TableModelUsuarios(cols, model.getList()));
                TablaUsuarios.revalidate();
                break;
            }
            case ModelUsuariosMensaje.MENSAJES: {
                // Los mensajes se muestran al presionar "Recibir"
                // Opcionalmente, puedes mostrar una notificación visual aquí
                break;
            }
        }
    }
}