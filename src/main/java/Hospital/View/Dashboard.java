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
    private JComboBox DesdeAnnio;
    private JComboBox DesdeMes;
    private JComboBox HastaAnnio;
    private JComboBox HastaMes;
    private JComboBox Medicamento;
    private JButton BusquedaUnica;
    private JButton BusquedaTodos;


    RecetaController recetaController;
    ModelReceta model;

    public Dashboard() {

        DesdeAnnio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        DesdeMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        HastaAnnio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        HastaMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        Medicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        BusquedaUnica.addActionListener(new ActionListener() {
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
