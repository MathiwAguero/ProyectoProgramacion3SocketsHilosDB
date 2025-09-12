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
    private List<Receta> recetasActuales;
    private GraficoPastel graficaPastel;

    public Dashboard() {
        // NUEVO: Inicializar la gráfica
        grafica = new Grafica();
        graficaPastel = new GraficoPastel();
        Recetas.setLayout(new BorderLayout());
        Recetas.add(graficaPastel, BorderLayout.CENTER);
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

                // Actualiza tu grafica principal (ya existente)
                actualizarGrafica(seleccionado);

                // NUEVO: Actualiza la graficaPastel
                try {
                    actualizarGraficaPastel(seleccionado);
                } catch (DataAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        MedicamentoCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (recetaController == null) return;
                if (MedicamentoCombo.getSelectedItem() == null) return;

                String seleccionado = (String) MedicamentoCombo.getSelectedItem();
                System.out.println("Medicamento seleccionado: " + seleccionado);

                // NUEVO: Actualizar gráfica cuando cambia la selección
                actualizarGrafica(seleccionado);
            }
        });

        // MODIFICADO: Cambiar el nombre del botón a "Guardar" según tu requerimiento
        BusquedaUnica.setText("Guardar");
        BusquedaUnica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón Guardar presionado - Actualizando tabla y gráfica");
                actualizarTablaYGrafica();
                // No llamamos limpiar() para mantener los datos visibles
            }
        });

        Dashboard.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                MedicamentoCombo.removeAllItems();
                MedicamentoCombo.addItem("Seleccione un medicamento...");
                for (String m : Factory.get().medicamento().obtenerNombres()) {
                    MedicamentoCombo.addItem(m);
                }

                // NUEVO: Integrar gráfica cuando se muestra el dashboard
                integrarGraficaEnPanel();
            }
        });

        DesdeFecha.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Fecha 'Desde' cambió: " + DesdeFecha.getDate());
            }
        });

        HastaFecha.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Fecha 'Hasta' cambió: " + HastaFecha.getDate());
            }
        });

        // NUEVO: Intentar integrar la gráfica inmediatamente
        SwingUtilities.invokeLater(() -> integrarGraficaEnPanel());
    }

    // NUEVO: Método específico para GridLayoutManager del UI Designer
    private void integrarGraficaEnPanel() {
        if (Medicamentos == null) {
            System.out.println("ERROR: Panel Medicamentos es null");
            return;
        }

        try {
            System.out.println("=== INTEGRANDO GRÁFICA EN PANEL CON GRIDLAYOUTMANAGER ===");
            System.out.println("Panel: " + Medicamentos.getClass().getName());
            System.out.println("Layout: " + Medicamentos.getLayout().getClass().getName());
            System.out.println("Componentes actuales: " + Medicamentos.getComponentCount());

            // Configurar la gráfica
            grafica.setPreferredSize(new Dimension(580, 350));
            grafica.setMinimumSize(new Dimension(400, 250));
            grafica.setVisible(true);
            grafica.setOpaque(true);
            grafica.setBackground(Color.WHITE);
            grafica.setBorder(BorderFactory.createTitledBorder("Gráfica de Medicamentos"));

            // Estrategia específica para GridLayoutManager
            if (Medicamentos.getLayout().getClass().getName().contains("GridLayoutManager")) {
                System.out.println("Detectado GridLayoutManager - usando estrategia específica");

                try {
                    // Crear GridConstraints usando reflection
                    Class<?> gridConstraintsClass = Class.forName("com.intellij.uiDesigner.core.GridConstraints");

                    // Constructor: GridConstraints(row, column, rowSpan, colSpan, anchor, fill, HSizePolicy, VSizePolicy, minimumSize, preferredSize, maximumSize)
                    Object constraints = gridConstraintsClass.getConstructor(
                            int.class, // row
                            int.class, // column
                            int.class, // rowSpan
                            int.class, // colSpan
                            int.class, // anchor
                            int.class, // fill
                            int.class, // HSizePolicy
                            int.class, // VSizePolicy
                            Dimension.class, // minimumSize
                            Dimension.class, // preferredSize
                            Dimension.class  // maximumSize
                    ).newInstance(
                            0, // row
                            0, // column
                            1, // rowSpan
                            1, // colSpan
                            0, // anchor (CENTER)
                            3, // fill (BOTH)
                            3, // HSizePolicy (CAN_GROW | CAN_SHRINK)
                            3, // VSizePolicy (CAN_GROW | CAN_SHRINK)
                            new Dimension(400, 250), // minimumSize
                            new Dimension(580, 350), // preferredSize
                            new Dimension(800, 500)  // maximumSize
                    );

                    // Limpiar panel y agregar con constraints
                    Medicamentos.removeAll();
                    Medicamentos.add(grafica, constraints);

                    System.out.println("✓ Gráfica agregada con GridConstraints");

                } catch (Exception e) {
                    System.err.println("Error con GridConstraints, probando método alternativo: " + e.getMessage());

                    // Fallback: Intentar sin constraints específicos
                    try {
                        Medicamentos.removeAll();
                        Medicamentos.add(grafica);
                        System.out.println("✓ Gráfica agregada sin constraints específicos");
                    } catch (Exception e2) {
                        System.err.println("Fallback también falló: " + e2.getMessage());
                        usarEstrategiaAlternativa();
                        return;
                    }
                }

            } else {
                System.out.println("Layout no es GridLayoutManager, usando método estándar");
                Medicamentos.removeAll();
                Medicamentos.add(grafica, BorderLayout.CENTER);
            }

            // Forzar actualización del layout
            Medicamentos.revalidate();
            Medicamentos.repaint();

            // Propagar hacia arriba
            Container parent = Medicamentos.getParent();
            while (parent != null) {
                parent.revalidate();
                parent.repaint();
                parent = parent.getParent();
            }

            System.out.println("✓ Gráfica integrada exitosamente");
            System.out.println("Componentes después: " + Medicamentos.getComponentCount());

        } catch (Exception e) {
            System.err.println("Error en integrarGraficaEnPanel: " + e.getMessage());
            e.printStackTrace();
            usarEstrategiaAlternativa();
        }
    }

    // NUEVO: Estrategia alternativa - ventana flotante anclada
    private void usarEstrategiaAlternativa() {
        System.out.println("=== USANDO ESTRATEGIA ALTERNATIVA: VENTANA ANCLADA ===");

        SwingUtilities.invokeLater(() -> {
            try {
                // Crear ventana flotante que se mantenga visible
                JFrame ventanaGrafica = new JFrame("Gráfica de Medicamentos - Dashboard");
                ventanaGrafica.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                ventanaGrafica.setSize(650, 450);

                // Posicionar cerca del dashboard principal
                if (Dashboard.getParent() != null) {
                    Window ventanaPrincipal = SwingUtilities.getWindowAncestor(Dashboard);
                    if (ventanaPrincipal != null) {
                        Point ubicacion = ventanaPrincipal.getLocation();
                        ventanaGrafica.setLocation(ubicacion.x + ventanaPrincipal.getWidth() + 10, ubicacion.y);
                    }
                }

                // Configurar contenido
                JPanel contenido = new JPanel(new BorderLayout());
                contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Panel superior con información
                JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel labelInfo = new JLabel("🔄 Se actualiza automáticamente con sus búsquedas");
                labelInfo.setFont(new Font("Arial", Font.BOLD, 12));
                labelInfo.setForeground(new Color(0, 120, 0));
                panelInfo.add(labelInfo);

                contenido.add(panelInfo, BorderLayout.NORTH);
                contenido.add(grafica, BorderLayout.CENTER);

                // Panel inferior con controles
                JPanel panelControles = new JPanel(new FlowLayout());

                JButton btnMantenerVisible = new JButton("Mantener Siempre Visible");
                btnMantenerVisible.addActionListener(e -> {
                    ventanaGrafica.setAlwaysOnTop(!ventanaGrafica.isAlwaysOnTop());
                    btnMantenerVisible.setText(ventanaGrafica.isAlwaysOnTop() ?
                            "Permitir Ocultar" : "Mantener Siempre Visible");
                });

                JButton btnColores = new JButton("Cambiar Colores");
                btnColores.addActionListener(e -> cambiarColoresGrafica());

                panelControles.add(btnMantenerVisible);
                panelControles.add(btnColores);
                contenido.add(panelControles, BorderLayout.SOUTH);

                ventanaGrafica.add(contenido);

                // Guardar referencia para uso posterior
                this.ventanaGrafica = ventanaGrafica;

                // Mostrar la ventana
                ventanaGrafica.setVisible(true);

                System.out.println("✓ Ventana de gráfica creada y mostrada");

                // Opcional: Agregar botón en el dashboard principal para controlar la ventana
                agregarBotonControlGrafica();

            } catch (Exception e) {
                System.err.println("Error creando ventana alternativa: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    // NUEVO: Referencia a ventana gráfica
    private JFrame ventanaGrafica;

    // NUEVO: Agregar botón de control en el dashboard
    private void agregarBotonControlGrafica() {
        try {
            if (DatosPanel != null) {
                JButton btnGrafica = new JButton("📊 Ver Gráfica");
                btnGrafica.setPreferredSize(new Dimension(120, 30));
                btnGrafica.addActionListener(e -> {
                    if (ventanaGrafica != null) {
                        if (ventanaGrafica.isVisible()) {
                            ventanaGrafica.setVisible(false);
                            btnGrafica.setText("📊 Ver Gráfica");
                        } else {
                            ventanaGrafica.setVisible(true);
                            btnGrafica.setText("📊 Ocultar Gráfica");
                        }
                    }
                });

                // Intentar agregar el botón al panel de datos
                try {
                    DatosPanel.add(btnGrafica);
                    DatosPanel.revalidate();
                    DatosPanel.repaint();
                    System.out.println("✓ Botón de control agregado al panel de datos");
                } catch (Exception e) {
                    System.out.println("No se pudo agregar botón al DatosPanel, será manejado por la ventana");
                }
            }
        } catch (Exception e) {
            System.err.println("Error agregando botón de control: " + e.getMessage());
        }
    }

    // NUEVO: Cambiar colores de la gráfica
    private void cambiarColoresGrafica() {
        String[] opciones = {
                "🔵 Azul Clásico",
                "🟢 Verde Profesional",
                "🟣 Morado Elegante",
                "🟠 Naranja Vibrante",
                "🎨 Personalizado"
        };

        String seleccion = (String) JOptionPane.showInputDialog(
                ventanaGrafica,
                "Seleccione un esquema de colores para la gráfica:",
                "🎨 Configurar Colores",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != null && grafica != null) {
            switch (seleccion) {
                case "🔵 Azul Clásico":
                    grafica.setColorLinea(new Color(52, 152, 219));
                    grafica.setColorPunto(new Color(231, 76, 60));
                    break;
                case "🟢 Verde Profesional":
                    grafica.setColorLinea(new Color(46, 204, 113));
                    grafica.setColorPunto(new Color(230, 126, 34));
                    break;
                case "🟣 Morado Elegante":
                    grafica.setColorLinea(new Color(155, 89, 182));
                    grafica.setColorPunto(new Color(241, 196, 15));
                    break;
                case "🟠 Naranja Vibrante":
                    grafica.setColorLinea(new Color(230, 126, 34));
                    grafica.setColorPunto(new Color(52, 152, 219));
                    break;
                case "🎨 Personalizado":
                    Color colorLinea = JColorChooser.showDialog(ventanaGrafica, "Color de Línea", grafica.getBackground());
                    Color colorPunto = JColorChooser.showDialog(ventanaGrafica, "Color de Puntos", grafica.getBackground());
                    if (colorLinea != null) grafica.setColorLinea(colorLinea);
                    if (colorPunto != null) grafica.setColorPunto(colorPunto);
                    break;
            }

            System.out.println("Colores de gráfica actualizados: " + seleccion);
        }
    }

    // NUEVO: Método principal que actualiza tabla Y gráfica al presionar Guardar
    private void actualizarTablaYGrafica() {
        System.out.println("=== INICIANDO ACTUALIZACIÓN DE TABLA Y GRÁFICA ===");

        if (recetaController == null || modelR == null || actualizandoModelo) {
            System.out.println("Controller o modelo no disponible");
            return;
        }

        try {
            actualizandoModelo = true;

            // Validar fechas
            if (DesdeFecha.getDate() == null || HastaFecha.getDate() == null) {
                JOptionPane.showMessageDialog(Dashboard,
                        "Por favor, seleccione ambas fechas (Desde y Hasta)",
                        "Fechas requeridas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar medicamento
            String medicamentoSeleccionado = (String) MedicamentoCombo.getSelectedItem();
            if (medicamentoSeleccionado == null || medicamentoSeleccionado.equals("Seleccione un medicamento...")) {
                JOptionPane.showMessageDialog(Dashboard,
                        "Por favor, seleccione un medicamento",
                        "Medicamento requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Filtrando recetas desde " + DesdeFecha.getDate() + " hasta " + HastaFecha.getDate());
            System.out.println("Medicamento seleccionado: " + medicamentoSeleccionado);

            // Obtener recetas filtradas por fecha
            recetasActuales = recetaController.RecetasPorFecha(DesdeFecha, HastaFecha);
            System.out.println("Recetas encontradas: " + (recetasActuales != null ? recetasActuales.size() : 0));

            // Procesar datos para la tabla
            MedicamentosResumenList list = new MedicamentosResumenList();
            list.insertarLista(recetasActuales);
            List<Receta> porNombre= recetaController.FiltradasPorNombre(medicamentoSeleccionado,recetasActuales);
            graficaPastel.actualizarDatos(porNombre, medicamentoSeleccionado);
            // Verificar si el medicamento existe en los datos
            boolean medicamentoExiste = false;
            for (MedicamentosResumen mr : list.obtenerTodos()) {
                if (mr.getNombreMedicamento().equals(medicamentoSeleccionado)) {
                    medicamentoExiste = true;
                    System.out.println("Medicamento encontrado en datos: " + mr.getCantidadTotal());
                    break;
                }
            }

            // Si no existe, crear entrada con cantidad 0
            if (!medicamentoExiste) {
                list.insertarConCantidad(medicamentoSeleccionado, 0);
                System.out.println("Medicamento agregado con cantidad 0");
            }

            // Actualizar modelo y tabla
            modelR.setList(list.obtenerElementosFiltrados(medicamentoSeleccionado));
            int[] cols = {TableModelDashboard.NOMBRE_MEDICAMENTO, TableModelDashboard.NUMERO_MEDICAMENTO};
            tabla1.setModel(new TableModelDashboard(cols, modelR.getList()));



            // IMPORTANTE: Actualizar gráfica inmediatamente
            SwingUtilities.invokeLater(() -> {
                System.out.println("Actualizando gráfica con medicamento: " + medicamentoSeleccionado);
                actualizarGrafica(medicamentoSeleccionado);
                try {
                    actualizarGraficaPastel(medicamentoSeleccionado);
                } catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Gráfica actualizada");
            });

            // Mensaje de confirmación
            JOptionPane.showMessageDialog(Dashboard,
                    "Datos guardados y gráfica actualizada correctamente",
                    "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            System.out.println("=== ACTUALIZACIÓN COMPLETADA ===");

        } catch (DataAccessException e) {
            System.err.println("Error de acceso a datos: " + e.getMessage());
            JOptionPane.showMessageDialog(Dashboard,
                    "Error al obtener datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            actualizandoModelo = false;
        }
    }

    // NUEVO: Método para actualizar solo la gráfica
    private void actualizarGrafica(String medicamento) {
        System.out.println("Actualizando gráfica para medicamento: " + medicamento);

        if (grafica == null) {
            System.out.println("Gráfica no inicializada");
            return;
        }

        if (recetasActuales != null && !recetasActuales.isEmpty() &&
                medicamento != null && !medicamento.equals("Seleccione un medicamento...")) {

            System.out.println("Datos disponibles: " + recetasActuales.size() + " recetas");
            grafica.actualizarDatos(recetasActuales, medicamento);
            System.out.println("Gráfica actualizada con datos");

        } else {
            System.out.println("Limpiando gráfica - sin datos válidos");
            grafica.limpiar();
        }
    }

    // RESTO DE MÉTODOS ORIGINALES...
    public JPanel getDashboard() {
        return Dashboard;
    }

    public void setController(RecetaController recetaController) {
        this.recetaController = recetaController;
        System.out.println("Controller configurado: " + (recetaController != null));
    }

    public void setModel(ModelReceta model) {
        this.model = model;
        model.addPropertyChangeListener(this);
        System.out.println("Model configurado");
    }

    public void setModelR(ModelMedicamentosResumen model) {
        this.modelR = model;
        this.modelR.addPropertyChangeListener(this);
        System.out.println("ModelR configurado");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (actualizandoModelo) return;

        System.out.println("PropertyChange detectado: " + evt.getPropertyName());

        switch (evt.getPropertyName()) {
            case ModelMedicamentosResumen.LIST:
                System.out.println("Modelo LIST actualizado externamente");

                SwingUtilities.invokeLater(() -> {
                    try {
                        int[] cols = {TableModelDashboard.NOMBRE_MEDICAMENTO, TableModelDashboard.NUMERO_MEDICAMENTO};
                        tabla1.setModel(new TableModelDashboard(cols, modelR.getList()));
                        System.out.println("Tabla actualizada desde propertyChange");

                        // Actualizar gráfica cuando se actualiza externamente
                        actualizarGraficaConSeleccionActual();

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
                                actualizarGrafica(nombreMed);
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

    private void actualizarGraficaConSeleccionActual() {
        String medicamentoSeleccionado = (String) MedicamentoCombo.getSelectedItem();
        if (medicamentoSeleccionado != null && !medicamentoSeleccionado.equals("Seleccione un medicamento...")) {
            actualizarGrafica(medicamentoSeleccionado);
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

        if (grafica != null) {
            grafica.limpiar();
            System.out.println("Gráfica limpiada");
        }
    }

    // Métodos de acceso público
    public Grafica getGrafica() {
        return grafica;
    }

    public void configurarGrafica(String titulo, Color colorLinea, Color colorPunto) {
        if (grafica != null) {
            grafica.setTitulo(titulo);
            grafica.setColorLinea(colorLinea);
            grafica.setColorPunto(colorPunto);
            System.out.println("Gráfica configurada: " + titulo);
        }
    }private void actualizarGraficaPastel(String medicamento) throws DataAccessException {
        if (graficaPastel == null) return;

        if (recetasActuales != null && !recetasActuales.isEmpty()
                && medicamento != null && !medicamento.equals("Seleccione un medicamento...")) {

            List<Receta> filtradas = recetaController.FiltradasPorNombre(medicamento, recetasActuales);
            graficaPastel.actualizarDatos(filtradas, medicamento);
        } else {

        }
    }
}