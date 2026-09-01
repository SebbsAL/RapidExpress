# REQUIREMENTS

# Sistema de Gestión de Flotas y Rutas “RapidExpress”

## 1. Introducción

La empresa de logística y mensajería **“RapidExpress”** se ha consolidado en el mercado nacional gracias a su compromiso con la puntualidad y la seguridad en la entrega de paquetería. Sin embargo, el crecimiento acelerado de sus operaciones ha puesto en evidencia las limitaciones de sus procesos manuales, generando ineficiencias, costos operativos elevados y dificultades para mantener la calidad del servicio.

El presente proyecto tiene como objetivo el desarrollo de un **sistema de información de backend**, operado exclusivamente a través de una **interfaz de línea de comandos (CLI)**, para automatizar y optimizar la gestión integral de su flota de vehículos, el personal de conducción, la planificación de rutas y el seguimiento de paquetes.

---

## 2. Contexto del Problema

Actualmente, la asignación de rutas y el seguimiento de los envíos en “RapidExpress” dependen de hojas de cálculo y comunicación manual entre los operadores de logística y los conductores. Este método es propenso a errores humanos, retrasa la comunicación de incidencias y no ofrece una visibilidad en tiempo real del estado de las entregas. La falta de un sistema centralizado dificulta la planificación de mantenimientos preventivos para los vehículos y la evaluación del desempeño de los conductores.

La dirección de “RapidExpress” requiere una solución de software robusta que centralice la información, automatice las tareas repetitivas y proporcione datos fiables para la toma de decisiones estratégicas.

---

## 3. Descripción del Sistema a Desarrollar

### Módulo 1: Gestión de la Flota de Vehículos

Administración completa del parque automotor de la empresa:
- **Registro de vehículos:** Placa, marca, modelo, año de fabricación y capacidad máxima de carga (en kg).
- **Actualización y consulta de estados:**
  - `Disponible`
  - `En Ruta`
  - `En Mantenimiento`
- **Gestión de mantenimientos:** Programación y registro de mantenimientos realizados a cada vehículo. El historial de mantenimiento debe persistirse de forma individual para consultas futuras.

### Módulo 2: Gestión de Personal (Conductores)

Administración de la información de los conductores:
- **Registro, actualización y consulta de datos:** Número de identificación, nombre completo, tipo de licencia y número de contacto.
- **Gestión de estados del conductor:**
  - `Activo`
  - `De Vacaciones`
  - `Inactivo`
- **Asignación de vehículos:** Asignar un conductor a un vehículo `Disponible`. Un conductor **no** puede estar asignado a más de un vehículo simultáneamente.

### Módulo 3: Gestión de Paquetes y Envíos

Núcleo de la operación logística:
- **Registro de paquetes:** Generación de un identificador único de seguimiento (tracking ID). Datos: descripción del contenido, peso, dimensiones, dirección de origen y dirección de destino.
- **Asociación:** Vincular cada paquete con remitente y destinatario.
- **Ciclo de vida / Estados del paquete:**
  - `En Bodega`
  - `Asignado a Ruta`
  - `En Tránsito`
  - `Entregado`
  - `Devuelto`

### Módulo 4: Planificación y Seguimiento de Rutas

Logística y distribución:
- **Creación de hoja de ruta diaria:** Asignar un conjunto de paquetes (`En Bodega`) a un vehículo y conductor correspondientes. **Validar que no se exceda la capacidad máxima de carga del vehículo.**
- **Inicio de ruta:** Cambia automáticamente el estado del vehículo y conductor a `En Ruta`, y los paquetes a `En Tránsito`.
- **Monitoreo de rutas activas:** Permitir actualización de estados de paquetes (ej. a `Entregado`) mientras la ruta está en curso, reflejando consistencia para consultas concurrentes en CLI.

### Módulo 5: Reportes y Auditoría

Trazabilidad y soporte a decisiones:
- **Reportes operativos:** Consultas como entregas por conductor en un rango de fechas, historial de rutas de un vehículo, etc.
- **Registro de auditoría (Logging):** Archivo de texto centralizado que registre operaciones críticas (creación de paquetes, inicio/fin de ruta, cambios de estado clave con timestamps).

---

## 4. Resultado Esperado y Requerimientos Técnicos

### 4.1. Estructura del Proyecto (Maven)

Estructura estándar de Maven bajo arquitectura MVC:

```
rapidexpress-management-system/
├── .gitignore
├── README.md
├── database/
│   ├── diagrama_entidad_relacion.png
│   ├── 1_schema_ddl.sql
│   └── 2_data_dml.sql
└── src/
    └── main/
        └── java/
            └── com/
                └── rapidexpress/
                    ├── controller/
                    ├── model/
                    │   ├── dao/ / repository/
                    │   └── entity/
                    ├── service/
                    ├── view/ / cli/
                    └── util/ / config/
```

### 4.2. Arquitectura y Persistencia

- **Patrón:** Modelo-Vista-Controlador (MVC).
- **Base de Datos:** MySQL

### 4.3. Entregables de Base de Datos

- **Diagrama Entidad-Relación (ERD):** Imagen `diagrama_entidad_relacion.png`.
- **`1_schema_ddl.sql`:** Sentencias `CREATE TABLE`, claves primarias, claves foráneas, índices y restricciones.
- **`2_data_dml.sql`:** Sentencias `INSERT` con al menos **20 registros significativos por entidad principal**.

### 4.4. Documentación (`README.md`)

Debe incluir:
1. Título del Proyecto.
2. Descripción del Proyecto.
3. Tecnologías Utilizadas.
4. Diseño de la Base de Datos (explicación + ERD).
5. Instalación y Ejecución (configuración DB nube, variables de entorno, scripts SQL, ejecución Maven/CLI).
6. Guía de Uso del CLI.
7. Autores.
