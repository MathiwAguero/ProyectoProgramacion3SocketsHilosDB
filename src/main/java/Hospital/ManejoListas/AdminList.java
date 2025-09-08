package Hospital.ManejoListas;

import  Hospital.Exceptions.DataAccessException;
import  Hospital.Entidades.Admin;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminList extends Base<Admin> {

    private static final String PATH = System.getProperty("user.dir") + "/src/main/resources/ArchivosXML/admins.XML";
    private List<Admin> administradores = new ArrayList<>();

    public AdminList() {
        try { cargarDesdeXML(); }
        catch (DataAccessException e) { System.out.println("No se pudo cargar administradores: " + e.getMessage()); }
    }

    @Override
    public void insertar(Admin admin) throws DataAccessException {
        if (admin == null || admin.getId() == null) {
            throw new DataAccessException("Administrador invalido");
        }
        if (existeId(admin.getId())) {
            throw new DataAccessException("Administrador ya existe: " + admin.getId());
        }
        administradores.add(admin);
        guardarEnXML();
    }

    @Override
    public Admin obtenerPorId(String id) {
        if (id == null) return null;
        return administradores.stream()
                .filter(a -> id.equals(a.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Admin> obtenerTodos() {
        return administradores;
    }

    @Override
    public void actualizar(Admin admin) throws DataAccessException {
        if (admin == null || admin.getId() == null) {
            throw new DataAccessException("Administrador invalido");
        }
        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getId().equals(admin.getId())) {
                administradores.set(i, admin);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Administrador no encontrado: " + admin.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = administradores.removeIf(a -> a.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe administrador: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && administradores.stream().anyMatch(a -> id.equals(a.getId()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(PATH));
             XMLEncoder enc = new XMLEncoder(out)) {
            enc.writeObject(administradores);
        } catch (Exception e) {
            throw new DataAccessException("No se pudo guardar " + PATH + ": " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cargarDesdeXML() throws DataAccessException {
        File f = new File(PATH);
        if (!f.exists() || f.length() == 0) {
            administradores = new ArrayList<>();
            return;
        }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
             XMLDecoder dec = new XMLDecoder(in)) {
            administradores = (List<Admin>) dec.readObject();
        } catch (Exception e) {
            throw new DataAccessException("No se pudo cargar " + PATH + ": " + e.getMessage());
        }
    }
}


