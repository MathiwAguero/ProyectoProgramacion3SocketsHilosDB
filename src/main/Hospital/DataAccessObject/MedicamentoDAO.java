package DataAccessObject;

import Exceptions.DataAccessException;
import Model.Medicamento;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO extends BaseDAO<Medicamento> {

    private static final String PATH = "resources/ArchivosXML/medicamentos.xml";
    private List<Medicamento> medicamentos = new ArrayList<>();

    public MedicamentoDAO() {
        try { cargarDesdeXML(); } catch (DataAccessException e) { System.out.println(e.getMessage()); }
    }

    @Override
    public void insertar(Medicamento m) throws DataAccessException {
        if (m == null || m.getCodigo() == null) throw new DataAccessException("Medicamento invalido");
        if (existeId(m.getCodigo())) throw new DataAccessException("Medicamento ya existe: " + m.getCodigo());
        medicamentos.add(m);
        guardarEnXML();
    }

    @Override
    public Medicamento obtenerPorId(String codigo) {
        if (codigo == null) return null;
        return medicamentos.stream().filter(x -> codigo.equals(x.getCodigo())).findFirst().orElse(null);
    }

    @Override
    public List<Medicamento> obtenerTodos() { return medicamentos; }

    @Override
    public void actualizar(Medicamento m) throws DataAccessException {
        if (m == null || m.getCodigo() == null) throw new DataAccessException("Medicamento invalido");
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equals(m.getCodigo())) {
                medicamentos.set(i, m);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Medicamento no encontrado: " + m.getCodigo());
    }

    @Override
    public void eliminar(String codigo) throws DataAccessException {
        boolean ok = medicamentos.removeIf(x -> x.getCodigo().equals(codigo));
        if (!ok) throw new DataAccessException("No existe medicamento: " + codigo);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String codigo) {
        return codigo != null && medicamentos.stream().anyMatch(x -> codigo.equals(x.getCodigo()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(medicamentos);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) { medicamentos = new ArrayList<>(); return; }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {
            medicamentos = (List<Medicamento>) dec.readObject();
        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}

