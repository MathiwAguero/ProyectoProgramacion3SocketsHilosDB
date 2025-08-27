package Services;

import DataAccessObject.MedicamentoDAO;
import DataAccessObject.MedicoDAO;
import DataAccessObject.PacienteDAO;
import DataAccessObject.RecetaDAO;
import Exceptions.DataAccessException;
import Model.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceRecetas {
    private MedicoDAO medico = new MedicoDAO();
    private PacienteDAO paciente = new PacienteDAO();
    private RecetaDAO receta = new RecetaDAO();
    private MedicamentoDAO medicamento = new MedicamentoDAO();

    public void creacionReceta(Receta x) throws DataAccessException {
        validateRecipe(x);
        if(receta.existeId(x.getId())) {
            receta.actualizar(x);
        } else {
            receta.insertar(x);
        }
    }


    public void cambiarEstado(String idReceta, EstadoReceta estado)  throws DataAccessException {
        if (idReceta == null || estado == null) throw new DataAccessException("Datos incorrectos");
        Receta nueva =  receta.obtenerPorId(idReceta);
        if (nueva.getEstado() == EstadoReceta.ENTREGADA) throw new DataAccessException("La receta fue entregada");
        if (nueva.getEstado() == EstadoReceta.CONFECCIONADA) throw new DataAccessException("La receta esta confeccionada");
        if (nueva.getEstado() == EstadoReceta.PROCESO) throw new  DataAccessException("La receta esta en proceso");
        if (nueva.getEstado() == EstadoReceta.LISTA) throw new   DataAccessException("La receta esta lista");
        nueva.setEstado(estado);
        receta.actualizar(nueva);
    }

    public void agregarDetalle(String idReceta, RecipeDetails x) throws DataAccessException {
        if (idReceta == null || x == null) throw new DataAccessException("Datos incorrectos");
        Receta r = receta.obtenerPorId(idReceta);
        if (r == null) throw new  DataAccessException("Receta no existe");
        if(r.getDetalles()== null) r.setDetalles(new ArrayList<>());
        validateRecipe(r);
        r.getDetalles().add(x);
        receta.actualizar(r);
    }

    public void quitarDetalle(String idReceta, int x) throws DataAccessException {
        Receta aux = receta.obtenerPorId(idReceta);
        if (aux == null) throw new DataAccessException("Receta no existe");
        if (aux.getDetalles()== null || x < 0 || x>aux.getDetalles().size()) throw new DataAccessException("Invalido");
        aux.getDetalles().remove(x);
        receta.actualizar(aux);
    }

    public void validateRecipe(Receta x)  throws DataAccessException {
        if (x == null || x.getId() == null) throw  new DataAccessException("Datos incorrectos");
        if(x.getIdPaciente() == null || paciente.obtenerPorId(x.getIdPaciente()) == null) throw   new DataAccessException("Paciente no existe");
        if(x.getIdMedico() == null || medico.obtenerPorId(x.getIdMedico()) == null) throw  new DataAccessException("Medico no existe");
        List<RecipeDetails> details = x.getDetalles();
        if (details == null || details.isEmpty()) throw new DataAccessException("Debe agregar un medicamento");
        for (RecipeDetails detail : details) {
            validarDetalle(detail);
        }
        if (x.getEstado() == null) x.setEstado(EstadoReceta.CONFECCIONADA); //Por default
    }

    public void validarDetalle(RecipeDetails d)  throws DataAccessException {
        if(d == null) throw new DataAccessException("Datos incorrectos");
        if (d.getCodigoMedicamento() == null || medicamento.obtenerPorId(d.getCodigoMedicamento()) == null) {
            throw new  DataAccessException("El medicamento " + d.getCodigoMedicamento() + " no existe");
        }
        if (d.getCantidad() == 0) throw new DataAccessException("Cantidad invalida");
        if (d.getDuracionTratamiento() < 0) throw new DataAccessException("Duracion no permitida");
    }

}
