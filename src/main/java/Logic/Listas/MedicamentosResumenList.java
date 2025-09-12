package Logic.Listas;

import Logic.Entities.MedicamentosResumen;
import Logic.Entities.Receta;
import Logic.Entities.RecipeDetails;
import Logic.Exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicamentosResumenList extends Base<MedicamentosResumen> {
    private List<MedicamentosResumen> medicamentos = new ArrayList<>();

    @Override
    public void insertar(MedicamentosResumen m) throws DataAccessException {
        if (m == null || m.getNombreMedicamento() == null)
            throw new DataAccessException("Medicamento invalido");
        if (existeId(m.getNombreMedicamento()))
            throw new DataAccessException("Medicamento ya existe: " + m.getNombreMedicamento());
        medicamentos.add(m);
        guardarEnXML();
    }

    @Override
    public MedicamentosResumen obtenerPorId(String id) throws DataAccessException {
        if (id == null) return null;
        return medicamentos.stream()
                .filter(x -> id.equals(x.getNombreMedicamento()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<MedicamentosResumen> obtenerTodos() throws DataAccessException {
        return new ArrayList<>(medicamentos); // Retornar copia para evitar modificaciones externas
    }
    public List<MedicamentosResumen> obtenerElementosFiltrados(String nombreMedicamento) throws DataAccessException {
        if (nombreMedicamento == null) return null;
        return medicamentos.stream().filter(x -> nombreMedicamento.equals(x.getNombreMedicamento())).collect(Collectors.toList());
    }
    @Override
    public void actualizar(MedicamentosResumen objeto) throws DataAccessException {
        // Implementar si es necesario
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        // Implementar si es necesario
    }

    @Override
    public boolean existeId(String id) throws DataAccessException {
        return id != null && medicamentos.stream().anyMatch(x -> id.equals(x.getNombreMedicamento()));
    }

    @Override
    protected void guardarEnXML() throws DataAccessException {
        // Implementar guardado en XML
    }

    @Override
    protected void cargarDesdeXML() throws DataAccessException {
        // Implementar carga desde XML
    }

    public void insertarConCantidad(String codigoMedicamento, int cantidad) throws DataAccessException {
        if (existeId(codigoMedicamento)) {
            MedicamentosResumen med = obtenerPorId(codigoMedicamento);
            med.setCantidadTotal(med.getCantidadTotal() + cantidad);
            actualizarEnLista(med);
        } else {
            MedicamentosResumen med = new MedicamentosResumen();
            med.setCantidadTotal(cantidad);
            med.setNombreMedicamento(codigoMedicamento);
            insertar(med);
        }
    }

    private void actualizarEnLista(MedicamentosResumen medicamentoActualizado) {
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getNombreMedicamento().equals(medicamentoActualizado.getNombreMedicamento())) {
                medicamentos.set(i, medicamentoActualizado);
                break;
            }
        }
        try {
            guardarEnXML();
        } catch (DataAccessException e) {
            System.err.println("Error al guardar en XML: " + e.getMessage());
        }
    }

    public void insertarLista(List<Receta> recetas) throws DataAccessException {
        if (recetas == null || recetas.isEmpty()) return;

        for (Receta receta : recetas) {
            List<RecipeDetails> detalles = receta.getDetalles();

            if (detalles != null) {
                for (RecipeDetails detalle : detalles) {
                    String codigoMedicamento = detalle.getNombre();
                    int cantidad = detalle.getCantidad();

                    if (codigoMedicamento != null && !codigoMedicamento.isEmpty() && cantidad > 0) {
                        insertarConCantidad(codigoMedicamento, cantidad);
                    }
                }
            }
        }
    }
}