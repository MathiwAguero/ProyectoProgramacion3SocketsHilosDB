package hospital.Entities.Entities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraficoPastel extends JPanel {

    private DefaultPieDataset dataset;

    public GraficoPastel() {
        setLayout(new BorderLayout());
        dataset = new DefaultPieDataset();

        // Crear gráfico inicial
        JFreeChart chart = ChartFactory.createPieChart(
                "Estados de Recetas", dataset, true, true, false);

        ChartPanel panel = new ChartPanel(chart);
        add(panel, BorderLayout.CENTER);
    }

    public void actualizarDatos(List<Receta> recetas, String medicamento) {
        if (recetas == null || recetas.isEmpty()) {
            dataset.clear();
            return;
        }

        // Contar estados para ese medicamento
        int enProceso = 0, completadas = 0, anuladas = 0, confeccionas=0;

        for (Receta r : recetas) {
            boolean contiene = r.getDetalles().stream()
                    .anyMatch(d -> d.getNombre().equalsIgnoreCase(medicamento));

            if (contiene) {
                switch (r.getEstado()) {
                    case PROCESO -> enProceso++;
                    case LISTA -> completadas++;
                    case ENTREGADA -> anuladas++;
                    case CONFECCIONADA -> confeccionas++;
                }
            }
        }

        // Actualizar dataset
        dataset.clear();
        dataset.setValue("En Proceso", enProceso);
        dataset.setValue("Finalizadas", completadas);
        dataset.setValue("Anuladas", anuladas);
        dataset.setValue("Confeccionas", confeccionas);
    }
}