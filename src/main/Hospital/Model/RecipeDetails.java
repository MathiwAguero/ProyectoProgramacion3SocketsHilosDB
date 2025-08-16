package Model;

public class RecipeDetails {
    private int cant;
    private String indicaciones;
    private String duracion;

    RecipeDetails(int cant, String indicaciones, String duracion) {
        this.cant = cant;
        this.indicaciones = indicaciones;
        this.duracion = duracion;
    }

    RecipeDetails() {
        this.cant = 0;
        this.indicaciones = "";
        this.duracion = "";
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}

