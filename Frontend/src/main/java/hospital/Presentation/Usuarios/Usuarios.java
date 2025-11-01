package hospital.Presentation.Usuarios;

import hospital.Entities.Entities.UsuarioBase;
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

        // Botón Recibir - Muestra historial de mensajes
        recibirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model == null) return;

                java.util.List<String> mensajes = model.getMensajes();

                if (mensajes == null || mensajes.isEmpty()) {
                    JOptionPane.showMessageDialog(panelUsuarios,
                            "No hay mensajes recibidos",
                            "Mensajes",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }


                JTextArea textArea = new JTextArea(15, 40);
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);

                StringBuilder sb = new StringBuilder();
                for (String msg : mensajes) {
                    sb.append(msg).append("\n\n");
                }
                textArea.setText(sb.toString());
                textArea.setCaretPosition(0);

                JScrollPane scrollPane = new JScrollPane(textArea);

                int option = JOptionPane.showOptionDialog(
                        panelUsuarios,
                        scrollPane,
                        "Mensajes Recibidos (" + mensajes.size() + ")",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Cerrar", "Limpiar Historial"},
                        "Cerrar"
                );

                // Si presionó "Limpiar Historial"
                if (option == 1) {
                    int confirm = JOptionPane.showConfirmDialog(
                            panelUsuarios,
                            "¿Desea limpiar el historial de mensajes?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        controller.limpiarMensajes();
                        JOptionPane.showMessageDialog(panelUsuarios,
                                "Historial limpiado",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
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