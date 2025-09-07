package View;

import Controller.DetailsController;
import Entidades.RecipeDetails;
import Model.ModelDetails;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PrescribirIndicaciones implements PropertyChangeListener  {
    private JPanel Prescrip;
    private JButton guardarButton;
    private JButton cancelarButton;
    private JSpinner spinner2;
    private JSpinner spinner1;
    private JTextArea textArea1;

    DetailsController detailsController;
    ModelDetails modelDetails;
    private RecipeDetails detalle;
    private String codigoMedicamento;

    public RecipeDetails getDetalle() {
        return detalle;
    }

    public PrescribirIndicaciones() {
        spinner1.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        spinner2.setModel(new SpinnerNumberModel(1, 1, 365, 1));
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cantidad = (Integer) spinner1.getValue();
                int dias = (Integer) spinner2.getValue();
                String indic = textArea1.getText() == null ? "" : textArea1.getText().trim();
                detalle = new RecipeDetails();
                detalle.setCantidad(cantidad);
                detalle.setDuracionTratamiento(dias);
                detalle.setCodigoMedicamento(codigoMedicamento);
                detalle.setIndicaciones(indic);

                Window w = SwingUtilities.getWindowAncestor(Prescrip);
                if (w instanceof JDialog) ((JDialog) w).dispose();

            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor(Prescrip);
                if (w instanceof JDialog) ((JDialog) w).dispose();
            }
        });
    }

    public JPanel getPrescrip() {
        return  Prescrip;
    }
    public void setCodigoMedicamento(String codigo) {
        this.codigoMedicamento = codigo;
    }

    public void setController(DetailsController detailsController) {
        this.detailsController = detailsController;
    }
    public void setModel(ModelDetails modelDetails) {
        this.modelDetails = modelDetails;
        modelDetails.addPropertyChangeListener(this);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
