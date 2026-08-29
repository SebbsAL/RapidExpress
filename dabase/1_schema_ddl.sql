-- ==========================================================
-- RapidExpress - Script DDL (Data Definition Language)
-- ==========================================================
CREATE DATABASE IF NOT EXISTS rapidexpress_db;
USE rapidexpress_db;

-- 1. Tabla: vehiculos
CREATE TABLE vehiculos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio_fabricacion INT NOT NULL CHECK (anio_fabricacion >= 1990),
    capacidad_maxima_kg DECIMAL(10,2) NOT NULL CHECK (capacidad_maxima_kg > 0),
    estado ENUM('DISPONIBLE', 'EN_RUTA', 'EN_MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Tabla: conductores
CREATE TABLE conductores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    tipo_licencia VARCHAR(10) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    estado ENUM('ACTIVO', 'DE_VACACIONES', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Tabla: mantenimientos
CREATE TABLE mantenimientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id INT NOT NULL,
    tipo_mantenimiento VARCHAR(30) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_programada DATE NOT NULL,
    fecha_realizacion DATE,
    costo DECIMAL(10,2) CHECK (costo >= 0),
    estado ENUM('PROGRAMADO', 'EN_PROCESO', 'COMPLETADO', 'CANCELADO') NOT NULL DEFAULT 'PROGRAMADO',
    observaciones TEXT,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id) ON DELETE RESTRICT
);

-- 4. Tabla: asignaciones_vehiculo_conductor
CREATE TABLE asignaciones_vehiculo_conductor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id INT NOT NULL,
    conductor_id INT NOT NULL,
    fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_desasignacion DATETIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id),
    FOREIGN KEY (conductor_id) REFERENCES conductores(id)
);

-- 5. Tabla: clientes (Remitentes y Destinatarios)
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    direccion VARCHAR(200) NOT NULL,
    ciudad VARCHAR(50) NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 6. Tabla: paquetes
CREATE TABLE paquetes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_seguimiento VARCHAR(30) NOT NULL UNIQUE,
    descripcion_contenido TEXT NOT NULL,
    peso_kg DECIMAL(8,2) NOT NULL CHECK (peso_kg > 0),
    largo_cm DECIMAL(6,2) CHECK (largo_cm > 0),
    ancho_cm DECIMAL(6,2) CHECK (ancho_cm > 0),
    alto_cm DECIMAL(6,2) CHECK (alto_cm > 0),
    volumen_m3 DECIMAL(8,4) CHECK (volumen_m3 > 0),
    direccion_origen TEXT NOT NULL,
    direccion_destino TEXT NOT NULL,
    remitente_id INT NOT NULL,
    destinatario_id INT NOT NULL,
    estado ENUM('EN_BODEGA', 'ASIGNADO_A_RUTA', 'EN_TRANSITO', 'ENTREGADO', 'DEVUELTO') NOT NULL DEFAULT 'EN_BODEGA',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (remitente_id) REFERENCES clientes(id),
    FOREIGN KEY (destinatario_id) REFERENCES clientes(id)
);

-- 7. Tabla: historial_paquetes
CREATE TABLE historial_paquetes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paquete_id INT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    descripcion_evento VARCHAR(255) NOT NULL,
    ubicacion VARCHAR(100),
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paquete_id) REFERENCES paquetes(id) ON DELETE CASCADE
);

-- 8. Tabla: rutas (Hojas de ruta)
CREATE TABLE rutas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_ruta VARCHAR(30) NOT NULL UNIQUE,
    vehiculo_id INT NOT NULL,
    conductor_id INT NOT NULL,
    fecha_ruta DATE NOT NULL,
    hora_inicio TIME,
    hora_fin TIME,
    peso_total_asignado_kg DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado ENUM('PLANIFICADA', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA') NOT NULL DEFAULT 'PLANIFICADA',
    observaciones TEXT,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id),
    FOREIGN KEY (conductor_id) REFERENCES conductores(id)
);

-- 9. Tabla: ruta_paquetes
CREATE TABLE ruta_paquetes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ruta_id INT NOT NULL,
    paquete_id INT NOT NULL,
    orden_entrega INT NOT NULL DEFAULT 1,
    estado_entrega ENUM('PENDIENTE', 'ENTREGADO', 'DEVUELTO', 'INCIDENCIA') NOT NULL DEFAULT 'PENDIENTE',
    fecha_entrega_estimada DATETIME,
    fecha_entrega_real DATETIME,
    observaciones_entrega TEXT,
    FOREIGN KEY (ruta_id) REFERENCES rutas(id) ON DELETE CASCADE,
    FOREIGN KEY (paquete_id) REFERENCES paquetes(id) ON DELETE RESTRICT
);

-- 10. Tabla: auditoria_logs
CREATE TABLE auditoria_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    entidad VARCHAR(50) NOT NULL,
    entidad_id INT,
    accion VARCHAR(50) NOT NULL,
    detalle TEXT NOT NULL,
    usuario_o_proceso VARCHAR(50) NOT NULL DEFAULT 'CLI_USER',
    fecha_evento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================================
-- Creación de Índices para Rendimiento
-- ==========================================================
CREATE INDEX idx_paquetes_tracking ON paquetes(codigo_seguimiento);
CREATE INDEX idx_paquetes_estado ON paquetes(estado);
CREATE INDEX idx_paquetes_remitente ON paquetes(remitente_id);
CREATE INDEX idx_paquetes_destinatario ON paquetes(destinatario_id);

CREATE INDEX idx_vehiculos_placa ON vehiculos(placa);
CREATE INDEX idx_vehiculos_estado ON vehiculos(estado);
CREATE INDEX idx_mantenimientos_vehiculo ON mantenimientos(vehiculo_id, fecha_programada);

CREATE INDEX idx_conductores_doc ON conductores(numero_identificacion);
CREATE INDEX idx_conductores_estado ON conductores(estado);

CREATE INDEX idx_rutas_fecha ON rutas(fecha_ruta);
CREATE INDEX idx_rutas_conductor_fecha ON rutas(conductor_id, fecha_ruta);
CREATE INDEX idx_rutas_vehiculo_fecha ON rutas(vehiculo_id, fecha_ruta);
CREATE INDEX idx_rutas_estado ON rutas(estado);

CREATE INDEX idx_ruta_paquetes_ruta ON ruta_paquetes(ruta_id);
CREATE INDEX idx_ruta_paquetes_paquete ON ruta_paquetes(paquete_id);

-- ==========================================================
-- Creación de Vistas para Reportes
-- ==========================================================
CREATE OR REPLACE VIEW vista_reporte_entregas_conductor AS
SELECT 
    c.id AS conductor_id,
    c.numero_identificacion,
    c.nombre_completo AS conductor_nombre,
    r.id AS ruta_id,
    r.codigo_ruta,
    r.fecha_ruta,
    p.codigo_seguimiento,
    p.descripcion_contenido,
    p.peso_kg,
    rp.estado_entrega,
    rp.fecha_entrega_real
FROM conductores c
JOIN rutas r ON c.id = r.conductor_id
JOIN ruta_paquetes rp ON r.id = rp.ruta_id
JOIN paquetes p ON rp.paquete_id = p.id;

CREATE OR REPLACE VIEW vista_historial_vehiculos AS
SELECT 
    v.id AS vehiculo_id,
    v.placa,
    v.marca,
    v.modelo,
    v.capacidad_maxima_kg,
    v.estado AS estado_actual,
    COUNT(DISTINCT r.id) AS total_rutas_realizadas,
    COALESCE(SUM(r.peso_total_asignado_kg), 0) AS total_kg_transportados,
    COUNT(DISTINCT m.id) AS total_mantenimientos_registrados,
    COALESCE(SUM(m.costo), 0) AS costo_total_mantenimiento
FROM vehiculos v
LEFT JOIN rutas r ON v.id = r.vehiculo_id AND r.estado = 'COMPLETADA'
LEFT JOIN mantenimientos m ON v.id = m.vehiculo_id AND m.estado = 'COMPLETADO'
GROUP BY v.id, v.placa, v.marca, v.modelo, v.capacidad_maxima_kg, v.estado;
