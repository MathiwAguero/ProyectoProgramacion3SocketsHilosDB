package hospital.Data;

import hospital.Logic.Service;
import hospital.Entities.Entities.*;

public class HospitalData {

    // Administradores
    static Admin ad1 = new Admin("ADM-111", "1234", "Nestor Zamora");
    static Admin ad2 = new Admin("ADM-222", "1111", "Jose Sanchez");

    // Médicos
    static Medico n1 = new Medico("MED-111", "111", "Mathiw Aguero", "Neurocirujano");
    static Medico n2 = new Medico("MED-222", "222", "Ignacio Bolanos", "Cardiologo");
    static Medico n3 = new Medico("MED-333", "333", "Naara Menjivar", "Pediatra");

    // Pacientes
    static Paciente p1 = new Paciente("P001", "Juan Perez", "2/06/2006", "12345678");
    static Paciente p2 = new Paciente("P002", "Maria Gonzalez", "2/03/1989", "87654321");

    // Farmaceutas
    static Farmaceuta f1 = new Farmaceuta("FAR-111", "123", "Wilson Ramirez");
    static Farmaceuta f2 = new Farmaceuta("FAR-222", "555", "Carlos Loria");
    static Farmaceuta f3 = new Farmaceuta("FAR-333", "123456", "Juan Pablo");

    // Medicamentos
    static Medicamento m1 = new Medicamento("001", "Acetaminofen", "100 mg");
    static Medicamento m2 = new Medicamento("002", "Panadol", "500 mg");

    // RecipeDetails
    static RecipeDetails rcp1 = new RecipeDetails(m1.getNombre(), 5, "Tomar una cada 8 horas", 15);
    static RecipeDetails rcp2 = new RecipeDetails(m2.getNombre(), 10, "Tomar una al dia", 10);

    /**
     * Inicializa los datos del sistema si no existen
     */
    public static void inicializarDatos() {
        try {
            System.out.println("=== Iniciando carga de datos ===");

            // CAMBIO: Todas las llamadas a Factory → Service
            if (Service.getInstance().findAllAdmins().isEmpty()) {
                crearDatosAdmins();
                System.out.println("✓ Admins cargados");
            }

            if (Service.getInstance().findAllMedicos().isEmpty()) {
                crearDatosMedicos();
                System.out.println("✓ Médicos cargados");
            }

            if (Service.getInstance().findAllPacientes().isEmpty()) {
                crearDatosPacientes();
                System.out.println("✓ Pacientes cargados");
            }

            if (Service.getInstance().findAllMedicamentos().isEmpty()) {
                crearDatosMedicamentos();
                System.out.println("✓ Medicamentos cargados");
            }

            if (Service.getInstance().findAllFarmaceutas().isEmpty()) {
                crearDatosFarmaceutas();
                System.out.println("✓ Farmaceutas cargados");
            }

            if (Service.getInstance().findAllDetalles().isEmpty()) {
                crearDetallesRecetas();
                System.out.println("✓ Detalles cargados");
            }

            if (Service.getInstance().findAllUsuarios().isEmpty()) {
                crearDatosUsuarios();
                System.out.println("✓ Usuarios cargados");
            }

            System.out.println("=== Datos cargados correctamente ===");

        } catch (Exception e) {
            System.err.println("Error inicializando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void crearDatosAdmins() throws Exception {
        Service.getInstance().create(ad1);
        Service.getInstance().create(ad2);
    }

    private static void crearDatosMedicos() throws Exception {
        Service.getInstance().create(n1);
        Service.getInstance().create(n2);
        Service.getInstance().create(n3);
    }

    private static void crearDatosPacientes() throws Exception {
        Service.getInstance().create(p1);
        Service.getInstance().create(p2);
    }

    private static void crearDatosFarmaceutas() throws Exception {
        Service.getInstance().create(f1);
        Service.getInstance().create(f2);
        Service.getInstance().create(f3);
    }

    private static void crearDatosMedicamentos() throws Exception {
        Service.getInstance().create(m1);
        Service.getInstance().create(m2);
    }

    private static void crearDetallesRecetas() throws Exception {
        // Nota: Los detalles normalmente se crean asociados a una receta específica
        // Aquí solo los creamos si tienes recetas de ejemplo

        // Si tienes recetas predefinidas, descomenta esto:
        /*
        if (!Service.getInstance().findAllRecetas().isEmpty()) {
            Service.getInstance().createDetalle(rcp1, "R001");
            Service.getInstance().createDetalle(rcp2, "R002");
        }
        */
    }

    private static void crearDatosUsuarios() throws Exception {
        // Usuarios basados en Admins
        Service.getInstance().create(
                new UsuarioBase(ad1.getId(), ad1.getClave(), ad1.getNombre(), TipoUsuario.ADMINISTRADOR)
        );
        Service.getInstance().create(
                new UsuarioBase(ad2.getId(), ad2.getClave(), ad2.getNombre(), TipoUsuario.ADMINISTRADOR)
        );

        // Usuarios basados en Médicos
        Service.getInstance().create(
                new UsuarioBase(n1.getId(), n1.getClave(), n1.getNombre(), TipoUsuario.MEDICO)
        );
        Service.getInstance().create(
                new UsuarioBase(n2.getId(), n2.getClave(), n2.getNombre(), TipoUsuario.MEDICO)
        );
        Service.getInstance().create(
                new UsuarioBase(n3.getId(), n3.getClave(), n3.getNombre(), TipoUsuario.MEDICO)
        );

        // Usuarios basados en Farmaceutas
        Service.getInstance().create(
                new UsuarioBase(f1.getId(), f1.getClave(), f1.getNombre(), TipoUsuario.FARMECEUTA)
        );
        Service.getInstance().create(
                new UsuarioBase(f2.getId(), f2.getClave(), f2.getNombre(), TipoUsuario.FARMECEUTA)
        );
        Service.getInstance().create(
                new UsuarioBase(f3.getId(), f3.getClave(), f3.getNombre(), TipoUsuario.FARMECEUTA)
        );
    }

    /**
     * Limpia todos los datos del sistema
     * USAR CON PRECAUCIÓN
     */
    public static void limpiarDatos() {
        try {
            System.out.println("⚠️ Limpiando todos los datos...");

            // Eliminar en orden inverso por dependencias
            for (Receta r : Service.getInstance().findAllRecetas()) {
                Service.getInstance().delete(r);
            }

            for (UsuarioBase u : Service.getInstance().findAllUsuarios()) {
                Service.getInstance().delete(u);
            }

            for (Farmaceuta f : Service.getInstance().findAllFarmaceutas()) {
                Service.getInstance().delete(f);
            }

            for (Medicamento m : Service.getInstance().findAllMedicamentos()) {
                Service.getInstance().delete(m);
            }

            for (Paciente p : Service.getInstance().findAllPacientes()) {
                Service.getInstance().delete(p);
            }

            for (Medico m : Service.getInstance().findAllMedicos()) {
                Service.getInstance().delete(m);
            }

            System.out.println("✓ Datos limpiados exitosamente");

        } catch (Exception e) {
            System.err.println("Error limpiando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}