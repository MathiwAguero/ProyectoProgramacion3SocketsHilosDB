package hospital.Presentation.Prescripcion;

import Presentation.Prescripcion.Filtros.PrescribirBuscarMedica;
import Presentation.Prescripcion.Filtros.PrescribirBuscarPacien;
import Presentation.Medicamentos.MedicamentoController;
import Presentation.Pacientes.PacienteController;
import Presentation.Detalles.ModelDetails;
import Presentation.Medicamentos.ModelMedicamentos;
import Presentation.Pacientes.ModelPaciente;
import Presentation.Recetas.ModelReceta;
import Presentation.TableModel.TableModelDetails;
import Logic.Entities.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class PrescribirMed implements PropertyChangeListener {

    private JButton buscarPacienteButton;
    private JButton agregarMedicamentoButton;
    private JTextField textField1;
    private JTable table1;
    private JButton guadarButton;
    private JButton limpiarButton;
    private JButton descartarMedicamentoButton;
    private JButton detallesButton;
    private JPanel prescribir;
    private JDateChooser JDateChooser1;

    private Paciente pacienteSeleccionado;
    private PrescribirController controller;
    private ModelDetails model;
    private Medico medicoActual;

    public PrescribirMed() {
        buscarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrescribirBuscarPacien panel = new PrescribirBuscarPacien();
                ModelPaciente mp = new ModelPaciente();
                PacienteController filtro = new PacienteController(panel, mp);

                JDialog d = new JDialog(SwingUtilities.getWindowAncestor(prescribir),
                        "Buscar paciente", Dialog.ModalityType.APPLICATION_MODAL);
                d.setContentPane(panel.getPanel());
                d.pack();
                d.setLocationRelativeTo(prescribir);
                d.setVisible(true);

                Paciente p = panel.getPacienteSeleccionado();
                if (p != null) {
                    pacienteSeleccionado = p;
                    textField1.setText(p.getNombre());
                }
            }
        });

        agregarMedicamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrescribirBuscarMedica panel = new PrescribirBuscarMedica();
                ModelMedicamentos mm = new ModelMedicamentos();
                MedicamentoController cm = new MedicamentoController(panel, mm);

                JDialog d = new JDialog(SwingUtilities.getWindowAncestor(prescribir),
                        "Buscar medicamento", Dialog.ModalityType.APPLICATION_MODAL);
                d.setContentPane(panel.getPanel());
                d.pack();
                d.setLocationRelativeTo(prescribir);
                d.setVisible(true);

                RecipeDetails det = panel.getDetalleSeleccionado();
                if (det != null) {
                    java.util.List<RecipeDetails> lista = (model.getList() != null)
                            ? new ArrayList<>(model.getList())
                            : new ArrayList<>();
                    lista.add(det);
                    model.setList(lista);
                    refrescarTabla();
                }
            }
        });

        guadarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validarParaGuardar()) return;
                String id = JOptionPane.showInputDialog(prescribir, "Ingrese ID de la receta:", JOptionPane.QUESTION_MESSAGE);
                try {

                    Receta r = new Receta();
                    r.setId(id);
                    r.setPaciente(pacienteSeleccionado);
                    r.setMedico(medicoActual);
                    r.setFechaConfeccion(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
                    r.setDetalles(new ArrayList<>(model.getList()));
                    r.setFechaRetiro(new java.text.SimpleDateFormat("dd/MM/yyyy").format(JDateChooser1.getDate()));
                    r.setFechaRecoleccion(JDateChooser1);
                    r.setEstado(EstadoReceta.CONFECCIONADA);

                    Receta guardada = controller.create(r);

                    JOptionPane.showMessageDialog(prescribir, "Receta guardada correctamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(prescribir, "Error al guardar: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        descartarMedicamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecipeDetails sel = getDetalleSeleccionadoTabla();
                if (sel == null) {
                    JOptionPane.showMessageDialog(prescribir, "Seleccione un medicamento en la tabla para descartar.",
                            "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                java.util.List<RecipeDetails> l = model.getList();
                l.remove(sel);
                model.setList(l);
                refrescarTabla();
            }
        });
        detallesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecipeDetails det = getDetalleSeleccionadoTabla();
                if (det == null) {
                    JOptionPane.showMessageDialog(prescribir, "Seleccione un medicamento para ver detalles.");
                    return;
                }
                String info = "Código: " + det.getCodigoMedicamento() + "\n" +
                        "Cantidad: " + det.getCantidad() + "\n" +
                        "Duración (días): " + det.getDuracionTratamiento() + "\n" +
                        "Indicaciones:\n" + det.getIndicaciones();
                JOptionPane.showMessageDialog(prescribir, info, "Detalles del medicamento",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void refrescarTabla() {
        int[] cols = {
                TableModelDetails.MEDICAMENTO,
                TableModelDetails.INDICACIONES,
                TableModelDetails.CANTIDAD,
                TableModelDetails.DURACION
        };
        table1.setModel(new TableModelDetails(cols, model.getList()));
        table1.revalidate();
        table1.repaint();
    }

    private void limpiarFormulario() {
        textField1.setText("");
        pacienteSeleccionado = null;
        if (JDateChooser1 != null) JDateChooser1.setDate(null);
        model.setList(new ArrayList<>());
        refrescarTabla();
    }

    private boolean validarParaGuardar() {
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(prescribir, "Seleccione un paciente.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (JDateChooser1 == null || JDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(prescribir, "Seleccione la fecha de la receta.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        java.util.List<RecipeDetails> l = model.getList();
        if (l == null || l.isEmpty()) {
            JOptionPane.showMessageDialog(prescribir, "Agregue al menos un medicamento.", "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private RecipeDetails getDetalleSeleccionadoTabla() {
        int fila = table1.getSelectedRow();
        if (fila < 0) return null;
        int modelRow = table1.convertRowIndexToModel(fila);
        java.util.List<RecipeDetails> l = model.getList();
        if (l == null || modelRow < 0 || modelRow >= l.size()) return null;
        return l.get(modelRow);
    }

    public void setModel(ModelDetails model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(PrescribirController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelDetails.LIST: {
                refrescarTabla();
                break;
            }
            case ModelReceta.CURRENT: {
                break;
            }
        }
        this.prescribir.revalidate();
    }

    public JPanel getPrescribirMed() {
        return prescribir;
    }

    private void createUIComponents() {
        JDateChooser1 = new JDateChooser();
        JDateChooser1.setDateFormatString("dd/MM/yyyy");
    }

    public void setMedicoActual(Medico medico) {
        this.medicoActual = medico;
    }


}
