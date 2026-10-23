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

Cada vez que un socio contrata un plan. El estado (ACTIVA / VENCIDA) no se guarda: se calcula comparando `fecha_fin` con hoy.

| Columna | Tipo | Notas |
|---|---|---|
| id | INT | PK, AUTO_INCREMENT |
| socio_id | INT | FK → socio.id |
| plan_id | INT | FK → plan.id |
| fecha_inicio | DATE | |
| fecha_fin | DATE | fecha_inicio + duracion_dias del plan |

### pago

Cada cobro a un socio. Pertenece a una membresía.

| Columna | Tipo | Notas |
|---|---|---|
| id | INT | PK, AUTO_INCREMENT |
| membresia_id | INT | FK → membresia.id |
| fecha | DATE | |
| monto | DECIMAL(10,2) | Mismo tipo que plan.precio |
| metodo | VARCHAR(20) | EFECTIVO, TARJETA o TRANSFERENCIA |

### usuario

*Pendiente.*

## Relaciones

*Pendiente.*
