package Data;

import Logic.Exceptions.DataAccessException;
import Logic.Listas.Factory;
import Logic.Entities.*;

public class HospitalData {

    //Administrador
    static Admin ad1 = new Admin("ADM-111", "1234", "Nestor Zamora");
    static Admin ad2 = new Admin("ADM-222", "1111", "Jose Sanchez");

    //Medicos
    static Medico n1 = new Medico("MED-111", "111", "Mathiw Aguero", "Neurocirujano");
    static Medico n2 = new Medico("MED-222", "222", "Ignacio Bolanos", "Cardiologo");
    static Medico n3 = new Medico("MED-333", "333", "Naara menjivar", "Pediatra");

    //Pacientes
    static Paciente p1 = new Paciente("P001","Juan Perez", "2/06/2006", "12345678");
    static Paciente p2 = new Paciente("P002", "Maria Gonzalez", "2/03/1989", "87654321");

    //Farmaceutas
    static Farmaceuta f1 = new Farmaceuta("FAR-111", "123", "Wilson Ramirez");
    static Farmaceuta f2 =new Farmaceuta("FAR-222", "555", "Carlos Loria");
    static Farmaceuta f3 = new Farmaceuta("FAR-333", "123456", "Juan Pablo");

    //Medicamentos
    static Medicamento m1 = new Medicamento("001", "Acetaminofen", "100 mg");
    static Medicamento m2 = new Medicamento("002", "Panadol", "500 mg");

    //RecipeDetails
    static RecipeDetails rcp1 = new RecipeDetails(m1.getNombre(), 5, "Tomar una cada 8 horas", 15);
    static RecipeDetails rcp2 = new RecipeDetails(m2.getNombre(), 10, "Tomar una al dia", 10);

    //Recetas
    //static Receta r1 = new Receta("004", p1, n1, "1/08/2025", "2/08/2025", EstadoReceta.ENTREGADA, new java.util.ArrayList<>());
    //static Receta r2 = new Receta("005", p2, n1, "20/07/2025", "1/08/2025", EstadoReceta.LISTA,      new java.util.ArrayList<>());
    //static { r1.agregarDetalle(rcp2); r2.agregarDetalle(rcp1); }

    public static void incializarDatos() {
        try {
                if(Factory.get().admin().obtenerTodos().isEmpty()){
                    crearDatosAdmins();
                }
                if(Factory.get().medico().obtenerTodos().isEmpty()){
                    crearDatosMedicos();
                }
                if(Factory.get().paciente().obtenerTodos().isEmpty()){
                crearDatosPacientes();
                }
                if(Factory.get().medicamento().obtenerTodos().isEmpty()){
                crearDatosMedicamentos();
                }
                /*if(Factory.get().receta().obtenerTodos().isEmpty()){
                //crearDatosRecetas();
                }*/
                if(Factory.get().farmaceuta().obtenerTodos().isEmpty()){
                crearDatosFarmaceutas();
                }
                if(Factory.get().details().obtenerTodos().isEmpty()){
                crearDetallesRecetas();
                }
                if(Factory.get().usuario().obtenerTodos().isEmpty()){
                crearDatosUsuarios();
            }

                System.out.println("Datos cargados correctamente");

        } catch (Exception e) {
            System.out.println("Error inicializando datos: " + e.getMessage());
        }
    }

    private static void crearDatosAdmins() throws DataAccessException {
        Factory.get().admin().insertar(ad1);
        Factory.get().admin().insertar(ad2);

    }

    private static void crearDatosMedicos() throws DataAccessException {
        Factory.get().medico().insertar(n1);
        Factory.get().medico().insertar(n2);
        Factory.get().medico().insertar(n3);
    }

    private static void crearDatosPacientes() throws DataAccessException {

        Factory.get().paciente().insertar(p1);
        Factory.get().paciente().insertar(p2);
    }

    private static void crearDatosFarmaceutas() throws DataAccessException {

        Factory.get().farmaceuta().insertar(f1);
        Factory.get().farmaceuta().insertar(f2);
        Factory.get().farmaceuta().insertar(f3);

    }

    /*private static void crearDatosRecetas() throws DataAccessException {
        Factory.get().receta().insertar(r1);
        Factory.get().receta().insertar(r2);
    }*/

    private static void crearDatosMedicamentos() throws DataAccessException {
        Factory.get().medicamento().insertar(m1);
        Factory.get().medicamento().insertar(m2);
    }

    private static void crearDetallesRecetas()  throws DataAccessException {
        Factory.get().details().insertar(rcp1);
        Factory.get().details().insertar(rcp2);
    }

    private static void crearDatosUsuarios() throws DataAccessException  {
        var users = Factory.get().usuario();

            users.insertar(new UsuarioBase(ad1.getId(), ad1.getClave(), ad1.getNombre(), TipoUsuario.ADMINISTRADOR));
            users.insertar(new UsuarioBase(ad2.getId(), ad2.getClave(), ad2.getNombre(), TipoUsuario.ADMINISTRADOR));

            users.insertar(new UsuarioBase(n1.getId(), n1.getClave(), n1.getNombre(), TipoUsuario.MEDICO));
            users.insertar(new UsuarioBase(n2.getId(), n2.getClave(), n2.getNombre(), TipoUsuario.MEDICO));
            users.insertar(new UsuarioBase(n3.getId(), n3.getClave(), n3.getNombre(), TipoUsuario.MEDICO));

            users.insertar(new UsuarioBase(f1.getId(), f1.getClave(), f1.getNombre(), TipoUsuario.FARMECEUTA));
            users.insertar(new UsuarioBase(f2.getId(), f2.getClave(), f2.getNombre(), TipoUsuario.FARMECEUTA));
            users.insertar(new UsuarioBase(f3.getId(), f3.getClave(), f3.getNombre(), TipoUsuario.FARMECEUTA));
    }


}
