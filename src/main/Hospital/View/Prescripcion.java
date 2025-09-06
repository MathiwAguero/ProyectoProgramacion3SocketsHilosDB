package View;

import Controller.DetailsController;
import Model.ModelDetails;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Prescripcion implements PropertyChangeListener  {
    private JPanel Prescrip;
    private JButton guardarButton;
    private JButton cancelarButton;
    private JSpinner spinner2;
    private JSpinner spinner1;
    private JTextArea textArea1;

    DetailsController detailsController;
    ModelDetails  modelDetails;

    public JPanel getPrescrip() {
        return  Prescrip;
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
