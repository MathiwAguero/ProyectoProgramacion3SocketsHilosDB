package Hospital.View;

import Hospital.Controller.PacienteController;
import Hospital.Controller.RecetaController;
import Hospital.Entidades.Medicamento;
import Hospital.Entidades.Paciente;
import Hospital.Entidades.Receta;
import Hospital.Model.ModelPaciente;
import Hospital.Model.ModelReceta;
import Hospital.TableModel.TableModelRecetas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Despacho implements PropertyChangeListener  {
    private JPanel despacho;
    private JTable table1;
    private JButton cambiarEstadoRecetaButton;
    private JButton pacientesButton;
    private JTextField textField1;

    ModelReceta model;
    RecetaController controller;
    private Paciente pacienteSeleccionado;

    public RecetaController getController() {
        return controller;
    }

    public void setController(RecetaController controller) {
        this.controller = controller;
    }

    public ModelReceta getModel() {
        return model;
    }

    public void setModel(ModelReceta model) {
        this.model = model;
    }

    public Despacho() {
        pacientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrescribirBuscarPacien panel = new PrescribirBuscarPacien();
                ModelPaciente model = new ModelPaciente();
                PacienteController controller = new PacienteController(panel, model);

                JDialog d = new JDialog(SwingUtilities.getWindowAncestor(despacho),
                        "Buscar paciente", Dialog.ModalityType.APPLICATION_MODAL);
                d.setContentPane(panel.getPanel());
                d.pack();
                d.setLocationRelativeTo(despacho);
                d.setVisible(true);

                Paciente seleccionado = panel.getPacienteSeleccionado();
                if(seleccionado!=null){
                    pacienteSeleccionado = seleccionado;
                    textField1.setText(seleccionado.getNombre());
                }

            }
        });
        cambiarEstadoRecetaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getSelectedReceta() == null) {
                    JOptionPane.showMessageDialog(despacho, "Debe seleccionar una receta antes");
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
                int[] cols = {TableModelRecetas.ID, TableModelRecetas.MEDICO, TableModelRecetas.PACIENTE, TableModelRecetas.ESTADO,
                        TableModelRecetas.FECHARETIRO, TableModelRecetas.FECHACONFECC, TableModelRecetas.DETALLES};
                table1.setModel(new TableModelRecetas(cols, model.getList()));
                break;
            }
            case ModelReceta.CURRENT: {
                Receta current = model.getCurrent();
                break;
            }
        }
        this.despacho.revalidate();
    }
    public Receta getSelectedReceta() {
        if (table1.getSelectedRow() < 0) return null;
        int modelRow = table1.convertRowIndexToModel(table1.getSelectedRow());
        return model.getList().get(modelRow);
    }

}
