package DataAccessObject;

import Exceptions.DataAccessException;
import Model.Receta;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO extends BaseDAO<Receta> {

    private static final String PATH = "resources/ArchivosXML/recetas.xml";
    private List<Receta> recetas = new ArrayList<>();

    public RecetaDAO() {
        try { cargarDesdeXML(); } catch (DataAccessException e) { System.out.println(e.getMessage()); }
    }

    @Override
    public void insertar(Receta r) throws DataAccessException {
        if (r == null || r.getId() == null) throw new DataAccessException("Receta invalido");
        if (existeId(r.getId())) throw new DataAccessException("Receta ya existe: " + r.getId());
        recetas.add(r);
        guardarEnXML();
    }

    @Override
    public Receta obtenerPorId(String id) {
        if (id == null) return null;
        return recetas.stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Receta> obtenerTodos() { return recetas; }

    @Override
    public void actualizar(Receta r) throws DataAccessException {
        if (r == null || r.getId() == null) throw new DataAccessException("Receta invalido");
        for (int i = 0; i < recetas.size(); i++) {
            if (recetas.get(i).getId().equals(r.getId())) {
                recetas.set(i, r);
                guardarEnXML();
                return; }
        }
        throw new DataAccessException("Receta no encontrada: " + r.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = recetas.removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe receta: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && recetas.stream().anyMatch(x -> id.equals(x.getId()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(recetas);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) { recetas = new ArrayList<>(); return; }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {
            recetas = (List<Receta>) dec.readObject();
        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}
