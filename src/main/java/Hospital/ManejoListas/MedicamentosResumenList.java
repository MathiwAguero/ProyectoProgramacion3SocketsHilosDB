package Hospital.ManejoListas;

import Hospital.Entidades.Medicamento;
import Hospital.Entidades.MedicamentosResumen;
import Hospital.Exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.List;

public class MedicamentosResumenList extends Base<MedicamentosResumen> {
    private List<MedicamentosResumen> medicamentos=new ArrayList<>();

    @Override
    public void insertar(MedicamentosResumen m) throws DataAccessException {
        if (m == null || m.getNombreMedicamento()    == null) throw new DataAccessException("Medicamento invalido");
        if (existeId(m.getNombreMedicamento())) throw new DataAccessException("Medicamento ya existe: " + m.getNombreMedicamento());
        medicamentos.add(m);
        guardarEnXML();
    }

    @Override
    public MedicamentosResumen obtenerPorId(String id) throws DataAccessException {
        if (id == null) return null;
        return medicamentos.stream().filter(x -> id.equals(x.getNombreMedicamento())).findFirst().orElse(null);
    }

    @Override
    public List<MedicamentosResumen> obtenerTodos() throws DataAccessException {
        return medicamentos;
    }

    @Override
    public void actualizar(MedicamentosResumen objeto) throws DataAccessException {

    }

    @Override
    public void eliminar(String id) throws DataAccessException {

    }

    @Override
    public boolean existeId(String id) throws DataAccessException {
        return id != null && medicamentos.stream().anyMatch(x -> id.equals(x.getNombreMedicamento()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {

    }

    @Override
    protected void cargarDesdeXML() throws DataAccessException {

    }
    public void insertarConCantidad(String id, int cantidad) throws DataAccessException {
        if(existeId(id)){
            MedicamentosResumen med= obtenerPorId(id);
            med.setCantidadTotal(med.getCantidadTotal()+cantidad);
            insertar(med);
        }else{
            MedicamentosResumen med= new MedicamentosResumen();
            med.setCantidadTotal(med.getCantidadTotal()+cantidad);
            med.setNombreMedicamento(id);
            insertar(med);
        }
    }
}