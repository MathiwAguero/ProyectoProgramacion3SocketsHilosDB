package DataAccessObject;

import Exceptions.DataAccessException;
import Model.Farmaceuta;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaDAO extends BaseDAO<Farmaceuta> {

    private static final String PATH = "resources/ArchivosXML/farmaceuticos.xml";
    private List<Farmaceuta> farmaceutas = new ArrayList<>();

    public FarmaceutaDAO() {
        try { cargarDesdeXML(); } catch (DataAccessException e) { System.out.println(e.getMessage()); }
    }

    @Override
    public void insertar(Farmaceuta f) throws DataAccessException {
        if (f == null || f.getId() == null) throw new DataAccessException("Farmaceutico invalido");
        if (existeId(f.getId())) throw new DataAccessException("Farmecutico ya existe: " + f.getId());
        farmaceutas.add(f);
        guardarEnXML();
    }

    @Override
    public Farmaceuta obtenerPorId(String id) {
        if (id == null) return null;
        return farmaceutas.stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Farmaceuta> obtenerTodos() { return farmaceutas; }

    @Override
    public void actualizar(Farmaceuta f) throws DataAccessException {
        if (f == null || f.getId() == null) throw new DataAccessException("Farmecutico invalido");
        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getId().equals(f.getId())) { farmaceutas.set(i, f); guardarEnXML(); return; }
        }
        throw new DataAccessException("Farmecutico no encontrado: " + f.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = farmaceutas.removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe farmecutico: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && farmaceutas.stream().anyMatch(x -> id.equals(x.getId()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(farmaceutas);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) { farmaceutas = new ArrayList<>(); return; }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {
            farmaceutas = (List<Farmaceuta>) dec.readObject();
        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}
