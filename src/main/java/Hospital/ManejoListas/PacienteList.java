package Hospital.ManejoListas;

import Hospital.Entidades.Paciente;
import  Hospital.Exceptions.DataAccessException;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteList extends Base<Paciente> {

    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/ArchivosXML/pacientes.XML";
    private List<Paciente> pacientes = new ArrayList<>();

    public PacienteList() {
        try {
            cargarDesdeXML();
        } catch (DataAccessException e) {
            System.out.println("No se pudo cargar pacientes: " + e.getMessage());
        }
    }

    @Override
    public void insertar(Paciente paciente) throws DataAccessException {
        if (paciente == null || paciente.getId() == null) {
            throw new DataAccessException("Paciente invalido");
        }
        if (existeId(paciente.getId())) {
            throw new DataAccessException("El paciente con id " + paciente.getId() + " ya existe");
        }
        pacientes.add(paciente);
        guardarEnXML();
    }

    @Override
    public Paciente obtenerPorId(String id) throws DataAccessException {
        if (id == null) return null;
        return pacientes.stream().filter(p -> id.equals(p.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Paciente> obtenerTodos() {
        return pacientes;
    }

    @Override
    public void actualizar(Paciente paciente) throws DataAccessException {
        if (paciente == null || paciente.getId() == null) {
            throw new DataAccessException("Paciente invalido");
        }
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId().equals(paciente.getId())) {
                pacientes.set(i, paciente);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Paciente no encontrado: " + paciente.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = pacientes.removeIf(p -> p.getId().equals(id));
        if (!ok) {
            throw new DataAccessException("No existe paciente: " + id);
        }
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && pacientes.stream().anyMatch(p -> id.equals(p.getId()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(pacientes);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) {
            pacientes = new ArrayList<>();
            return;
        }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {
            pacientes = (List<Paciente>) dec.readObject();
        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}

