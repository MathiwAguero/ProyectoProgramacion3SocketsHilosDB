package Presentation.Recetas;

import Logic.Entities.Receta;
import Logic.Listas.Factory;
import Presentation.TableModel.TableModelRecetas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class Historial implements PropertyChangeListener {
    private JPanel Historial;
    private JTable table1;
    private JButton buscarButton;
    private JButton detallesButton;
    private JTextField textField1;

    RecetaController controller;
    ModelReceta model;

    public JPanel getHistorial() {
        return Historial;
    }

    public Historial() {

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busqueda = textField1.getText().trim().toLowerCase();
                try {
                    controller.search(busqueda);
                } catch (Exception x) {
                    JOptionPane.showMessageDialog(Historial, "No se encontraron recetas");
                }
            }
        });

        detallesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busqueda = textField1.getText().trim().toLowerCase();
                if (busqueda.isEmpty()) {
                    JOptionPane.showMessageDialog(Historial, "Debe buscar");
                    return;
                }

                List<Receta> recetas = Factory.get().receta().obtenerTodos();
                for (Receta rec : recetas) {
                    if (rec.getId() != null && rec.getId().toLowerCase().equals(busqueda)) {
                        String reporte = "Reporte de la receta:\n\n" +
                                "ID: " + rec.getId() + "\n" +
                                "Medico: " + (rec.getMedico() != null ? rec.getMedico().getNombre() : "") + "\n" +
                                "Paciente: " + (rec.getPaciente() != null ? rec.getPaciente().getNombre() : "") + "\n" +
                                "Estado: " + String.valueOf(rec.getEstado()) + "\n" +
                                "Fecha confeccion: " + rec.getFechaConfeccion() + "\n" +
                                "Fecha retiro: " + rec.getFechaRetiro() + "\n" +
                                "Detalles : " + (rec.mostarListaDetalles());

                        JOptionPane.showMessageDialog(Historial, reporte);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(Historial, "No se encontraron");
            }
        });
    }

    public void setModel(ModelReceta model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(RecetaController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelReceta.LIST: {
                int[] cols = {
                        TableModelRecetas.ID,
                        TableModelRecetas.MEDICO,
                        TableModelRecetas.PACIENTE,
                        TableModelRecetas.ESTADO,
                        TableModelRecetas.FECHARETIRO,
                        TableModelRecetas.FECHACONFECC,
                        TableModelRecetas.DETALLES
                };
                table1.setModel(new TableModelRecetas(cols, model.getList()));
                break;
            }
            case ModelReceta.CURRENT: {
                break;
            }
        }
        this.Historial.revalidate();
    }

    public Receta getSelectedReceta() {
        if (table1.getSelectedRow() < 0) return null;
        int modelRow = table1.convertRowIndexToModel(table1.getSelectedRow());
        return model.getList().get(modelRow);
    }

}

