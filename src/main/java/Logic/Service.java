package Logic;

import Data.*;
import Logic.Entities.Admin;
import Logic.Entities.Farmaceuta;
import Logic.Entities.Medico;
import Logic.Entities.Paciente;

import java.util.List;

public class Service {
    private static Service instance;
   public static Service getInstance() {
       if (instance == null) {
           instance = new Service();
       }
       return instance;
   }
    /// Aqui van Dao´s
    private PacienteDao pacienteDao;
    private FarmaceutaDao farmaceutaDao;
    private MedicoDao medicoDao;
    private AdminDao adminDao;

    private Service() {
        try{
            pacienteDao = new PacienteDao();
        }catch(Exception e){
            System.exit(-1);
        }
    }
    public void stop(){
        try{
            Database.getDatabase().close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    //============Pacientes=============
    public void create(Paciente paciente)throws Exception{
        pacienteDao.crate(paciente);
    }
    public Paciente read(String id) throws Exception{
       return pacienteDao.read(id);
    }
    public void update(Paciente paciente) throws Exception{
        pacienteDao.update(paciente);
    }
    public void delete(Paciente paciente) throws Exception{
        pacienteDao.delete(paciente);
    }
    public List<Paciente> findAll() throws Exception{
        return pacienteDao.findAll();
    }
    public boolean exists(Paciente paciente) throws Exception{
        return pacienteDao.exists(paciente.getId());
    }
    public Paciente findById(String id) throws Exception{
        return pacienteDao.read(id);
    }
    public List<Paciente> findByNome(String nome) throws Exception{
        return pacienteDao.searchByName(nome);
    }
    //=============Farmaceuta==========
    public void create(Farmaceuta farmaceuta) throws Exception{
        farmaceutaDao.create(farmaceuta);
    }
    public void update(Farmaceuta farmaceuta) throws Exception{
        farmaceutaDao.update(farmaceuta);
    }
    public void delete(Farmaceuta farmaceuta) throws Exception{
        farmaceutaDao.delete(farmaceuta);
    }
    public List<Farmaceuta> findAllFarmaceuta() throws Exception{
        return farmaceutaDao.findAll();
    }
    public List<Farmaceuta> findAllNombre(String nome) throws Exception{
        return farmaceutaDao.findByNombre(nome);
    }
    //================Medico========
    public void create(Medico medico) throws Exception{
            medicoDao.create(medico);
    }
    public void update(Medico medico) throws Exception{
        medicoDao.update(medico);
    }
    public void delete(Medico medico) throws Exception{
        medicoDao.delete(medico);
    }
    public List<Medico> findAllMedico() throws Exception{
        return medicoDao.findAll();
    }
    public List<Medico> findNombre(String nome) throws Exception{
        return medicoDao.searchByName(nome);
    }
    public boolean exists(Medico medico) throws Exception{
        return medicoDao.exists(medico.getId());
    }

    //===============Admin========
    public void create(Admin admin) throws Exception{
        adminDao.create(admin);
    }
    public Admin readAdmin(String id) throws Exception{
        return adminDao.read(id);
    }
    public List<Admin> findAllAdmin() throws Exception{
        return adminDao.findAll();
    }
    public List<Admin> findByClave(String clave) throws Exception{
        return adminDao.findByClave(clave);
    }

}
