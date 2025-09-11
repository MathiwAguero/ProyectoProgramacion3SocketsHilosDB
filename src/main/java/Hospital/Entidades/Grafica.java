package Hospital.Entidades;

import Hospital.Entidades.Receta;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Grafica extends JPanel {
    private Map<Date, Integer> datosGrafica;
    private String medicamentoSeleccionado;
    private Date fechaInicio;
    private Date fechaFin;

    // Configuración visual
    private final int MARGEN_IZQUIERDO = 70;
    private final int MARGEN_DERECHO = 50;
    private final int MARGEN_SUPERIOR = 50;
    private final int MARGEN_INFERIOR = 70;
    private final Color COLOR_LINEA = new Color(0, 123, 255);
    private final Color COLOR_PUNTOS = new Color(220, 53, 69);
    private final Color COLOR_EJES = Color.BLACK;
    private final Color COLOR_GRILLA = new Color(221, 221, 221);

    public Grafica() {
        this.datosGrafica = new HashMap<>();
        this.medicamentoSeleccionado = "";
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Actualiza los datos de la gráfica con las recetas filtradas
     * @param recetas Lista de recetas del medicamento seleccionado
     * @param medicamento Nombre del medicamento
     */
    public void actualizarDatos(List<Receta> recetas, String medicamento) {
        this.medicamentoSeleccionado = medicamento;
        this.datosGrafica.clear();

        if (recetas == null || recetas.isEmpty()) {
            repaint();
            return;
        }

        // Procesar recetas y agrupar por fecha
        for (Receta receta : recetas) {
            Date fecha = receta.getFechaConfeccion();
            // Obtener cantidad del medicamento específico
            int cantidad = obtenerCantidadMedicamento(receta, medicamento);

            if (cantidad > 0) {
                // Si ya existe la fecha, sumar las cantidades
                datosGrafica.merge(fecha, cantidad, Integer::sum);
            }
        }

        // Establecer rango de fechas
        if (!datosGrafica.isEmpty()) {
            fechaInicio = Collections.min(datosGrafica.keySet());
            fechaFin = Collections.max(datosGrafica.keySet());
        }

        repaint();
    }

    /**
     * Obtiene la cantidad del medicamento específico de una receta
     */
    private int obtenerCantidadMedicamento(Receta receta, String medicamento) {
        try {
            // Asumiendo que tienes un método para obtener detalles de la receta
            if (receta.getDetalles() != null && receta.getDetalles().size() > 0) {
                String codigoMed = receta.getDetalles().get().getCodigoMedicamento();
                // Aquí podrías hacer una comparación más sofisticada o buscar por código
                if (medicamento.equals(codigoMed)) {
                    return receta.getDetalles().get().getCantidad();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener cantidad: " + e.getMessage());
        }
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (datosGrafica.isEmpty()) {
            dibujarMensajeVacio(g2d);
            return;
        }

        // Calcular área de dibujo
        int anchoGrafica = getWidth() - MARGEN_IZQUIERDO - MARGEN_DERECHO;
        int altoGrafica = getHeight() - MARGEN_SUPERIOR - MARGEN_INFERIOR;

        if (anchoGrafica <= 0 || altoGrafica <= 0) return;

        // Obtener valores ordenados por fecha
        List<Map.Entry<Date, Integer>> datosOrdenados = new ArrayList<>(datosGrafica.entrySet());
        datosOrdenados.sort(Map.Entry.comparingByKey());

        // Calcular rangos
        int maxCantidad = Collections.max(datosGrafica.values());
        int minCantidad = 0;

        // Dibujar componentes
        dibujarTitulo(g2d);
        dibujarEjes(g2d, anchoGrafica, altoGrafica, maxCantidad);
        dibujarGrilla(g2d, anchoGrafica, altoGrafica, maxCantidad);
        dibujarDatos(g2d, anchoGrafica, altoGrafica, datosOrdenados, maxCantidad, minCantidad);
        dibujarEtiquetas(g2d, anchoGrafica, altoGrafica);
    }

    private void dibujarMensajeVacio(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        FontMetrics fm = g2d.getFontMetrics();
        String mensaje = "Sin datos para mostrar";
        String submensaje = "Seleccione medicamento y fechas";

        int x1 = (getWidth() - fm.stringWidth(mensaje)) / 2;
        int y1 = getHeight() / 2 - 10;
        int x2 = (getWidth() - fm.stringWidth(submensaje)) / 2;
        int y2 = getHeight() / 2 + 15;

        g2d.drawString(mensaje, x1, y1);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(submensaje, x2, y2);
    }

    private void dibujarTitulo(Graphics2D g2d) {
        g2d.setColor(COLOR_EJES);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();

        String titulo = "Consumo de " + medicamentoSeleccionado;
        if (fechaInicio != null && fechaFin != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            titulo += " (" + sdf.format(fechaInicio) + " - " + sdf.format(fechaFin) + ")";
        }

        int x = (getWidth() - fm.stringWidth(titulo)) / 2;
        g2d.drawString(titulo, x, 25);
    }

    private void dibujarEjes(Graphics2D g2d, int anchoGrafica, int altoGrafica, int maxCantidad) {
        g2d.setColor(COLOR_EJES);
        g2d.setStroke(new BasicStroke(2));

        // Eje Y
        g2d.drawLine(MARGEN_IZQUIERDO, MARGEN_SUPERIOR,
                MARGEN_IZQUIERDO, MARGEN_SUPERIOR + altoGrafica);

        // Eje X
        g2d.drawLine(MARGEN_IZQUIERDO, MARGEN_SUPERIOR + altoGrafica,
                MARGEN_IZQUIERDO + anchoGrafica, MARGEN_SUPERIOR + altoGrafica);
    }

    private void dibujarGrilla(Graphics2D g2d, int anchoGrafica, int altoGrafica, int maxCantidad) {
        g2d.setColor(COLOR_GRILLA);
        g2d.setStroke(new BasicStroke(1));

        // Líneas horizontales
        for (int i = 1; i <= 5; i++) {
            int y = MARGEN_SUPERIOR + altoGrafica - (i * altoGrafica / 5);
            g2d.drawLine(MARGEN_IZQUIERDO, y, MARGEN_IZQUIERDO + anchoGrafica, y);
        }

        // Etiquetas del eje Y
        g2d.setColor(COLOR_EJES);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= 5; i++) {
            int y = MARGEN_SUPERIOR + altoGrafica - (i * altoGrafica / 5);
            int valor = (maxCantidad * i) / 5;

            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(String.valueOf(valor),
                    MARGEN_IZQUIERDO - fm.stringWidth(String.valueOf(valor)) - 10,
                    y + 3);
        }
    }

    private void dibujarDatos(Graphics2D g2d, int anchoGrafica, int altoGrafica,
                              List<Map.Entry<Date, Integer>> datosOrdenados,
                              int maxCantidad, int minCantidad) {

        if (datosOrdenados.size() < 2) {
            // Solo dibujar puntos si hay pocos datos
            dibujarSoloPuntos(g2d, anchoGrafica, altoGrafica, datosOrdenados, maxCantidad);
            return;
        }

        // Dibujar líneas conectoras
        g2d.setColor(COLOR_LINEA);
        g2d.setStroke(new BasicStroke(3));

        for (int i = 0; i < datosOrdenados.size() - 1; i++) {
            Point p1 = calcularPuntoEnGrafica(i, datosOrdenados, anchoGrafica, altoGrafica, maxCantidad);
            Point p2 = calcularPuntoEnGrafica(i + 1, datosOrdenados, anchoGrafica, altoGrafica, maxCantidad);

            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Dibujar puntos
        dibujarPuntos(g2d, anchoGrafica, altoGrafica, datosOrdenados, maxCantidad);
    }

    private void dibujarSoloPuntos(Graphics2D g2d, int anchoGrafica, int altoGrafica,
                                   List<Map.Entry<Date, Integer>> datosOrdenados, int maxCantidad) {
        dibujarPuntos(g2d, anchoGrafica, altoGrafica, datosOrdenados, maxCantidad);
    }

    private void dibujarPuntos(Graphics2D g2d, int anchoGrafica, int altoGrafica,
                               List<Map.Entry<Date, Integer>> datosOrdenados, int maxCantidad) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        for (int i = 0; i < datosOrdenados.size(); i++) {
            Map.Entry<Date, Integer> entrada = datosOrdenados.get(i);
            Point punto = calcularPuntoEnGrafica(i, datosOrdenados, anchoGrafica, altoGrafica, maxCantidad);

            // Dibujar punto rojo
            g2d.setColor(COLOR_PUNTOS);
            g2d.fillOval(punto.x - 5, punto.y - 5, 10, 10);

            // Borde blanco
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(punto.x - 5, punto.y - 5, 10, 10);

            // Valor encima del punto
            g2d.setColor(COLOR_EJES);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            FontMetrics fm = g2d.getFontMetrics();
            String valor = String.valueOf(entrada.getValue());
            g2d.drawString(valor, punto.x - fm.stringWidth(valor)/2, punto.y - 10);

            // Fecha debajo del eje X
            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            fm = g2d.getFontMetrics();
            String fecha = sdf.format(entrada.getKey());
            g2d.drawString(fecha, punto.x - fm.stringWidth(fecha)/2,
                    MARGEN_SUPERIOR + altoGrafica + 20);
        }
    }

    private Point calcularPuntoEnGrafica(int indice, List<Map.Entry<Date, Integer>> datos,
                                         int anchoGrafica, int altoGrafica, int maxCantidad) {

        int x = MARGEN_IZQUIERDO + (indice * anchoGrafica / Math.max(1, datos.size() - 1));
        if (datos.size() == 1) {
            x = MARGEN_IZQUIERDO + anchoGrafica / 2; // Centrar si solo hay un dato
        }

        int cantidad = datos.get(indice).getValue();
        int y = MARGEN_SUPERIOR + altoGrafica - (cantidad * altoGrafica / Math.max(1, maxCantidad));

        return new Point(x, y);
    }

    private void dibujarEtiquetas(Graphics2D g2d, int anchoGrafica, int altoGrafica) {
        g2d.setColor(COLOR_EJES);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();

        // Etiqueta eje X
        String etiquetaX = "Fecha";
        int xEtiqueta = MARGEN_IZQUIERDO + anchoGrafica / 2 - fm.stringWidth(etiquetaX) / 2;
        g2d.drawString(etiquetaX, xEtiqueta, getHeight() - 15);

        // Etiqueta eje Y (rotada)
        Graphics2D g2dRotado = (Graphics2D) g2d.create();
        g2dRotado.rotate(-Math.PI / 2, 20, MARGEN_SUPERIOR + altoGrafica / 2);
        String etiquetaY = "Cantidad";
        g2dRotado.drawString(etiquetaY, 20 - fm.stringWidth(etiquetaY) / 2,
                MARGEN_SUPERIOR + altoGrafica / 2 + fm.getHeight() / 2);
        g2dRotado.dispose();
    }

    /**
     * Limpia los datos de la gráfica
     */
    public void limpiar() {
        datosGrafica.clear();
        medicamentoSeleccionado = "";
        fechaInicio = null;
        fechaFin = null;
        repaint();
    }
}