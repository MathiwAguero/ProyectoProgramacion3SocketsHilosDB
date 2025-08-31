package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import DataAccessObject.DAOFactory;
import Exceptions.DataAccessException;
import Model.Medico;
import TableModel.TableModelMedicos;

public class Medicos {
    private JTextField textField1;
    private JTextField textField2;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JTextField textField3;
    private JTextField textField4;
    private JButton buscarButton;
    private JButton reporteButton;
    private JPanel medico;
    private JTable table1;

    public Medicos() {
        cargarTabla();

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField1.getText().trim();
                String nombre = textField2.getText().trim();
                String especialidad = textField3.getText().trim();

                String clave = JOptionPane.showInputDialog(medico, "Ingrese la clave del medico:");
                clave = clave.trim();

                try {
                    Medico nuevo = new Medico(id, clave, nombre, especialidad);
                    if (DAOFactory.get().medico().existeId(id)) {
                        DAOFactory.get().medico().actualizar(nuevo);
                        JOptionPane.showMessageDialog(medico, "Medico actualizado");
                    } else {
                        DAOFactory.get().medico().insertar(nuevo);
                        JOptionPane.showMessageDialog(medico, "Medico insertado");
                    }
                    cargarTabla();
                    limpiarCampos();
                } catch (DataAccessException y) {
                    JOptionPane.showMessageDialog(medico, y.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                String nombre = textField2.getText().trim().toLowerCase();
                List<Medico> lista = DAOFactory.get().medico().obtenerTodos();
                for (Medico m : lista) {
                    if (m.getNombre().toLowerCase().equals(nombre)) {
                        try {
                            DAOFactory.get().medico().eliminar(m.getId());
                            cargarTabla();
                            limpiarCampos();
                            return;
                        } catch (DataAccessException ex) {
                            JOptionPane.showMessageDialog(medico, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                JOptionPane.showMessageDialog(medico, "No se encontro medico con ese nombre.");
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String q = textField4.getText().trim().toLowerCase();
                List<Medico> lista = DAOFactory.get().medico().obtenerTodos();
                if (!q.isEmpty()) {
                    lista = lista.stream()
                            .filter(m -> (m.getNombre()!=null && m.getNombre().toLowerCase().contains(q))).toList();
                }
                setTable(lista);
            }
        });

        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String q = textField4.getText().trim().toLowerCase();
                List<Medico> lista = DAOFactory.get().medico().obtenerTodos();
                if (!q.isEmpty()) {
                    lista = lista.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase().contains(q)).toList();
                }
                StringBuilder reporte = new StringBuilder("Reporte de Medicos\n\n");
                for (Medico m : lista) {
                    reporte.append("ID: ").append(m.getId()).append("\n")
                            .append("Nombre: ").append(m.getNombre()).append("\n")
                            .append("Especialidad: ").append(m.getEspecialidad()).append("\n\n");
                }
                JOptionPane.showMessageDialog(medico, reporte.toString());
            }
        });

    }


    private void setTable(List<Medico> lista) {
        int[] cols = {TableModelMedicos.ID, TableModelMedicos.NOMBRE, TableModelMedicos.ESPECIALIDAD};
        table1.setModel(new TableModelMedicos(cols, lista));
        table1.setAutoCreateRowSorter(true);
        table1.setFillsViewportHeight(true);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void cargarTabla() {
        setTable(DAOFactory.get().medico().obtenerTodos());
    }

    public void limpiarCampos() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    public JPanel getMedico() {
        return medico;
    }
}
