package Hospital.View;

import Hospital.Controller.RecetaController;
import Hospital.Model.ModelReceta;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Dashboard implements PropertyChangeListener  {
    private JPanel Dashboard;
    private JPanel DatosPanel;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton button1;

    RecetaController recetaController;
    ModelReceta model;

    public Dashboard() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

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
