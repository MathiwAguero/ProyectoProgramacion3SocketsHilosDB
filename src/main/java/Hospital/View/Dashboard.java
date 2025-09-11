package Hospital.View;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import Hospital.Controller.RecetaController;
import Hospital.Entidades.*;
import Hospital.Exceptions.DataAccessException;
import Hospital.ManejoListas.Factory;
import Hospital.ManejoListas.MedicamentosResumenList;
import Hospital.Model.ModelFarmaceuta;
import Hospital.Model.ModelMedicamentos;
import Hospital.Model.ModelMedicamentosResumen;
import Hospital.Model.ModelReceta;
import Hospital.TableModel.TableModelDashboard;
import Hospital.TableModel.TableModelFarmaceutas;
import Hospital.TableModel.TableModelMedicos;
import Hospital.TableModel.TableModelRecetas;
import com.toedter.calendar.JDateChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard implements PropertyChangeListener  {
    private JPanel Dashboard;
    private JPanel DatosPanel;
    private JTable tabla1;

    private JComboBox MedicamentoCombo;
    private JButton BusquedaUnica;
    private JPanel Medicamentos;
    private JPanel Recetas;
    private JDateChooser DesdeFecha;
    private JDateChooser HastaFecha;

    ModelMedicamentosResumen modelR;
    RecetaController recetaController;
    ModelReceta model;
    private boolean actualizandoModelo = false;
    public Dashboard() {
        MedicamentoCombo.addItem("Selecione un medicamento...");
        for (String m : Factory.get().medicamento().obtenerNombres()) {
            MedicamentoCombo.addItem(m);

        }
        MedicamentoCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (recetaController == null) return;
                if (MedicamentoCombo.getSelectedItem() == null) return;

                String seleccionado = (String) MedicamentoCombo.getSelectedItem();
                System.out.println("Medicamento seleccionado: " + seleccionado);
            }
        });
        BusquedaUnica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTabla();

                limpiar();
            }
        });
        DesdeFecha.addComponentListener(new ComponentAdapter() {
        });
        Dashboard.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                MedicamentoCombo.removeAllItems();
                MedicamentoCombo.addItem("Seleccione un medicamento...");
                for (String m : Factory.get().medicamento().obtenerNombres()) {
                    MedicamentoCombo.addItem(m);
                }
            }
        });
        DesdeFecha.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });

        HastaFecha.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });
    }

    public JPanel getDashboard() {
        return Dashboard;
    }


    public void setController(RecetaController recetaController) {
        this.recetaController = recetaController;
    }
    public void setModel(ModelReceta model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    public void setModelR(ModelMedicamentosResumen model) {
        this.modelR = model;
        this.modelR.addPropertyChangeListener(this); // para que propertyChange funcione
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (actualizandoModelo) return;

        switch (evt.getPropertyName()) {
            case ModelMedicamentosResumen.LIST:


                System.out.println("Modelo actualizado externamente: " + evt.getPropertyName());


                SwingUtilities.invokeLater(() -> {
                    try {
                        int[] cols = {TableModelDashboard.NOMBRE_MEDICAMENTO, TableModelDashboard.NUMERO_MEDICAMENTO};
                        tabla1.setModel(new TableModelDashboard(cols, modelR.getList()));
                        System.out.println("Tabla actualizada desde cambio externo del modelo");
                    } catch (Exception ex) {
                        System.err.println("Error al actualizar tabla desde propertyChange: " + ex.getMessage());
                    }
                });
                break;

            case ModelMedicamentosResumen.CURRENT:

                if (modelR != null) {
                    MedicamentosResumen current = modelR.getCurrent();
                    if (current != null && current.getNombreMedicamento() != null) {
                        SwingUtilities.invokeLater(() -> {
                            String nombreMed = current.getNombreMedicamento();
                            if (!nombreMed.isEmpty()) {
                                MedicamentoCombo.setSelectedItem(nombreMed);
                                System.out.println("ComboBox actualizado desde CURRENT: " + nombreMed);
                            }
                        });
                    }
                }
                break;

            default:

                System.out.println("Property change no manejado: " + evt.getPropertyName());
                break;
        }
    }
    private void createUIComponents() {
        HastaFecha = new JDateChooser();
        HastaFecha.setDateFormatString("dd/MM/yyyy");

        DesdeFecha = new JDateChooser();
        DesdeFecha.setDateFormatString("dd/MM/yyyy");
    }
    public void limpiar() {
        MedicamentoCombo.setSelectedIndex(0);
        DesdeFecha.setDate(null);
        HastaFecha.setDate(null);

    }

    /*public void generarResumenMedicamentos(JDateChooser Desde, JDateChooser Hasta) {
        try {
            List<Receta> recetasFiltradas = recetaController.RecetasPorFecha(Hasta, Desde);

            if (recetasFiltradas == null || recetasFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontraron recetas en el rango de fechas seleccionado");
                return;
            }
            MedicamentosResumenList resumenList = new MedicamentosResumenList();
            for (Receta receta : recetasFiltradas) {
                String nombreMedicamento = receta.getDetalles().get().getCodigoMedicamento();
                int cantidad = receta.getDetalles().get().getCantidad();

                resumenList.insertarConCantidad(nombreMedicamento, cantidad);
            }

            // 4. Actualizar el modelo con los resultados
            ModelMedicamentosResumen modelResumen = new ModelMedicamentosResumen();
            modelResumen.setList(resumenList.obtenerTodos()); // Necesitarás implementar este método

            if (!resumenList.obtenerTodos().isEmpty()) {
                modelResumen.setCurrent(resumenList.obtenerTodos().get(0));
            }

        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null, "Error al generar resumen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
    private void actualizarTabla() {
        if (recetaController == null || modelR == null || actualizandoModelo) {
            return;
        }

        try {
            actualizandoModelo = true;


            if (DesdeFecha.getDate() == null || HastaFecha.getDate() == null) {
                JOptionPane.showMessageDialog(Dashboard,
                        "Por favor, seleccione ambas fechas (Desde y Hasta)",
                        "Fechas requeridas", JOptionPane.WARNING_MESSAGE);
                return;
            }


            String medicamentoSeleccionado = (String) MedicamentoCombo.getSelectedItem();


            if (medicamentoSeleccionado == null || medicamentoSeleccionado.equals("Seleccione un medicamento...")) {
                JOptionPane.showMessageDialog(Dashboard,
                        "Por favor, seleccione un medicamento",
                        "Medicamento requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }


            List<Receta> rec = recetaController.RecetasPorFecha(DesdeFecha, HastaFecha);
            MedicamentosResumenList list = new MedicamentosResumenList();
            list.insertarLista(rec);


            boolean medicamentoExiste = false;
            for (MedicamentosResumen mr : list.obtenerTodos()) {
                if (mr.getNombreMedicamento().equals(medicamentoSeleccionado)) {
                    medicamentoExiste = true;
                    break;
                }
            }


            if (!medicamentoExiste) {
                list.insertarConCantidad(medicamentoSeleccionado, 0);
            }

            modelR.setList(list.obtenerElementosFiltrados(medicamentoSeleccionado));


            int[] cols = {TableModelDashboard.NOMBRE_MEDICAMENTO, TableModelDashboard.NUMERO_MEDICAMENTO};
            tabla1.setModel(new TableModelDashboard(cols, modelR.getList()));


            JOptionPane.showMessageDialog(Dashboard,
                    "Tabla actualizada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(Dashboard,
                    "Error al obtener datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            actualizandoModelo = false;
        }
    }



}
