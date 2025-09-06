package View;

import Application.Main;
import Controller.DetailsController;
import Controller.FarmaceutaController;
import Entidades.RecipeDetails;
import ManejoListas.Factory;
import Model.ModelDetails;
import Model.ModelFarmaceuta;
import TableModel.TableModelDetails;

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

    DetailsController controller;
    ModelDetails model;


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
                    JOptionPane.showMessageDialog(Historial, "No se encontraron los detalles de esa receta");

                }

            }
        });
        detallesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busqueda = textField1.getText().trim().toLowerCase();
                List<RecipeDetails> details = Factory.get().details().obtenerTodos();
                if (busqueda.isEmpty()) {
                    JOptionPane.showMessageDialog(Historial, "Debe buscar\n");
                }
                for (RecipeDetails rd : details) {
                    if (rd.getCodigoMedicamento() != null && rd.getCodigoMedicamento().equals(busqueda)) {
                        String reporte = "Reporte de los detalles:\n\n" +
                                "Codigo medicamento: " + rd.getCodigoMedicamento() + "\n" +
                                "Duracion del tratamiento: " + rd.getDuracionTratamiento() + "dias\n" +
                                "Cantidad: " + rd.getCantidad() + "\n" +
                                "Indicaciones de consumo: " + rd.getIndicaciones() + "\n";
                        JOptionPane.showMessageDialog(Historial, reporte);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(Historial, "No se encontraron a ese nombre");

            }
        });

    }

    public void setModel(ModelDetails model) {

        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(DetailsController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelDetails.LIST: {
                int[] cols = {TableModelDetails.MEDICAMENTO,TableModelDetails.INDICACIONES,
                              TableModelDetails.CANTIDAD,
                              TableModelDetails.DURACION};
                table1.setModel(new TableModelDetails(cols, model.getList()));
                break;
            }
            case ModelDetails.CURRENT: {
                RecipeDetails current = model.getCurrent();
                break;
        }
        }
        this.Historial.revalidate();
    }
}


