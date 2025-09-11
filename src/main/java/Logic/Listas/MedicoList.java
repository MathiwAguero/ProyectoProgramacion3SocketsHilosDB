package Logic.Listas;

import Logic.Entities.Medico;
import  Logic.Exceptions.DataAccessException;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoList extends Base<Medico> {
    private List<Medico> medicos = new ArrayList<>();
    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/ArchivosXML/medicos.XML";

    public MedicoList() {
        try {
            cargarDesdeXML();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage()); }
    }

    @Override
    public void insertar(Medico m) throws DataAccessException {
        if (m == null || m.getId() == null) throw new DataAccessException("Medico invalido");
        if (existeId(m.getId())) throw new DataAccessException("Medico ya existe: " + m.getId());
        medicos.add(m);
        guardarEnXML();
    }

    @Override
    public Medico obtenerPorId(String id) {
        if(id == null) return null;
        return medicos.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Medico> obtenerTodos() { return medicos; }

    @Override
    public void actualizar(Medico m) throws DataAccessException {
        if(m == null || m.getId() == null) throw new DataAccessException("Medico inexistente");
        for (int i=0; i<medicos.size(); i++) {
            if (medicos.get(i).getId().equals(m.getId())) {
                medicos.set(i, m);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Medico no encontrado: " + m.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = medicos.removeIf(x -> x.getId().equals(id));
        if (!ok) {
            throw new DataAccessException("No existe medico: " + id);
        }
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && medicos.stream().anyMatch(x -> x.getId().equals(id));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(medicos);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    /*
      Esto no lo hemos visto pero es interesante, basicamente se sabe que
      todo en java hereda de la clase objetc, digamos si yo hago Object obj = dec.readObject()
      eso puede ser un String, int, double, etc, por eso ponemos unchecked porque debemos verificar que es.
    */

    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) {
            //si existe vacío o no existe, arranca sin datos
            medicos = new ArrayList<>();
            return;
        }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {

            /*Eso es porque el Decoder no sabe que tipo de datos hay
            el funciona como "Yo te devuelvo algo, vos sabras que hacer con eso"
            por eso lo usamos, porque el decoder leera el archivo sin saber que son medicos,
            admins, pacientes, recetas, etc.
            */

            medicos = (List<Medico>) dec.readObject();
            /* aca hacemos el casteo porque ese object puede ser cualquier
              cosa y ocupamos que lea solo la lista de medicos */

        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}

