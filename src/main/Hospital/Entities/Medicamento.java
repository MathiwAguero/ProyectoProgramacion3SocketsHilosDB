package Entities;

public class Medicamento {
    private String nombre;
    private int codigo;
    private String presentation;

    Medicamento(String nombre, int codigo, String presentation) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.presentation = presentation;
    }

    Medicamento() {
        this.nombre = "";
        this.codigo = 0;
        this.presentation = "";
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getPresentation() {
        return presentation;
    }
    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

}
