package hospital.Presentation.Prescripcion.Filtros;

import Presentation.Pacientes.PacienteController;
import Logic.Entities.Paciente;
import Logic.Exceptions.DataAccessException;
import Presentation.Pacientes.ModelPaciente;
import Presentation.TableModel.TableModelPacientes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PrescribirBuscarPacien implements PropertyChangeListener  {
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTable table1;
    private JPanel busquedaPaciente;
    private JButton cancelButton;
    private JButton okButton;

    PacienteController controller;
    ModelPaciente model;

    //guardamos aquI el resultado al dar OK en busqueda de pacientes
    private Paciente pacienteSeleccionado;

    public JPanel getPanel() { return busquedaPaciente; }

    public PrescribirBuscarPacien() {
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller == null) return;
                if (comboBox1.getSelectedItem() == null) return;

                String criterio = comboBox1.getSelectedItem().toString();
                String busqueda = (textField1.getText() == null) ? "" : textField1.getText().trim();

                try {
                    controller.searchComboBox(criterio, busqueda);
                } catch (DataAccessException ex) {
                    JOptionPane.showMessageDialog(busquedaPaciente, "No se encontró nadie");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ActionListener al : comboBox1.getActionListeners()) {
                    al.actionPerformed(new ActionEvent(comboBox1, ActionEvent.ACTION_PERFORMED, "enter"));
                }
            }
        });

        textField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { disparar(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { disparar(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { disparar(); }
            private void disparar() {
                for (ActionListener al : comboBox1.getActionListeners()) {
                    al.actionPerformed(new ActionEvent(comboBox1, ActionEvent.ACTION_PERFORMED, "typing"));
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor(busquedaPaciente);
                if (w instanceof JDialog) ((JDialog) w).dispose();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Paciente sel = getSelectedPaciente();
                if (sel == null) {
                    JOptionPane.showMessageDialog(busquedaPaciente, "Seleccione un paciente de la lista.");
                    return;
                }
                pacienteSeleccionado = sel;

                Window w = SwingUtilities.getWindowAncestor(busquedaPaciente);
                if (w instanceof JDialog) ((JDialog) w).dispose();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelPaciente.LIST: {
                int[] cols = {TableModelPacientes.ID, TableModelPacientes.NOMBRE,
                        TableModelPacientes.TELEFONO, TableModelPacientes.FECNACIMIENTO};
                table1.setModel(new TableModelPacientes(cols, model.getList()));
                break;
            }
            case ModelPaciente.CURRENT: {
                Paciente current = model.getCurrent();
                break;
            }
        }
        this.busquedaPaciente.revalidate();
    }

    public void setModel(ModelPaciente model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(PacienteController controller) {
        this.controller = controller;
    }

    public Paciente getSelectedPaciente() {
        if (table1.getSelectedRow() < 0) return null;
        int modelRow = table1.convertRowIndexToModel(table1.getSelectedRow());
        return model.getList().get(modelRow);
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }
}
