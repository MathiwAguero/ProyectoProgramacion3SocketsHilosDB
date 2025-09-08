package Hospital.ManejoListas;

import  Hospital.Entidades.Receta;
import  Hospital.Entidades.RecipeDetails;
import  Hospital.Exceptions.DataAccessException;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DetailsList extends Base<RecipeDetails> {

    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/ArchivosXML/details.XML";
    private List<RecipeDetails> details = new ArrayList<>();

    public DetailsList() {
        try {
            cargarDesdeXML();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insertar(RecipeDetails m) throws DataAccessException {
        if (m == null || m.getCodigoMedicamento() == null) throw new DataAccessException("Detalle invalido");
        if (existeId(m.getCodigoMedicamento())) throw new DataAccessException("Ya existe: " + m.getCodigoMedicamento());
        details.add(m);
        guardarEnXML();
    }

    @Override
    public RecipeDetails obtenerPorId(String codigo) {
        if (codigo == null) return null;
        return details.stream().filter(x -> codigo.equals(x.getCodigoMedicamento())).findFirst().orElse(null);
    }

    @Override
    public List<RecipeDetails> obtenerTodos() { return details; }

    @Override
    public void actualizar(RecipeDetails m) throws DataAccessException {
        if (m == null || m.getCodigoMedicamento() == null) throw new DataAccessException("Detalle de receta invalido");
        for (int i = 0; i < details.size(); i++) {
            if (details.get(i).getCodigoMedicamento().equals(m.getCodigoMedicamento())) {
                details.set(i, m);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Detalle de la receta no encontrado: " + m.getCodigoMedicamento());
    }

    @Override
    public void eliminar(String codigo) throws DataAccessException {
        boolean ok = details.removeIf(x -> x.getCodigoMedicamento().equals(codigo));
        if (!ok) throw new DataAccessException("No existe detalle de receta a codigo de: " + codigo);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String codigo) {
        return codigo != null && details.stream().anyMatch(x -> codigo.equals(x.getCodigoMedicamento()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(details);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) { details = new ArrayList<>(); return; }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {
            details = (List<RecipeDetails>) dec.readObject();
        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}

