package ManejoListas;

import Exceptions.DataAccessException;
import Entidades.UsuarioBase;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListUsers extends Base<UsuarioBase> {

    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/ArchivosXML/user.XML";
    private List<UsuarioBase> usuarios = new ArrayList<>();

    public ListUsers() {
        try { cargarDesdeXML(); } catch (DataAccessException e) { System.out.println(e.getMessage()); }
    }

    @Override
    public void insertar(UsuarioBase u) throws DataAccessException {
        if (u == null || u.getId() == null) throw new DataAccessException("Usuario invalido");
        if (existeId(u.getId())) throw new DataAccessException("Usuario ya existe: " + u.getId());
        usuarios.add(u);
        guardarEnXML();
    }

    @Override
    public UsuarioBase obtenerPorId(String id) {
        if (id == null) return null;
        return usuarios.stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<UsuarioBase> obtenerTodos() { return usuarios; }

    @Override
    public void actualizar(UsuarioBase u) throws DataAccessException {
        if (u == null || u.getId() == null) throw new DataAccessException("Usuario invalido");
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(u.getId())) { usuarios.set(i, u); guardarEnXML(); return; }
        }
        throw new DataAccessException("Usuario no encontrado: " + u.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = usuarios.removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe usuario: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && usuarios.stream().anyMatch(x -> id.equals(x.getId()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(usuarios);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) { usuarios = new ArrayList<>(); return; }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {
            usuarios = (List<UsuarioBase>) dec.readObject();
        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}
