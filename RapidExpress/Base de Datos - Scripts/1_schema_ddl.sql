USE RapidExpress;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET FOREIGN_KEY_CHECKS = 1;
-- Modulo 1, Gestion de la Flota de Vehiculos
CREATE TABLE IF NOT EXISTS vehiculo(
placa VARCHAR(10) NOT NULL,
marca VARCHAR(50) NOT NULL,
modelo VARCHAR(50) NOT NULL,
anio_fabricacion YEAR NOT NULL,
capacidad_carga_kg DECIMAL(8,2) NOT NULL,
estado ENUM('DISPONIBLE','EN_RUTA', 'EN_MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT pk_vehiculo PRIMARY KEY (placa),
CONSTRAINT chk_vehiculo_capacidad CHECK (capacidad_carga_kg > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Parque automotor de la empresa';
CREATE TABLE IF NOT EXISTS mantenimiento(
id INT NOT NULL AUTO_INCREMENT,
vehiculo_placa VARCHAR(10) NOT NULL,
fecha_inicio DATETIME NOT NULL,
fecha_fin DATETIME NULL,
descripcion VARCHAR(255) NOT NULL,
CONSTRAINT pk_mantenimiento PRIMARY KEY(id),
CONSTRAINT fk_mantenimiento_vehiculo FOREIGN KEY (vehiculo_placa) REFERENCES vehiculo(placa) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT chk_mantenimiento_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio),
INDEX idx_mantenimiento_vehiculo (vehiculo_placa)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Historial de Mantenimientos por vehiculo. fecha_fin NULL = mantenimiento abierto';
-- Gestion de Personal (Conductores)
CREATE TABLE IF NOT EXISTS conductor(
numero_identificacion VARCHAR(15) NOT NULL,
nombre_completo VARCHAR(120) NOT NULL,
tipo_licencia VARCHAR(10) NOT NULL,
numero_contacto VARCHAR(20) NOT NULL,
estado ENUM('ACTIVO', 'DE_VACACIONES', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT pk_conductor PRIMARY KEY (numero_identificacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Conductores de la Empresa';
CREATE TABLE IF NOT EXISTS asignacion_conductor_vehiculo(
id INT NOT NULL AUTO_INCREMENT,
conductor_id VARCHAR(15) NOT NULL,
vehiculo_placa VARCHAR(10) NOT NULL,
fecha_inicio DATETIME NOT NULL,
fecha_fin DATETIME NULL,
CONSTRAINT pk_asignacion PRIMARY KEY(id),
CONSTRAINT fk_asignacion_conductor FOREIGN KEY(conductor_id) REFERENCES conductor (numero_identificacion) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_asignacion_vehiculo FOREIGN KEY (vehiculo_placa) REFERENCES vehiculo (placa) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT chk_asignacion_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio),
INDEX idx_asignacion_conductor (conductor_id),
INDEX idx_asignacion_vehiculo (vehiculo_placa)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Historial de Asignaciones Conductor / Vehiculo. fecha_fin NULL = Asignacion vigente';
-- Gestion de Paquetes y Envios
CREATE TABLE IF NOT EXISTS cliente(
id INT NOT NULL AUTO_INCREMENT,
tipo_documento VARCHAR(5) NOT NULL,
numero_documento VARCHAR(20) NOT NULL,
nombre_completo VARCHAR(120) NOT NULL,
direccion VARCHAR(150) NOT NULL,
telefono VARCHAR(20) NOT NULL,
CONSTRAINT pk_cliente PRIMARY KEY (id),
CONSTRAINT uq_cliente_documento UNIQUE (tipo_documento, numero_documento)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Remitentes y Destinatarios de los paquetes';
CREATE TABLE IF NOT EXISTS paquete(
tracking_id VARCHAR(20) NOT NULL,
descripcion_contenido VARCHAR(255) NOT NULL,
peso_kg DECIMAL(6,2) NOT NULL,
largo_cm DECIMAL (6,2) NOT NULL,
ancho_cm DECIMAL (6,2) NOT NULL,
alto_cm DECIMAL (6,2) NOT NULL,
direccion_origen VARCHAR(150) NOT NULL,
direccion_destino VARCHAR(150) NOT NULL,
remitente_id INT NOT NULL,
destinatario_id INT NOT NULL,
estado ENUM('EN_BODEGA', 'ASIGNADO_A_RUTA', 'EN_TRANSITO', 'ENTREGADO', 'DEVUELTO') NOT NULL DEFAULT 'EN_BODEGA',
fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT pk_paquete PRIMARY KEY (tracking_id),
CONSTRAINT fk_paquete_remitente FOREIGN KEY (remitente_id) REFERENCES cliente(id) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_paquete_destinatario FOREIGN KEY (destinatario_id) REFERENCES cliente(id) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT chk_paquete_peso CHECK (peso_kg > 0),
CONSTRAINT chk_paquete_dimensiones CHECK (largo_cm > 0 AND ancho_cm > 0 AND alto_cm > 0),
INDEX idx_paquete_remitente (remitente_id),
INDEX idx_paquete_destinatario (destinatario_id),
INDEX idx_paquete_estado (estado)
) ENGINE = InnoDB DEFAULT CHARSET= utf8mb4 COMMENT = 'Paquetes registrados y su ciclo de vida';
-- Modulo 4, Planificacion y Seguimiento de Rutas
CREATE TABLE IF NOT EXISTS ruta(
id INT NOT NULL AUTO_INCREMENT,
fecha DATE NOT NULL,
vehiculo_placa VARCHAR(10) NOT NULL,
conductor_id VARCHAR(15) NOT NULL,
estado ENUM('PLANIFICADA', 'EN_CURSO', 'FINALIZADA') NOT NULL DEFAULT 'PLANIFICADA',
hora_inicio DATETIME NULL,
hora_fin DATETIME NULL,
CONSTRAINT pk_ruta PRIMARY KEY(id),
CONSTRAINT fk_ruta_vehiculo FOREIGN KEY (vehiculo_placa) REFERENCES vehiculo (placa) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_ruta_conductor FOREIGN KEY (conductor_id) REFERENCES conductor (numero_identificacion) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT chk_ruta_horas CHECK (hora_fin IS NULL OR hora_inicio IS NULL OR hora_fin >= hora_inicio),
INDEX idx_ruta_vehiculo (vehiculo_placa),
INDEX idx_ruta_conductor (conductor_id),
INDEX idx_ruta_fecha (fecha)
) ENGINE = InnoDB DEFAULT CHARSET= utf8mb4 COMMENT= 'HOJAS DE RUTA DIARIAS';
CREATE TABLE IF NOT EXISTS ruta_paquete(
ruta_id INT NOT NULL,
paquete_tracking_id VARCHAR(20) NOT NULL,
resultado ENUM('PENDIENTE', 'ENTREGADO', 'DEVUELTO') NOT NULL DEFAULT 'PENDIENTE',
fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT pk_ruta_paquete PRIMARY KEY (ruta_id, paquete_tracking_id),
CONSTRAINT fk_rutapaquete_ruta FOREIGN KEY(ruta_id) REFERENCES ruta (id) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_rutapaquete_paquete FOREIGN KEY (paquete_tracking_id) REFERENCES paquete (tracking_id) ON UPDATE CASCADE ON DELETE RESTRICT,
INDEX idx_rutapaquete_paquete (paquete_tracking_id)
) ENGINE= InnoDB DEFAULT CHARSET= utf8mb4 COMMENT= 'Historial de que paquetes pasaron por cual ruta y su resultado';