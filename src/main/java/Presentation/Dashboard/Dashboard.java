package Presentation.Dashboard;
import java.awt.*;
import java.awt.event.ComponentEvent;
import Logic.Entities.GraficoPastel;
import Logic.Entities.Grafica;
import Presentation.Recetas.RecetaController;
import Logic.Exceptions.DataAccessException;
import Logic.Listas.Factory;
import Logic.Listas.MedicamentosResumenList;
import Presentation.Recetas.ModelReceta;
import Presentation.TableModel.TableModelDashboard;
import Logic.Entities.MedicamentosResumen;
import Logic.Entities.Receta;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

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
    private Grafica grafica;
    private GraficoPastel graficaPastel;
    private List<Receta> recetasActuales;

    public Dashboard() {
        // Inicializar gráficas
        grafica = new Grafica();
        graficaPastel = new GraficoPastel();

        // Panel Recetas -> gráfico pastel
        Recetas.setLayout(new BorderLayout());
        Recetas.add(graficaPastel, BorderLayout.CENTER);

        // ComboBox medicamentos
        MedicamentoCombo.addItem("Seleccione un medicamento...");
        for (String m : Factory.get().medicamento().obtenerNombres()) {
            MedicamentoCombo.addItem(m);
        }

        // Acción al seleccionar medicamento
        MedicamentoCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (recetaController == null) return;
                if (MedicamentoCombo.getSelectedItem() == null) return;

                String seleccionado = (String) MedicamentoCombo.getSelectedItem();
                if (!seleccionado.equals("Seleccione un medicamento...")) {
                    actualizarGrafica(seleccionado);
                    try {
                        actualizarGraficaPastel(seleccionado);
                    } catch (DataAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // Botón buscar/guardar
        BusquedaUnica.setText("Guardar");
        BusquedaUnica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTablaYGrafica();
            }
        });

        // Recargar combo y gráfica al mostrar dashboard
        Dashboard.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                MedicamentoCombo.removeAllItems();
                MedicamentoCombo.addItem("Seleccione un medicamento...");
                for (String m : Factory.get().medicamento().obtenerNombres()) {
                    MedicamentoCombo.addItem(m);
                }
                integrarGraficaEnPanel();
            }
        });

        // Escuchar cambios en fechas
        DesdeFecha.addPropertyChangeListener("date", evt ->
                System.out.println("Fecha 'Desde' cambió: " + DesdeFecha.getDate()));
        HastaFecha.addPropertyChangeListener("date", evt ->
                System.out.println("Fecha 'Hasta' cambió: " + HastaFecha.getDate()));

        // Integrar gráfica inicial
        SwingUtilities.invokeLater(() -> integrarGraficaEnPanel());
    }

    // Método para integrar la gráfica principal en el panel Medicamentos
    private void integrarGraficaEnPanel() {
        if (Medicamentos == null) {
            System.out.println("ERROR: Panel Medicamentos es null");
            return;
        }

        Medicamentos.removeAll();
        Medicamentos.setLayout(new BorderLayout());
        grafica.setPreferredSize(new Dimension(580, 350));
        Medicamentos.add(grafica, BorderLayout.CENTER);

        Medicamentos.revalidate();
        Medicamentos.repaint();

        System.out.println("✓ Gráfica principal integrada en el panel Medicamentos");
    }

    // Método principal que actualiza tabla y gráficas
    private void actualizarTablaYGrafica() {
        if (recetaController == null || modelR == null || actualizandoModelo) return;

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

            // Obtener recetas filtradas
            recetasActuales = recetaController.RecetasPorFecha(DesdeFecha, HastaFecha);

            MedicamentosResumenList list = new MedicamentosResumenList();
            list.insertarLista(recetasActuales);

            List<Receta> porNombre = recetaController.FiltradasPorNombre(medicamentoSeleccionado, recetasActuales);
            graficaPastel.actualizarDatos(porNombre, medicamentoSeleccionado);

            // Si medicamento no aparece, añadir con cantidad 0
            boolean existe = list.obtenerTodos().stream()
                    .anyMatch(mr -> mr.getNombreMedicamento().equals(medicamentoSeleccionado));
            if (!existe) {
                list.insertarConCantidad(medicamentoSeleccionado, 0);
            }

            // Actualizar tabla
            modelR.setList(list.obtenerElementosFiltrados(medicamentoSeleccionado));
            int[] cols = {TableModelDashboard.NOMBRE_MEDICAMENTO, TableModelDashboard.NUMERO_MEDICAMENTO};
            tabla1.setModel(new TableModelDashboard(cols, modelR.getList()));

            // Actualizar gráficas
            SwingUtilities.invokeLater(() -> {
                actualizarGrafica(medicamentoSeleccionado);
                try {
                    actualizarGraficaPastel(medicamentoSeleccionado);
                } catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }
            });

            JOptionPane.showMessageDialog(Dashboard,
                    "Datos guardados y gráfica actualizada correctamente",
                    "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            actualizandoModelo = false;
        }
    }

    // Actualizar gráfica principal
    private void actualizarGrafica(String medicamento) {
        if (grafica == null) return;

        if (recetasActuales != null && !recetasActuales.isEmpty()
                && medicamento != null && !medicamento.equals("Seleccione un medicamento...")) {
            grafica.actualizarDatos(recetasActuales, medicamento);
        } else {

        }
    }

    // Actualizar gráfica pastel
    private void actualizarGraficaPastel(String medicamento) throws DataAccessException {
        if (graficaPastel == null) return;

        if (recetasActuales != null && !recetasActuales.isEmpty()
                && medicamento != null && !medicamento.equals("Seleccione un medicamento...")) {
            List<Receta> filtradas = recetaController.FiltradasPorNombre(medicamento, recetasActuales);
            graficaPastel.actualizarDatos(filtradas, medicamento);
        }
    }

    // Métodos de acceso y propertyChange
    public JPanel getDashboard() { return Dashboard; }

    public void setController(RecetaController recetaController) {
        this.recetaController = recetaController;
    }

    public void setModel(ModelReceta model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setModelR(ModelMedicamentosResumen model) {
        this.modelR = model;
        this.modelR.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (actualizandoModelo) return;

        if (ModelMedicamentosResumen.LIST.equals(evt.getPropertyName())) {
            SwingUtilities.invokeLater(() -> {
                int[] cols = {TableModelDashboard.NOMBRE_MEDICAMENTO, TableModelDashboard.NUMERO_MEDICAMENTO};
                tabla1.setModel(new TableModelDashboard(cols, modelR.getList()));
                actualizarGraficaConSeleccionActual();
            });
        } else if (ModelMedicamentosResumen.CURRENT.equals(evt.getPropertyName())) {
            MedicamentosResumen current = modelR.getCurrent();
            if (current != null) {
                SwingUtilities.invokeLater(() -> {
                    MedicamentoCombo.setSelectedItem(current.getNombreMedicamento());
                    actualizarGrafica(current.getNombreMedicamento());
                });
            }
        }
    }

    private void actualizarGraficaConSeleccionActual() {
        String seleccionado = (String) MedicamentoCombo.getSelectedItem();
        if (seleccionado != null && !seleccionado.equals("Seleccione un medicamento...")) {
            actualizarGrafica(seleccionado);
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
        if (grafica != null) ;
    }

    public Grafica getGrafica() {
        return grafica;
    }
}