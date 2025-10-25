package hospital.Logic.Services;

import Logic.Listas.MedicamentoList;
import Logic.Listas.MedicoList;
import Logic.Listas.PacienteList;
import Logic.Listas.RecetaList;
import Logic.Exceptions.DataAccessException;
import Logic.Entities.EstadoReceta;
import Logic.Entities.Receta;
import Logic.Entities.RecipeDetails;

import java.util.ArrayList;
import java.util.List;

public class ServiceRecetas {
    private final MedicoList medicoList = new MedicoList();
    private final PacienteList pacienteList = new PacienteList();
    private final RecetaList recetaList = new RecetaList();
    private final MedicamentoList medicamentoList = new MedicamentoList();

    public void creacionReceta(Receta x) throws DataAccessException {
        validateRecipe(x);
        if (recetaList.existeId(x.getId())) {
            recetaList.actualizar(x);
        } else {
            recetaList.insertar(x);
        }
    }

    public void cambiarEstado(String idReceta, EstadoReceta nuevo) throws DataAccessException {
        if (idReceta == null || nuevo == null) throw new DataAccessException("Datos incorrectos");
        Receta r = recetaList.obtenerPorId(idReceta);
        if (r == null) throw new DataAccessException("Receta no existe");
        if (r.getEstado() == EstadoReceta.ENTREGADA) throw new DataAccessException("La receta ya fue entregada");
        if (r.getEstado() == EstadoReceta.PROCESO && nuevo == EstadoReceta.LISTA) throw new DataAccessException("Debe estar confeccionada antes de estar lista");
        r.setEstado(nuevo);
        recetaList.actualizar(r);
    }

    public void agregarDetalle(String idReceta, RecipeDetails x) throws DataAccessException {
        if (idReceta == null || x == null) throw new DataAccessException("Datos incorrectos");
        Receta r = recetaList.obtenerPorId(idReceta);
        if (r == null) throw new DataAccessException("Receta no existe");
        if (r.getDetalles() == null) r.setDetalles(new ArrayList<>());

        validarDetalle(x);
        r.getDetalles().add(x);
        if (r.getEstado() == null) r.setEstado(EstadoReceta.PROCESO);
        recetaList.actualizar(r);
    }

    public void quitarDetalle(String idReceta, int index) throws DataAccessException {
        Receta r = recetaList.obtenerPorId(idReceta);
        if (r == null) throw new DataAccessException("Receta no existe");
        if (r.getDetalles() == null) throw new DataAccessException("No hay detalles");
        if (index < 0 || index >= r.getDetalles().size()) throw new DataAccessException("Índice inválido");
        r.getDetalles().remove(index);
        recetaList.actualizar(r);
    }

    public void validateRecipe(Receta x) throws DataAccessException {
        if (x == null || x.getId() == null) throw new DataAccessException("Datos incorrectos");

        if (x.getPaciente() == null || x.getPaciente().getId() == null ||
                pacienteList.obtenerPorId(x.getPaciente().getId()) == null) {
            throw new DataAccessException("Paciente no existe");
        }

        if (x.getMedico() == null || x.getMedico().getId() == null ||
                medicoList.obtenerPorId(x.getMedico().getId()) == null) {
            throw new DataAccessException("Médico no existe");
        }

        List<RecipeDetails> details = x.getDetalles();
        if (details == null || details.isEmpty()) throw new DataAccessException("Debe agregar un medicamento");

        for (RecipeDetails d : details) validarDetalle(d);

        if (x.getEstado() == null) x.setEstado(EstadoReceta.CONFECCIONADA);
    }

    public void validarDetalle(RecipeDetails d) throws DataAccessException {
        if (d == null) throw new DataAccessException("Detalle invalido");
        if (d.getCodigoMedicamento() == null ||
                medicamentoList.obtenerPorId(d.getCodigoMedicamento()) == null) {
            throw new DataAccessException("El medicamento " + d.getCodigoMedicamento() + " no existe");
        }
        if (d.getCantidad() <= 0) throw new DataAccessException("Cantidad invalida");
        if (d.getDuracionTratamiento() <= 0) throw new DataAccessException("Duración invalida");
    }
}

