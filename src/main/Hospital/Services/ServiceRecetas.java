package Services;

import ManejoListas.MedicamentoList;
import ManejoListas.MedicoList;
import ManejoListas.PacienteList;
import ManejoListas.RecetaList;
import Exceptions.DataAccessException;
import Entidades.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceRecetas {
    private MedicoList medicoList = new MedicoList();
    private PacienteList pacienteList = new PacienteList();
    private RecetaList recetaList = new RecetaList();
    private MedicamentoList medicamentoList = new MedicamentoList();

    public void creacionReceta(Entidades.Receta x) throws DataAccessException {
        validateRecipe(x);
        if(recetaList.existeId(x.getId())) {
            recetaList.actualizar(x);
        } else {
            recetaList.insertar(x);
        }
    }


    public void cambiarEstado(String idReceta, EstadoReceta estado)  throws DataAccessException {
        if (idReceta == null || estado == null) throw new DataAccessException("Datos incorrectos");
        Entidades.Receta nueva =  recetaList.obtenerPorId(idReceta);
        if (nueva.getEstado() == EstadoReceta.ENTREGADA) throw new DataAccessException("La receta fue entregada");
        if (nueva.getEstado() == EstadoReceta.CONFECCIONADA) throw new DataAccessException("La receta esta confeccionada");
        if (nueva.getEstado() == EstadoReceta.PROCESO) throw new  DataAccessException("La receta esta en proceso");
        if (nueva.getEstado() == EstadoReceta.LISTA) throw new   DataAccessException("La receta esta lista");
        nueva.setEstado(estado);
        recetaList.actualizar(nueva);
    }

    public void agregarDetalle(String idReceta, RecipeDetails x) throws DataAccessException {
        if (idReceta == null || x == null) throw new DataAccessException("Datos incorrectos");
        Entidades.Receta r = recetaList.obtenerPorId(idReceta);
        if (r == null) throw new  DataAccessException("Receta no existe");
        if(r.getDetalles()== null) r.setDetalles(new ArrayList<>());
        validateRecipe(r);
        r.getDetalles().add(x);
        recetaList.actualizar(r);
    }

    public void quitarDetalle(String idReceta, int x) throws DataAccessException {
        Entidades.Receta aux = recetaList.obtenerPorId(idReceta);
        if (aux == null) throw new DataAccessException("Receta no existe");
        if (aux.getDetalles()== null || x < 0 || x>aux.getDetalles().size()) throw new DataAccessException("Invalido");
        aux.getDetalles().remove(x);
        recetaList.actualizar(aux);
    }

    public void validateRecipe(Entidades.Receta x)  throws DataAccessException {
        if (x == null || x.getId() == null) throw  new DataAccessException("Datos incorrectos");
        if(x.getIdPaciente() == null || pacienteList.obtenerPorId(x.getId()) == null) throw   new DataAccessException("Paciente no existe");
        if(x.getIdMedico() == null || medicoList.obtenerPorId(x.getId()) == null) throw  new DataAccessException("Medico no existe");
        List<RecipeDetails> details = x.getDetalles();
        if (details == null || details.isEmpty()) throw new DataAccessException("Debe agregar un medicamento");
        for (RecipeDetails detail : details) {
            validarDetalle(detail);
        }
        if (x.getEstado() == null) x.setEstado(EstadoReceta.CONFECCIONADA); //Por default
    }

    public void validarDetalle(RecipeDetails d)  throws DataAccessException {
        if(d == null) throw new DataAccessException("Datos incorrectos");
        if (d.getCodigoMedicamento() == null || medicamentoList.obtenerPorId(d.getCodigoMedicamento()) == null) {
            throw new  DataAccessException("El medicamento " + d.getCodigoMedicamento() + " no existe");
        }
        if (d.getCantidad() == 0) throw new DataAccessException("Cantidad invalida");
        if (d.getDuracionTratamiento() < 0) throw new DataAccessException("Duracion no permitida");
    }

}
