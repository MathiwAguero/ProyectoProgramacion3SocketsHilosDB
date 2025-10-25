package hospital.Presentation.Despacho;
import hospital.Presentation.Pacientes.ModelPaciente;
import hospital.Presentation.Pacientes.PacienteController;
import hospital.Presentation.Recetas.RecetaController;
import hospital.Entities.Entities.*;
import hospital.Logic.Listas.*;
import hospital.Presentation.Recetas.ModelReceta;
import hospital.Presentation.TableModel.TableModelRecetas;
import hospital.Presentation.Prescripcion.Filtros.PrescribirBuscarPacien;
import hospital.Presentation.Pacientes.PacienteController;
import hospital.Presentation.Pacientes.ModelPaciente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import hospital.Presentation.Prescripcion.Filtros.PrescribirBuscarPacien;
import hospital.Entities.Entities.*;
import hospital.Logic.Listas.*;
import hospital.Presentation.Pacientes.PacienteController;
import hospital.Presentation.Pacientes.ModelPaciente;
import hospital.Presentation.Pacientes.PacienteController;

public class Despacho implements PropertyChangeListener  {
    private JPanel despacho;
    private JTable table1;
    private JButton cambiarEstadoRecetaButton;
    private JButton pacientesButton;
    private JTextField textField1;

    ModelReceta model;
    RecetaController controller;
    private Paciente pacienteSeleccionado;

    public RecetaController getController() { return controller; }
    public void setController(RecetaController controller) { this.controller = controller; }

    public ModelReceta getModel() { return model; }
    public void setModel(ModelReceta model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public Despacho() {
        pacientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrescribirBuscarPacien panel = new PrescribirBuscarPacien();
                ModelPaciente model = new ModelPaciente();
                new PacienteController(panel, model);

                JDialog d = new JDialog(SwingUtilities.getWindowAncestor(despacho),
                        "Buscar paciente", Dialog.ModalityType.APPLICATION_MODAL);
                d.setContentPane(panel.getPanel());
                d.pack();
                d.setLocationRelativeTo(despacho);
                d.setVisible(true);

                Paciente seleccionado = panel.getPacienteSeleccionado();
                if (seleccionado != null) {
                    pacienteSeleccionado = seleccionado;
                    textField1.setText(seleccionado.getNombre());
                    cargarRecetasPaciente(pacienteSeleccionado);
                }
            }
        });

        cambiarEstadoRecetaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receta sel = getSelectedReceta();
                if (sel == null) {
                    JOptionPane.showMessageDialog(despacho, "Debe seleccionar una receta antes");
                    return;
                }

                String id = sel.getId();
                EstadoReceta nuevo = nextEstado(sel.getEstado());

                try {
                    controller.actualizarEstado(id, nuevo);

                    if (pacienteSeleccionado != null) {
                        cargarRecetasPaciente(pacienteSeleccionado);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(despacho, "Error al actualizar: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JPanel getDespacho() {
        return despacho;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
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
        this.despacho.revalidate();
        this.despacho.repaint();
    }

    public Receta getSelectedReceta() {
        int viewRow = table1.getSelectedRow();
        if (viewRow < 0) return null;
        int modelRow = table1.convertRowIndexToModel(viewRow);
        return model.getList().get(modelRow);
    }

    private EstadoReceta nextEstado(EstadoReceta e) {
        if (e == null) return EstadoReceta.CONFECCIONADA;
        if (e == EstadoReceta.CONFECCIONADA) return EstadoReceta.LISTA;
        if (e == EstadoReceta.LISTA)         return EstadoReceta.ENTREGADA;
        if (e == EstadoReceta.ENTREGADA)         return EstadoReceta.PROCESO;
        if (e == EstadoReceta.PROCESO)         return EstadoReceta.CONFECCIONADA;
        return EstadoReceta.CONFECCIONADA;
    }

    private void cargarRecetasPaciente(Paciente p) {
        try {
            List<Receta> todas = Factory.get().receta().obtenerTodos();
            List<Receta> filtradas = new ArrayList<>();
            for (Receta r : todas) {
                if (r.getPaciente() != null && r.getPaciente().getId() != null
                        && r.getPaciente().getId().equalsIgnoreCase(p.getId())) {
                    filtradas.add(r);
                }
            }
            model.setList(filtradas);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(despacho, "Error cargando recetas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
