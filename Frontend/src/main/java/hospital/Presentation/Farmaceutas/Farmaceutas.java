package hospital.Presentation.Farmaceutas;

import hospital.Application;
import hospital.Entities.Entities.*;
import hospital.Logic.Listas.Factory;
import hospital.Presentation.TableModel.TableModelFarmaceutas;
import hospital.Presentation.TableModel.TableModelMedicos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class Farmaceutas implements PropertyChangeListener {

    FarmaceutaController controller;
    ModelFarmaceuta model;

    private JPanel Farmaceutas;
    private JTextField textField1;
    private JTextField textField2;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JTextField textField4;
    private JButton reportebutton4;
    private JButton buscarButton;
    private JTable table1;

    public Farmaceutas() {

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LimpiarCampos();
            }
        });
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validate()) {
                    Farmaceuta f = take();
                    try {
                        controller.create(f);
                        JOptionPane.showMessageDialog(Farmaceutas, "Farmaceuta guardada con exito");
                    } catch(Exception t) {
                        JOptionPane.showMessageDialog(Farmaceutas, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField1.getText().trim();
                if (!id.isEmpty()) {
                    try {
                        Farmaceuta borror=new Farmaceuta();
                        borror.setId(id);
                        controller.delete(borror);
                        JOptionPane.showMessageDialog(Farmaceutas,  "FARMACEUTA ELIMINADO", "", JOptionPane.INFORMATION_MESSAGE );
                        LimpiarCampos();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Farmaceutas, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Farmaceutas, "Ingrese ID del farmaceuta a eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textField4.getText().trim();
                try {
                    controller.search(nombre);
                } catch (Exception x) {
                    JOptionPane.showMessageDialog(Farmaceutas, "No se encuentra nadie a ese nombre");
                }
            }
        });
        reportebutton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String busqueda = textField4.getText().trim().toLowerCase();
                List<Farmaceuta> farmas = Factory.get().farmaceuta().obtenerTodos();
                if(busqueda.isEmpty()) {
                    JOptionPane.showMessageDialog(Farmaceutas, "Debe buscar un farmaceuta\n");
                }
                for(Farmaceuta f : farmas) {
                    if(f.getNombre() != null && f.getNombre().toLowerCase().contains(busqueda)) {
                        String reporte = "Reporte del farmaceuta encontrado:\n\n" +
                                "ID: " + f.getId() + "\n" +
                                "Nombre: " + f.getNombre() + "\n";
                        JOptionPane.showMessageDialog(Farmaceutas, reporte);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(Farmaceutas, "No se encontraron a ese nombre");
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cargarFarmaceutaSeleccionado();
                }
            }
        });



    }
    private void cargarFarmaceutaSeleccionado() {
        int row = table1.getSelectedRow();
        if (row < 0) return;

        int modelRow = table1.convertRowIndexToModel(row);
        Farmaceuta farmaceuta = model.getList().get(modelRow);

        textField1.setText(farmaceuta.getId() != null ? farmaceuta.getId() : "");
        textField2.setText(farmaceuta.getNombre() != null ? farmaceuta.getNombre() : "");

        model.setCurrent(farmaceuta);
        System.out.println("✓ Farmaceuta cargado: " + farmaceuta.getNombre());
    }
    public void LimpiarCampos() {
        textField1.setText("");
        textField2.setText("");
    }

    public void setModel(ModelFarmaceuta model) {

        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(FarmaceutaController controller) {
        this.controller = controller;
    }

    public JPanel getFarmaceutas() {
        return Farmaceutas;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModelFarmaceuta.LIST: {
                int[] cols = {TableModelMedicos.ID, TableModelMedicos.NOMBRE};
                table1.setModel(new TableModelFarmaceutas(cols, model.getList()));
                break;
            }
            case ModelFarmaceuta.CURRENT: {
                Farmaceuta current = model.getCurrent();
                if (current != null) {
                    textField1.setText(current.getId() != null ? current.getId() : "");
                    textField2.setText(current.getNombre() != null ? current.getNombre() : "");
                }
                textField1.setBackground(null); textField1.setToolTipText(null);
                textField2.setBackground(null); textField2.setToolTipText(null);
                break;
            }
        }
        this.Farmaceutas.revalidate();
        this.Farmaceutas.repaint();
    }

    public Farmaceuta take() {
        Farmaceuta f = new Farmaceuta();
        f.setId(textField1.getText().trim());
        f.setNombre(textField2.getText().trim());

        String clave = JOptionPane.showInputDialog(Farmaceutas, "Ingrese la clave del farmaceuta:");
        if (clave != null) f.setClave(clave.trim());
        return f;
    }

    private boolean validate() {
        boolean valid = true;
        if (textField1.getText().trim().isEmpty()) {
            valid = false;
            textField1.setBackground(Application.BACKGROUND_ERROR);
            textField1.setToolTipText("ID requerido");
        } else {
            textField1.setBackground(null);
            textField1.setToolTipText(null);
        }
        if (textField2.getText().trim().isEmpty()) {
            valid = false;
            textField2.setBackground(Application.BACKGROUND_ERROR);
            textField2.setToolTipText("Nombre requerido");
        } else {
            textField2.setBackground(null);
            textField2.setToolTipText(null);
        }
        return valid;
    }
}
