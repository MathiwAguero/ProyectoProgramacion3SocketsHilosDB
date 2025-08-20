package Controller;

import Model.Medicamento;

import javax.swing.text.View;

public class MedicamentoController {
    private View view;
    private Medicamento medicamento;
    public void MedicamentoController(View view, Medicamento medicamento ) {
        this.view = view;
        this.medicamento = medicamento;
    }

}
