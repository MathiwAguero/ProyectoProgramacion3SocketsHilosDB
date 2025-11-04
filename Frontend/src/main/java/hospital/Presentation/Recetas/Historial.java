package hospital.Presentation.Recetas;

import hospital.Entities.Entities.*;
import hospital.Presentation.TableModel.TableModelRecetas;
import hospital.Logic.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

                List<Receta> recetas = null;
                try {
                    recetas = Service.getInstance().findAllRecetas();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                for (Receta rec : recetas) {
                    if (rec.getId() != null && rec.getId().toLowerCase().equals(busqueda)) {
                        String reporte = "Reporte de la receta:\n\n" +
                                "ID: " + rec.getId() + "\n" +
                                "Medico: " + (rec.getMedico() != null ? rec.getMedico().getNombre() : "") + "\n" +
                                "Paciente: " + (rec.getPaciente() != null ? rec.getPaciente().getNombre() : "") + "\n" +
                                "Estado: " + String.valueOf(rec.getEstado()) + "\n" +
                                "Fecha confeccion: " + rec.getFechaConfeccion() + "\n" +
                                "Fecha retiro: " + rec.getFechaRetiro() + "\n" +
                                "Detalles : " + "\n" + (rec.mostarListaDetalles());

                        JOptionPane.showMessageDialog(Historial, reporte);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(Historial, "No se encontraron");
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostrarDetallesRecetaSeleccionada();
                }
            }
        });
    }
    private void mostrarDetallesRecetaSeleccionada() {
        int row = table1.getSelectedRow();
        if (row < 0) return;

        int modelRow = table1.convertRowIndexToModel(row);
        Receta receta = model.getList().get(modelRow);

        // Llenar textField1 con el ID
        textField1.setText(receta.getId());

        // Mostrar detalles completos
        String reporte = "Reporte de la receta:\n\n" +
                "ID: " + receta.getId() + "\n" +
                "Médico: " + (receta.getMedico() != null ? receta.getMedico().getNombre() : "No asignado") + "\n" +
                "Paciente: " + (receta.getPaciente() != null ? receta.getPaciente().getNombre() : "No asignado") + "\n" +
                "Estado: " + receta.getEstado() + "\n" +
                "Fecha confección: " + receta.getFechaConfeccion() + "\n" +
                "Fecha retiro: " + receta.getFechaRetiro() + "\n\n" +
                "Detalles:\n" + receta.mostarListaDetalles();

        JOptionPane.showMessageDialog(Historial, reporte,
                "Detalles de Receta " + receta.getId(),
                JOptionPane.INFORMATION_MESSAGE);

        System.out.println("✓ Receta visualizada: " + receta.getId());
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

