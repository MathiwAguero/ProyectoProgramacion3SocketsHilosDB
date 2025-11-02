CREATE DATABASE IF NOT EXISTS Hospital;

USE Hospital;

-- Tabla de Administradores
CREATE TABLE IF NOT EXISTS admin (
    id VARCHAR(20) NOT NULL,
    clave VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- Tabla de Médicos
CREATE TABLE IF NOT EXISTS medicos (
    id VARCHAR(20) NOT NULL,
    clave VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- Tabla de Farmaceutas
CREATE TABLE IF NOT EXISTS farmaceutas (
    id VARCHAR(20) NOT NULL,
    clave VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- Tabla de Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fechaNacimiento VARCHAR(20) NOT NULL,
    numeroTelefono VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

-- Tabla de Medicamentos
CREATE TABLE IF NOT EXISTS medicamentos (
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    presentacion VARCHAR(100) NOT NULL,
    PRIMARY KEY (codigo)
);

-- Tabla de Usuarios (para login y sesiones)
CREATE TABLE IF NOT EXISTS usuarios (
    id VARCHAR(20) NOT NULL,
    clave VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- MEDICO, FARMECEUTA, ADMINISTRADOR
    activo BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

-- Tabla de Recetas
CREATE TABLE IF NOT EXISTS recetas (
    id VARCHAR(20) NOT NULL,
    paciente_id VARCHAR(20) NOT NULL,
    medico_id VARCHAR(20) NOT NULL,
    fechaConfeccion VARCHAR(20) NOT NULL,
    fechaRetiro VARCHAR(20),
    estado VARCHAR(20) NOT NULL, -- CONFECCIONADA, PROCESO, LISTA, ENTREGADA
    PRIMARY KEY (id),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES medicos(id) ON DELETE CASCADE
);

-- Tabla de Detalles de Recetas
CREATE TABLE IF NOT EXISTS receta_detalles (
    id INT AUTO_INCREMENT,
    receta_id VARCHAR(20) NOT NULL,
    codigoMedicamento VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    indicaciones TEXT,
    duracionTratamiento INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (receta_id) REFERENCES recetas(id) ON DELETE CASCADE,
    FOREIGN KEY (codigoMedicamento) REFERENCES medicamentos(codigo) ON DELETE CASCADE
);

-- Tabla de Mensajes (para la funcionalidad de chat)
CREATE TABLE IF NOT EXISTS mensajes (
    id INT AUTO_INCREMENT,
    remitente_id VARCHAR(20) NOT NULL,
    destinatario_id VARCHAR(20) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (remitente_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (destinatario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Insertar datos iniciales de Administradores
INSERT INTO admin (id, clave, nombre) VALUES
('ADM-111', '1234', 'Nestor Zamora'),
('ADM-222', '1111', 'Jose Sanchez'),
('ADM-333','2222','Gregorio Villalobos');

-- Insertar datos iniciales de Médicos
INSERT INTO medicos (id, clave, nombre, especialidad) VALUES
('MED-111', '111', 'Mathiw Aguero', 'Neurocirujano'),
('MED-222', '222', 'Ignacio Bolanos', 'Cardiologo'),
('MED-333', '333', 'Naara Menjivar', 'Pediatra');

-- Insertar datos iniciales de Pacientes
INSERT INTO pacientes (id, nombre, fechaNacimiento, numeroTelefono) VALUES
('P001', 'Juan Perez', '2/06/2006', '12345678'),
('P002', 'Maria Gonzalez', '2/03/1989', '87654321');

-- Insertar datos iniciales de Farmaceutas
INSERT INTO farmaceutas (id, clave, nombre) VALUES
('FAR-111', '123', 'Wilson Ramirez'),
('FAR-222', '555', 'Carlos Loria'),
('FAR-333', '123456', 'Juan Pablo');

-- Insertar datos iniciales de Medicamentos
INSERT INTO medicamentos (codigo, nombre, presentacion) VALUES
('001', 'Acetaminofen', '100 mg'),
('002', 'Panadol', '500 mg');

-- Insertar datos iniciales de Usuarios
INSERT INTO usuarios (id, clave, nombre, tipo, activo) VALUES
('ADM-111', '1234', 'Nestor Zamora', 'ADMINISTRADOR', FALSE),
('ADM-222', '1111', 'Jose Sanchez', 'ADMINISTRADOR', FALSE),
('MED-111', '111', 'Mathiw Aguero', 'MEDICO', FALSE),
('MED-222', '222', 'Ignacio Bolanos', 'MEDICO', FALSE),
('MED-333', '333', 'Naara Menjivar', 'MEDICO', FALSE),
('FAR-111', '123', 'Wilson Ramirez', 'FARMECEUTA', FALSE),
('FAR-222', '555', 'Carlos Loria', 'FARMECEUTA', FALSE),
('FAR-333', '123456', 'Juan Pablo', 'FARMECEUTA', FALSE);
