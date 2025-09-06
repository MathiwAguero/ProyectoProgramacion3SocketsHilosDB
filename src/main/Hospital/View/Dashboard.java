package View;

import Controller.RecetaController;
import Model.ModelReceta;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Dashboard implements PropertyChangeListener  {
    private JPanel Dashboard;
    private JComboBox comboBox1;
    private JButton button1;
    private JButton button2;
    private JTable table1;

    RecetaController recetaController;
    ModelReceta model;

    public JPanel getDashboard() {
        return Dashboard;
    }


    public void setController(RecetaController recetaController) {
        this.recetaController = recetaController;
    }

    public void setModel(ModelReceta model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
