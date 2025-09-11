package Presentation.Medicamentos;

import Application.Main;
import Presentation.Medicamentos.MedicamentoController;
import Logic.Entities.Medicamento;
import Logic.Listas.Factory;
import Presentation.TableModel.TableModelMedicamentos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class Medicamentos implements PropertyChangeListener  {

    MedicamentoController controller;
    ModelMedicamentos model;
    private JPanel Medicamentos;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton guardarButton;
    private JTextField textField4;
    private JTable table1;
    private JButton buscarButton;
    private JButton reporteButton;

    public Medicamentos() {
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField1.getText();
                if(!id.isEmpty()) {
                    try {
                        controller.delete(id);
                        JOptionPane.showMessageDialog(Medicamentos, "MEDICAMENTO ELIMINADO", "", JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Medicamentos, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(Medicamentos, "Ingrese codigo del medicamento a eliminar", "Error", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validate()) {
                    Medicamento nuevo = take();

                    try {
                        controller.create(nuevo);
                        JOptionPane.showMessageDialog(Medicamentos, "Medicamento guardado con exito");
                    } catch (Exception x) {
                        JOptionPane.showMessageDialog(Medicamentos, x.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String busqueda = textField4.getText().trim().toLowerCase();
                List<Medicamento> listmedicamentos = Factory.get().medicamento().obtenerTodos();
                if(busqueda.isEmpty()) {
                    JOptionPane.showMessageDialog(Medicamentos, "Debe buscar un medicamento\n");
                }
                for (Medicamento m : listmedicamentos) {
                    if(m.getNombre() != null && m.getNombre().toLowerCase().contains(busqueda)) {
                        String reporte = "Reporte del medicamento encontrado:\n\n" +
                                "Codigo: " + m.getCodigo() + "\n" +
                                "Nombre: " + m.getNombre() + "\n" +
                                "Presentacion: " + m.getPresentacion() + "\n";
                                JOptionPane.showMessageDialog(Medicamentos, reporte);
                                return;
                    }
                }
                JOptionPane.showMessageDialog(Medicamentos, "No se encontraron medicamentos a ese nombre");
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busqueda = textField4.getText().trim();
                try {
                    controller.search(busqueda);
                } catch (Exception x) {
                    JOptionPane.showMessageDialog(Medicamentos, "No se encuentra medicamento a ese nombre");
                }
            }
        });
    }

    public void setModel(ModelMedicamentos model) {

        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(MedicamentoController medicamentoController) {

        this.controller = medicamentoController;
    }

    public JPanel getMedicamentos() {
        return Medicamentos;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()) {
            case ModelMedicamentos.LIST: {
                int[] cols = {TableModelMedicamentos.CODIGO, TableModelMedicamentos.NOMBRE, TableModelMedicamentos.PRESENTACION};
                table1.setModel(new TableModelMedicamentos(cols, model.getList()));
                break;
            }
            case ModelMedicamentos.CURRENT: {
                Medicamento current = model.getCurrent();
                if (current != null) {
                    textField1.setText(current.getCodigo() != null ? current.getCodigo() : "");
                    textField2.setText(current.getNombre() != null ? current.getNombre() : "");
                    textField3.setText(current.getPresentacion() != null ? current.getPresentacion() : "");
                }
                textField1.setBackground(null);
                textField1.setToolTipText(null);
                textField2.setBackground(null);
                textField2.setToolTipText(null);
                textField3.setBackground(null);
                textField3.setToolTipText(null);
                break;
            }
        }
        this.Medicamentos.revalidate();
    }

    public void limpiarCampos() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
    }

    public boolean validate() {
        boolean valid = true;
        if(textField1.getText().isEmpty()){
            valid = false;
            textField1.setBackground(Main.BACKGROUND_ERROR);
            textField1.setToolTipText("Codigo requerido");
        } else {
            textField1.setBackground(null);
            textField1.setToolTipText(null);
        }
        if(textField2.getText().isEmpty()){
            valid = false;
            textField2.setBackground(Main.BACKGROUND_ERROR);
            textField2.setToolTipText("Nombre requerido");
        } else {
            textField2.setBackground(null);
            textField2.setToolTipText(null);
        }
        if(textField3.getText().isEmpty()){
            valid = false;
            textField3.setBackground(Main.BACKGROUND_ERROR);
            textField3.setToolTipText("Presentacion del medicamento requerido");
        } else {
            textField3.setBackground(null);
            textField3.setToolTipText(null);
        }
        return valid;
    }

    public Medicamento take() {
        Medicamento medicam1 = new Medicamento();
        medicam1.setCodigo(textField1.getText());
        medicam1.setNombre(textField2.getText());
        medicam1.setPresentacion(textField3.getText());
        return medicam1;

    }

}
