package View;

import Application.Main;
import Controller.PacienteController;
import Entidades.Farmaceuta;
import Entidades.Paciente;
import ManejoListas.Factory;
import Model.ModelPaciente;
import TableModel.TableModelFarmaceutas;
import TableModel.TableModelPacientes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class Pacientes implements PropertyChangeListener {
    private JPanel Pacientes;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton guadarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JTextField textField5;
    private JButton reporteButton;
    private JButton buscarButton;
    private JTable table1;

    PacienteController controller;
    ModelPaciente model;

    public Pacientes() {
        guadarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validate()){
                    Paciente p1 = take();
                    try {
                        controller.create(p1);
                        JOptionPane.showMessageDialog(Pacientes, "Paciente guardado con exito");
                    } catch(Exception x) {
                        JOptionPane.showMessageDialog(Pacientes, x.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textField2.getText().trim();

                if(!nombre.isEmpty()){
                    try {
                        controller.delete(nombre);
                        JOptionPane.showMessageDialog(Pacientes,  "PACIENTE ELIMINADO", "", JOptionPane.INFORMATION_MESSAGE );
                        limpiarCampos();
                    } catch (Exception x) {
                        JOptionPane.showMessageDialog(Pacientes, x.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Pacientes, "Ingrese ID del paciente a eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textField5.getText().trim().toLowerCase();
                try {
                    controller.search(nombre);
                } catch(Exception x) {
                    JOptionPane.showMessageDialog(Pacientes, "No se encuentra nadie a ese nombre");
                }
            }
        });
        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busqueda = textField5.getText().trim().toLowerCase();
                List<Paciente> pacientes = Factory.get().paciente().obtenerTodos();
                if(busqueda.isEmpty()) {
                    JOptionPane.showMessageDialog(Pacientes, "Debe buscar un paciente\n");
                }
                for (Paciente p : pacientes) {
                if(p.getNombre() != null && p.getNombre().toLowerCase().contains(busqueda)) {
                    String reporte = "Reporte del paciente encontrado:\n\n" +
                            "ID: " + p.getId() + "\n" +
                            "Nombre: " + p.getNombre() + "\n" +
                            "Telefono: " + p.getNumeroTelefonico() + "\n" +
                            "Fecha nacimiento: " + p.getFechaNacimiento() + "\n";
                            JOptionPane.showMessageDialog(Pacientes, reporte);
                    return;
                    }
                }
                JOptionPane.showMessageDialog(Pacientes, "No se encontraron pacientes a ese nombre");
            }
        });
    }

    public Paciente take() {
        Paciente p = new Paciente();
        p.setId(textField1.getText().trim());
        p.setNombre(textField2.getText().trim());
        p.setNumeroTelefonico(textField3.getText().trim());
        p.setFechaNacimiento(textField4.getText().trim());

        return p;
    }

    public boolean validate() {
            boolean valid = true;
            if (textField1.getText().trim().isEmpty()) {
                valid = false;
                textField1.setBackground(Main.BACKGROUND_ERROR);
                textField1.setToolTipText("ID requerido");
            } else {
                textField1.setBackground(null);
                textField1.setToolTipText(null);
            }
            if (textField2.getText().trim().isEmpty()) {
                valid = false;
                textField2.setBackground(Main.BACKGROUND_ERROR);
                textField2.setToolTipText("Nombre requerido");
            } else {
                textField2.setBackground(null);
                textField2.setToolTipText(null);
            }
            if (textField3.getText().trim().isEmpty()) {
                valid = false;
                textField3.setBackground(Main.BACKGROUND_ERROR);
                textField3.setToolTipText("Numero telefonico requerido");
            } else {
                textField3.setBackground(null);
                textField3.setToolTipText(null);
            }
            if (textField4.getText().trim().isEmpty()) {
                valid = false;
                textField4.setBackground(Main.BACKGROUND_ERROR);
                textField4.setToolTipText("Fecha de nacimiento requerido");
            } else {
                textField4.setBackground(null);
                textField4.setToolTipText(null);
            }
            return valid;
    }

    public void setModel(ModelPaciente model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(PacienteController pacienteController) {

        this.controller = pacienteController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelPaciente.LIST: {
                int[] cols = {TableModelPacientes.ID, TableModelPacientes.NOMBRE,
                        TableModelPacientes.TELEFONO, TableModelPacientes.FECNACIMIENTO};
                table1.setModel(new TableModelPacientes(cols, model.getList()));
            }
            case ModelPaciente.CURRENT: {
                Paciente current = model.getCurrent();
                if (current != null) {
                    textField1.setText(current.getId() != null ? current.getId() : "");
                    textField2.setText(current.getNombre() != null ? current.getNombre() : "");
                    textField3.setText(current.getNumeroTelefonico() != null ? current.getNumeroTelefonico() : "");
                    textField4.setText(current.getFechaNacimiento() != null ? current.getFechaNacimiento() : "");

                }
                textField1.setBackground(null);
                textField1.setToolTipText(null);
                textField2.setBackground(null);
                textField2.setToolTipText(null);
                textField3.setBackground(null);
                textField2.setToolTipText(null);
                textField4.setBackground(null);
                textField2.setToolTipText(null);
                break;
            }
        }

        this.Pacientes.revalidate();
    }

    public JPanel getPacientes() {
        return Pacientes;
    }
    public void limpiarCampos() {
        textField3.setText("");
        textField1.setText("");
        textField2.setText("");
        textField4.setText("");
    }

}
