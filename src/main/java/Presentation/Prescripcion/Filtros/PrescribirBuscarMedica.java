package Presentation.Prescripcion.Filtros;

import Presentation.Medicamentos.MedicamentoController;
import Logic.Entities.Medicamento;
import Logic.Entities.RecipeDetails;
import Logic.Exceptions.DataAccessException;
import Presentation.Medicamentos.ModelMedicamentos;
import Presentation.TableModel.TableModelMedicamentos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PrescribirBuscarMedica implements PropertyChangeListener  {
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTable table1;
    private JPanel busquedaMedica;
    private JButton buttonCancel;
    private JButton buttonOk;

    MedicamentoController controller;
    ModelMedicamentos model;

    // Detalle que devuelve este flujo despues de OK y guardar en indicaciones
    private RecipeDetails detalleSeleccionado;

    public JPanel getPanel() { return busquedaMedica; }

    public PrescribirBuscarMedica() {
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
                    JOptionPane.showMessageDialog(busquedaMedica, "No se encontró ningún medicamento");
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
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { dispararBusquedaDesdeCombo(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { dispararBusquedaDesdeCombo(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { dispararBusquedaDesdeCombo(); }
            private void dispararBusquedaDesdeCombo() {
                for (ActionListener al : comboBox1.getActionListeners()) {
                    al.actionPerformed(new ActionEvent(comboBox1, ActionEvent.ACTION_PERFORMED, "typing"));
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor(busquedaMedica);
                if (w instanceof JDialog) ((JDialog) w).dispose();
            }
        });

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Medicamento seleccionado = getSelectedMedicamento();
                if (seleccionado == null) {
                    JOptionPane.showMessageDialog(busquedaMedica, "Seleccione un medicamento de la lista.");
                    return;
                }

                PrescribirIndicaciones panelIndicaciones = new PrescribirIndicaciones();
                panelIndicaciones.setCodigoMedicamento(seleccionado.getCodigo());
                panelIndicaciones.setNombreMedicamento(seleccionado.getNombre());

                JDialog d = new JDialog(SwingUtilities.getWindowAncestor(busquedaMedica),
                        "Datos de prescripción", Dialog.ModalityType.APPLICATION_MODAL);
                d.setContentPane(panelIndicaciones.getPrescrip());
                d.pack();
                d.setLocationRelativeTo(busquedaMedica);
                d.setVisible(true);

                RecipeDetails det = panelIndicaciones.getDetalle();
                if (det != null) {
                    detalleSeleccionado = det;

                    Window w = SwingUtilities.getWindowAncestor(busquedaMedica);
                    if (w instanceof JDialog) ((JDialog) w).dispose();
                }
            }
        });

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelMedicamentos.LIST: {
                int[] cols = {
                        TableModelMedicamentos.CODIGO, TableModelMedicamentos.NOMBRE,
                        TableModelMedicamentos.PRESENTACION};
                table1.setModel(new TableModelMedicamentos(cols, model.getList()));
                break;
            }
            case ModelMedicamentos.CURRENT: {
                Medicamento current = model.getCurrent();
                break;
            }
        }
        this.busquedaMedica.revalidate();
    }

    public void setModel(ModelMedicamentos model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(MedicamentoController controller) {
        this.controller = controller;
    }

    public Medicamento getSelectedMedicamento() {
        if (table1.getSelectedRow() < 0) return null;
        int modelRow = table1.convertRowIndexToModel(table1.getSelectedRow());
        return model.getList().get(modelRow);
    }

    public RecipeDetails getDetalleSeleccionado() {
        return detalleSeleccionado;
    }
}
