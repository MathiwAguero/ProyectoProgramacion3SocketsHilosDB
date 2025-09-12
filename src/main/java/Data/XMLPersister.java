package Data;

import jakarta.xml.bind.*;
import jakarta.xml.bind.annotation.*;

import Logic.Entities.*;
import Logic.Exceptions.DataAccessException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class XMLPersister {

    private String path;
    private static XMLPersister theInstance;

    public static XMLPersister instance() {
        if (theInstance == null)
            theInstance = new XMLPersister(
                    System.getProperty("user.dir") + "/src/main/resources/ArchivosXML/Hospital_data.XML"
            );
        return theInstance;
    }

    public XMLPersister(String p) { path = p; }

    @XmlRootElement(name = "hospital")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Data {

        @XmlElementWrapper(name = "admins")
        @XmlElement(name = "admin")
        private List<Admin> admins = new ArrayList<>();

        @XmlElementWrapper(name = "medicos")
        @XmlElement(name = "medico")
        private List<Medico> medicos = new ArrayList<>();

        @XmlElementWrapper(name = "pacientes")
        @XmlElement(name = "paciente")
        private List<Paciente> pacientes = new ArrayList<>();

        @XmlElementWrapper(name = "medicamentos")
        @XmlElement(name = "medicamento")
        private List<Medicamento> medicamentos = new ArrayList<>();

        @XmlElementWrapper(name = "recetas")
        @XmlElement(name = "receta")
        private List<Receta> recetas = new ArrayList<>();

        @XmlElementWrapper(name = "usuarios")
        @XmlElement(name = "usuario")
        private List<UsuarioBase> usuarios = new ArrayList<>();

        @XmlElementWrapper(name = "farmaceutas")
        @XmlElement(name = "farmaceuta")
        private List<Farmaceuta> farmaceutas = new ArrayList<>();

        @XmlElementWrapper(name = "detalles")
        @XmlElement(name = "detalle")
        private List<RecipeDetails> detalles = new ArrayList<>();

        public Data() {}

        public List<Admin> getAdmins() { return admins; }
        public void setAdmins(List<Admin> v){ admins=v; }

        public List<Medico> getMedicos() { return medicos; }
        public void setMedicos(List<Medico> v) { medicos=v; }

        public List<Paciente> getPacientes() { return pacientes; }
        public void setPacientes(List<Paciente> v) { pacientes=v; }

        public List<Medicamento> getMedicamentos() { return medicamentos; }
        public void setMedicamentos(List<Medicamento> v) { medicamentos=v; }

        public List<Receta> getRecetas() { return recetas; }
        public void setRecetas(List<Receta> v) { recetas=v; }

        public List<UsuarioBase> getUsuarios() { return usuarios; }
        public void setUsuarios(List<UsuarioBase> v) { usuarios=v; }

        public List<Farmaceuta> getFarmaceutas() { return farmaceutas; }
        public void setFarmaceutas(List<Farmaceuta> v) { farmaceutas=v; }

        public List<RecipeDetails> getDetalles() { return detalles; }
        public void setDetalles(List<RecipeDetails> v) { detalles=v; }
    }

    public Data load() throws DataAccessException {
        try {
            JAXBContext ctx = JAXBContext.newInstance(
                    Data.class,
                    Admin.class, Medico.class, Paciente.class, Medicamento.class, Receta.class,
                    UsuarioBase.class, Farmaceuta.class, RecipeDetails.class,
                    EstadoReceta.class, TipoUsuario.class
            );
            Unmarshaller um = ctx.createUnmarshaller();
            try (FileInputStream is = new FileInputStream(path)) {
                return (Data) um.unmarshal(is);
            }
        } catch (Exception e) {
            throw new DataAccessException("Error leyendo XML: " + e.getMessage());
        }
    }

    public void store(Data d) throws DataAccessException {
        try {
            JAXBContext ctx = JAXBContext.newInstance(
                    Data.class,
                    Admin.class, Medico.class, Paciente.class, Medicamento.class, Receta.class,
                    UsuarioBase.class, Farmaceuta.class, RecipeDetails.class,
                    EstadoReceta.class, TipoUsuario.class
            );
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            try (FileOutputStream os = new FileOutputStream(path)) {
                m.marshal(d, os);
                os.flush();
            }
        } catch (Exception e) {
            throw new DataAccessException("Error guardando XML: " + e.getMessage());
        }
    }
}

