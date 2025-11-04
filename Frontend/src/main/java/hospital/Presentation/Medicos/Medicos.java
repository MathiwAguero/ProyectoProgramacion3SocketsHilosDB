package hospital.Presentation.Medicos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import hospital.Application;
import hospital.Logic.Listas.Factory;
import hospital.Entities.Entities.*;
import hospital.Presentation.TableModel.TableModelMedicos;

public class Medicos implements PropertyChangeListener {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel medico;
    private JTable table1;

    MedicoController controller;
    ModelMedico model;

    public Medicos() {
        guardarButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medico n = take();
                    try {
                        controller.create(n);
                        JOptionPane.showMessageDialog(medico, "MEDICO GUARDADO");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(medico, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String id = textField1.getText().trim();
                if (!id.isEmpty()) {
                    try {
                        controller.delete(id);
                        JOptionPane.showMessageDialog(medico, "MEDICO ELIMINADO", "", JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(medico, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(medico, "Ingrese ID del medico a eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String search = textField4.getText().trim();
                try {
                    controller.search(search);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(medico, ex.getMessage(), "Informacion", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        reporteButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String busqueda = textField4.getText().trim().toLowerCase();
                List<Medico> meds = Factory.get().medico().obtenerTodos();
                if(busqueda.isEmpty()) {
                    JOptionPane.showMessageDialog(medico, "Debe buscar un medico\n");
                }
                for (Medico m : meds) {
                    if (m.getNombre() != null && m.getNombre().toLowerCase().contains(busqueda)) {
                        String reporte = "Reporte del medico encontrado:\n\n" +
                                "ID: " + m.getId() + "\n" +
                                "Nombre: " + m.getNombre() + "\n" +
                                "Especialidad: " + m.getEspecialidad() + "\n";
                        JOptionPane.showMessageDialog(medico, reporte);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(medico, "No se encontraron a ese nombre");
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    cargarMedicoSeleccionado();
                }
            }
        });
    }

    public JPanel getMedico() {
        return medico;
    }

    public void setController(MedicoController controller) {
        this.controller = controller;
    }

    public void setModel(ModelMedico model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    private void cargarMedicoSeleccionado() {
        int row = table1.getSelectedRow();
        if (row < 0) return;

        int modelRow = table1.convertRowIndexToModel(row);
        Medico medico = model.getList().get(modelRow);

        // Llenar campos del formulario
        textField1.setText(medico.getId() != null ? medico.getId() : "");
        textField2.setText(medico.getNombre() != null ? medico.getNombre() : "");
        textField3.setText(medico.getEspecialidad() != null ? medico.getEspecialidad() : "");

        // Actualizar modelo
        model.setCurrent(medico);

        System.out.println("✓ Médico cargado: " + medico.getNombre());
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelMedico.LIST: {
                int[] cols = {TableModelMedicos.ID, TableModelMedicos.NOMBRE, TableModelMedicos.ESPECIALIDAD};
                table1.setModel(new TableModelMedicos(cols, model.getList()));
                break;
            }
            case ModelMedico.CURRENT: {
                Medico current = model.getCurrent();
                if (current != null) {
                    textField1.setText(current.getId() != null ? current.getId() : "");
                    textField2.setText(current.getNombre() != null ? current.getNombre() : "");
                    textField3.setText(current.getEspecialidad() != null ? current.getEspecialidad() : "");
                }
                textField1.setBackground(null); textField1.setToolTipText(null);
                textField2.setBackground(null); textField2.setToolTipText(null);
                textField3.setBackground(null); textField3.setToolTipText(null);
                break;
            }
        }
        this.medico.revalidate();
        this.medico.repaint();
    }

    public Medico take() {
        Medico m = new Medico();
        m.setId(textField1.getText().trim());
        m.setNombre(textField2.getText().trim());
        m.setEspecialidad(textField3.getText().trim());

        String clave = JOptionPane.showInputDialog(medico, "Ingrese la clave del medico:");
        if (clave != null) m.setClave(clave.trim());

        return m;
    }

    private boolean validate() {
        boolean valid = true;
        if (textField1.getText().trim().isEmpty()) {
            valid = false;
            textField1.setBackground(Application.BACKGROUND_ERROR);
            textField1.setToolTipText("ID requerido");
        } else {
            textField1.setBackground(null);
            textField1.setToolTipText(null);
        }
        if (textField2.getText().trim().isEmpty()) {
            valid = false;
            textField2.setBackground(Application.BACKGROUND_ERROR);
            textField2.setToolTipText("Nombre requerido");
        } else {
            textField2.setBackground(null);
            textField2.setToolTipText(null);
        }
        if (textField3.getText().trim().isEmpty()) {
            valid = false;
            textField3.setBackground(Application.BACKGROUND_ERROR);
            textField3.setToolTipText("Especialidad requerida");
        } else {
            textField3.setBackground(null);
            textField3.setToolTipText(null);
        }
        return valid;
    }

    public void limpiarCampos() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }
}
