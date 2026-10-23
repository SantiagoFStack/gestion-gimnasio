# Modelo de datos

Issue #7. Base de datos MySQL del sistema de gestión del gimnasio.

## Tablas

### socio

Personas inscritas en el gimnasio.

| Columna | Tipo | Notas |
|---|---|---|
| id | INT | PK, AUTO_INCREMENT |
| dni | VARCHAR(15) | UNIQUE, no puede repetirse |
| nombre | VARCHAR(50) | |
| apellido | VARCHAR(50) | |
| email | VARCHAR(100) | |
| telefono | VARCHAR(20) | Texto: un celular de 10 dígitos no cabe en INT |
| fecha_registro | DATE | Día en que se inscribió |

### plan

Planes que vende el gimnasio (Mensual, Trimestral, Anual...).

| Columna | Tipo | Notas |
|---|---|---|
| id | INT | PK, AUTO_INCREMENT |
| nombre | VARCHAR(50) | UNIQUE, dos planes no pueden llamarse igual |
| duracion_dias | INT | Cantidad de días, por ejemplo 30 |
| precio | DECIMAL(10,2) | |

### membresia

*Pendiente.*

### pago

*Pendiente.*

### usuario

*Pendiente.*

## Relaciones

*Pendiente.*
