package hospital.Entities.Entities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class Grafica extends JPanel {
    private List<DataPoint> dataPoints;
    private String medicamentoSeleccionado;
    private String titulo;
    private Color colorLinea = new Color(52, 152, 219);
    private Color colorPunto = new Color(231, 76, 60);
    private Color colorFondo = Color.WHITE;
    private Color colorEjes = Color.BLACK;
    private Color colorGrid = new Color(200, 200, 200);

    // Márgenes del gráfico
    private static final int MARGIN_LEFT = 80;
    private static final int MARGIN_RIGHT = 40;
    private static final int MARGIN_TOP = 60;
    private static final int MARGIN_BOTTOM = 80;

    // Clase interna para representar un punto de datos
    public static class DataPoint {
        public final Date fecha;
        public final int cantidad;
        public final String medicamento;

        public DataPoint(Date fecha, int cantidad, String medicamento) {
            this.fecha = fecha;
            this.cantidad = cantidad;
            this.medicamento = medicamento;
        }
    }

    public Grafica() {
        this.dataPoints = new ArrayList<>();
        this.medicamentoSeleccionado = "";
        this.titulo = "Cantidad de Mecicamentos por recenta \n evaluado mensualmente";

        setBackground(colorFondo);
        setPreferredSize(new Dimension(600, 400));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }

    /**
     * Actualiza los datos del gráfico basándose en las recetas filtradas
     */
    public void actualizarDatos(List<Receta> recetas, String medicamento) {
        this.medicamentoSeleccionado = medicamento;
        this.dataPoints.clear();

        if (recetas == null || recetas.isEmpty() || medicamento == null || medicamento.equals("Seleccione un medicamento...")) {
            System.out.println("No hay datos válidos para actualizar la gráfica");
            repaint();
            return;
        }

        // Mapa para agrupar cantidades por fecha
        Map<String, Integer> cantidadPorFecha = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Procesando " + recetas.size() + " recetas para medicamento: " + medicamento);

        // Procesar todas las recetas
        for (Receta receta : recetas) {
            try {
                // Validar que la receta tenga fecha
                Date fechaReceta = obtenerFechaValida(receta);
                if (fechaReceta == null) {
                    System.out.println("Receta sin fecha válida, saltando...");
                    continue;
                }

                // Procesar detalles de la receta
                if (receta.getDetalles() != null) {
                    for (RecipeDetails detalle : receta.getDetalles()) {
                        if (medicamento.equals(detalle
                                .getNombre())) {
                            String fechaStr = sdf.format(fechaReceta);
                            int cantidad = detalle.getCantidad();

                            cantidadPorFecha.merge(fechaStr, cantidad, Integer::sum);
                            System.out.println("Agregado: " + fechaStr + " -> " + cantidad);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error procesando receta: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Datos agrupados por fecha: " + cantidadPorFecha.size() + " entradas");

        // Convertir a DataPoints
        for (Map.Entry<String, Integer> entry : cantidadPorFecha.entrySet()) {
            try {
                Date fecha = sdf.parse(entry.getKey());
                dataPoints.add(new DataPoint(fecha, entry.getValue(), medicamento));
                System.out.println("DataPoint creado: " + entry.getKey() + " -> " + entry.getValue());
            } catch (ParseException e) {
                System.err.println("Error al parsear fecha: " + entry.getKey());
            }
        }

        // Ordenar por fecha
        dataPoints.sort(Comparator.comparing(dp -> dp.fecha));

        // Si no hay datos para el medicamento, agregar un punto con cantidad 0
        if (dataPoints.isEmpty()) {
            if (!medicamento.equals("Seleccione un medicamento...")) {
                Date fechaActual = new Date();
                dataPoints.add(new DataPoint(fechaActual, 0, medicamento));
                System.out.println("No se encontraron datos, agregando punto con cantidad 0");
            }
        }

        System.out.println("Total de puntos de datos: " + dataPoints.size());
        repaint();
    }

    /**
     * Obtiene una fecha válida de la receta, manejando diferentes tipos de datos
     */
    private Date obtenerFechaValida(Receta receta) {
        try {
            // Intentar obtener la fecha directamente
            Object fechaObj = receta.getFechaConfeccion();

            if (fechaObj == null) {
                System.out.println("Fecha de receta es null");
                return new Date(); // Usar fecha actual como fallback
            }

            // Si ya es un Date, devolverlo
            if (fechaObj instanceof Date) {
                return (Date) fechaObj;
            }

            // Si es un String, intentar parsearlo
            if (fechaObj instanceof String) {
                String fechaStr = (String) fechaObj;
                return parsearFechaString(fechaStr);
            }

            // Si es un java.sql.Date
            if (fechaObj instanceof java.sql.Date) {
                return new Date(((java.sql.Date) fechaObj).getTime());
            }

            // Si es un java.sql.Timestamp
            if (fechaObj instanceof java.sql.Timestamp) {
                return new Date(((java.sql.Timestamp) fechaObj).getTime());
            }

            // Si es un LocalDate o LocalDateTime (Java 8+)
            if (fechaObj.getClass().getSimpleName().equals("LocalDate")) {
                // Usar reflection para manejar LocalDate
                try {
                    Object epochDay = fechaObj.getClass().getMethod("toEpochDay").invoke(fechaObj);
                    long days = (Long) epochDay;
                    return new Date(days * 24 * 60 * 60 * 1000L);
                } catch (Exception e) {
                    System.err.println("Error convirtiendo LocalDate: " + e.getMessage());
                }
            }

            System.out.println("Tipo de fecha no reconocido: " + fechaObj.getClass().getName());
            System.out.println("Valor: " + fechaObj.toString());

            // Intentar parsear el toString()
            return parsearFechaString(fechaObj.toString());

        } catch (Exception e) {
            System.err.println("Error obteniendo fecha válida: " + e.getMessage());
            return new Date(); // Fallback a fecha actual
        }
    }

    /**
     * Intenta parsear diferentes formatos de fecha desde String
     */
    private Date parsearFechaString(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return new Date();
        }

        // Formatos comunes a intentar
        String[] formatos = {
                "yyyy-MM-dd",
                "dd/MM/yyyy",
                "MM/dd/yyyy",
                "yyyy-MM-dd HH:mm:ss",
                "dd/MM/yyyy HH:mm:ss",
                "EEE MMM dd HH:mm:ss zzz yyyy" // Para formato como "Tue Jul 01 19:30:54 CST 2025"
        };

        for (String formato : formatos) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                return sdf.parse(fechaStr);
            } catch (ParseException e) {
                // Continuar con el siguiente formato
            }
        }

        System.err.println("No se pudo parsear la fecha: " + fechaStr);
        return new Date(); // Fallback a fecha actual
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Configurar renderizado suave
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Área de dibujo del gráfico
        int graphWidth = width - MARGIN_LEFT - MARGIN_RIGHT;
        int graphHeight = height - MARGIN_TOP - MARGIN_BOTTOM;

        if (dataPoints.isEmpty()) {
            dibujarMensajeVacio(g2d, width, height);
            g2d.dispose();
            return;
        }

        // Dibujar título
        dibujarTitulo(g2d, width);

        // Calcular rangos
        int maxCantidad = dataPoints.stream().mapToInt(dp -> dp.cantidad).max().orElse(1);
        maxCantidad = Math.max(maxCantidad, 1); // Evitar división por cero

        long minTime = dataPoints.stream().mapToLong(dp -> dp.fecha.getTime()).min().orElse(0);
        long maxTime = dataPoints.stream().mapToLong(dp -> dp.fecha.getTime()).max().orElse(0);

        // Si todas las fechas son iguales, expandir el rango
        if (minTime == maxTime) {
            long oneDay = 24 * 60 * 60 * 1000; // Un día en milisegundos
            minTime -= oneDay;
            maxTime += oneDay;
        }

        // Dibujar ejes y grid
        dibujarEjesYGrid(g2d, graphWidth, graphHeight, maxCantidad, minTime, maxTime);

        // Dibujar líneas de datos
        dibujarLineasDatos(g2d, graphWidth, graphHeight, maxCantidad, minTime, maxTime);

        // Dibujar puntos de datos
        dibujarPuntosDatos(g2d, graphWidth, graphHeight, maxCantidad, minTime, maxTime);

        g2d.dispose();
    }

    private void dibujarTitulo(Graphics2D g2d, int width) {
        g2d.setColor(colorEjes);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();

        String tituloCompleto = titulo;
        if (!medicamentoSeleccionado.isEmpty() && !medicamentoSeleccionado.equals("Seleccione un medicamento...")) {
            tituloCompleto += " - " + medicamentoSeleccionado;
        }

        int titleWidth = fm.stringWidth(tituloCompleto);
        int titleX = (width - titleWidth) / 2;
        g2d.drawString(tituloCompleto, titleX, 25);
    }

    private void dibujarMensajeVacio(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        FontMetrics fm = g2d.getFontMetrics();

        String mensaje = "Seleccione un medicamento y rango de fechas para ver el gráfico";
        int messageWidth = fm.stringWidth(mensaje);
        int messageX = (width - messageWidth) / 2;
        int messageY = height / 2;

        g2d.drawString(mensaje, messageX, messageY);
    }

    private void dibujarEjesYGrid(Graphics2D g2d, int graphWidth, int graphHeight, int maxCantidad, long minTime, long maxTime) {
        // Dibujar grid vertical (fechas)
        g2d.setColor(colorGrid);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0));

        int numVerticalLines = 5;
        for (int i = 0; i <= numVerticalLines; i++) {
            int x = MARGIN_LEFT + (i * graphWidth / numVerticalLines);
            g2d.draw(new Line2D.Double(x, MARGIN_TOP, x, MARGIN_TOP + graphHeight));
        }

        // Dibujar grid horizontal (cantidades)
        int numHorizontalLines = 5;
        for (int i = 0; i <= numHorizontalLines; i++) {
            int y = MARGIN_TOP + (i * graphHeight / numHorizontalLines);
            g2d.draw(new Line2D.Double(MARGIN_LEFT, y, MARGIN_LEFT + graphWidth, y));
        }

        // Dibujar ejes principales
        g2d.setColor(colorEjes);
        g2d.setStroke(new BasicStroke(2));

        // Eje Y (cantidades)
        g2d.draw(new Line2D.Double(MARGIN_LEFT, MARGIN_TOP, MARGIN_LEFT, MARGIN_TOP + graphHeight));

        // Eje X (fechas)
        g2d.draw(new Line2D.Double(MARGIN_LEFT, MARGIN_TOP + graphHeight, MARGIN_LEFT + graphWidth, MARGIN_TOP + graphHeight));

        // Etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        for (int i = 0; i <= numHorizontalLines; i++) {
            int cantidad = maxCantidad - (i * maxCantidad / numHorizontalLines);
            int y = MARGIN_TOP + (i * graphHeight / numHorizontalLines);

            String label = String.valueOf(cantidad);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, MARGIN_LEFT - labelWidth - 10, y + 5);
        }

        // Etiquetas del eje X
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        for (int i = 0; i <= numVerticalLines; i++) {
            long time = minTime + (long)((double)i * (maxTime - minTime) / numVerticalLines);
            Date fecha = new Date(time);
            String label = sdf.format(fecha);

            int x = MARGIN_LEFT + (i * graphWidth / numVerticalLines);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, x - labelWidth / 2, MARGIN_TOP + graphHeight + 20);
        }

        // Etiquetas de los ejes
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();

        // Etiqueta eje Y
        String labelY = "Cantidad";
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(labelY, -(MARGIN_TOP + graphHeight / 2 + fm.stringWidth(labelY) / 2), 20);
        g2d.rotate(Math.PI / 2);

        // Etiqueta eje X
        String labelX = "Fecha";
        int labelXWidth = fm.stringWidth(labelX);
        g2d.drawString(labelX, MARGIN_LEFT + graphWidth / 2 - labelXWidth / 2, MARGIN_TOP + graphHeight + 50);
    }

    private void dibujarLineasDatos(Graphics2D g2d, int graphWidth, int graphHeight, int maxCantidad, long minTime, long maxTime) {
        if (dataPoints.size() < 2) return;

        g2d.setColor(colorLinea);
        g2d.setStroke(new BasicStroke(3));

        for (int i = 0; i < dataPoints.size() - 1; i++) {
            DataPoint current = dataPoints.get(i);
            DataPoint next = dataPoints.get(i + 1);

            int x1 = MARGIN_LEFT + (int)((double)(current.fecha.getTime() - minTime) * graphWidth / (maxTime - minTime));
            int y1 = MARGIN_TOP + graphHeight - (current.cantidad * graphHeight / maxCantidad);

            int x2 = MARGIN_LEFT + (int)((double)(next.fecha.getTime() - minTime) * graphWidth / (maxTime - minTime));
            int y2 = MARGIN_TOP + graphHeight - (next.cantidad * graphHeight / maxCantidad);

            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    private void dibujarPuntosDatos(Graphics2D g2d, int graphWidth, int graphHeight, int maxCantidad, long minTime, long maxTime) {
        g2d.setColor(colorPunto);

        for (DataPoint dp : dataPoints) {
            int x = MARGIN_LEFT + (int)((double)(dp.fecha.getTime() - minTime) * graphWidth / (maxTime - minTime));
            int y = MARGIN_TOP + graphHeight - (dp.cantidad * graphHeight / maxCantidad);

            // Dibujar punto
            g2d.fill(new Ellipse2D.Double(x - 4, y - 4, 8, 8));

            // Dibujar valor sobre el punto
            g2d.setColor(colorEjes);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String valor = String.valueOf(dp.cantidad);
            FontMetrics fm = g2d.getFontMetrics();
            int valorWidth = fm.stringWidth(valor);
            g2d.drawString(valor, x - valorWidth / 2, y - 8);

            g2d.setColor(colorPunto);
        }
    }

    // Métodos públicos para configuración
    public void setTitulo(String titulo) {
        this.titulo = titulo;
        repaint();
    }

    public void setColorLinea(Color color) {
        this.colorLinea = color;
        repaint();
    }

    public void setColorPunto(Color color) {
        this.colorPunto = color;
        repaint();
    }

    public String getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    public int getNumeroDataPoints() {
        return dataPoints.size();
    }

    /**
     * Limpia todos los datos del gráfico
     */
    public void limpiar() {
        this.dataPoints.clear();
        this.medicamentoSeleccionado = "";
        repaint();
    }
}