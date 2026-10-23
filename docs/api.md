# Contrato de la API

Acuerdo entre frontend y backend. Si algo cambia, se cambia primero aquí y se avisa al otro.

- Base: `http://localhost:8080/api`
- Datos en JSON, nombres de campo en camelCase.
- Fechas como texto `"AAAA-MM-DD"`, por ejemplo `"2026-10-22"`.
- Errores: código `400`, `401` o `404` con `{"error": "mensaje"}`.

## Endpoints

| Método | Dirección | Para qué | Pantalla | Issue |
|---|---|---|---|---|
| POST | `/login` | Entrar con usuario y contraseña | Login | #1 |
| GET | `/dashboard` | Números del panel principal | Dashboard | #2 |
| GET | `/socios?buscar=ana` | Lista de socios, buscador opcional | Lista de socios | #3, #10 |
| GET | `/socios/{id}` | Un socio | Formulario de socio | #4, #10 |
| POST | `/socios` | Crear socio | Formulario de socio | #4, #10 |
| PUT | `/socios/{id}` | Editar socio | Formulario de socio | #4, #10 |
| DELETE | `/socios/{id}` | Eliminar socio | Lista de socios | #3, #10 |
| GET | `/planes` | Lista de planes | Planes | #5, #11 |
| POST | `/planes` | Crear plan | Planes | #5, #11 |
| PUT | `/planes/{id}` | Editar plan | Planes | #5, #11 |
| DELETE | `/planes/{id}` | Eliminar plan | Planes | #5, #11 |
| GET | `/membresias?socioId=1` | Membresías de un socio con su estado | Lista de socios | #12 |
| POST | `/pagos` | Registrar pago; crea o renueva la membresía | Registrar pago | #6, #13 |
| GET | `/pagos?socioId=1` | Historial de pagos de un socio | Registrar pago | #6, #13 |

## Objetos

### Socio

```json
{
  "id": 1,
  "dni": "40123456",
  "nombre": "Ana",
  "apellido": "Pérez",
  "email": "ana@mail.com",
  "telefono": "3001234567",
  "fechaRegistro": "2026-10-22",
  "estado": "ACTIVA"
}
```

Al crear o editar, el front envía solo `dni`, `nombre`, `apellido`, `email` y `telefono`. `fechaRegistro` la pone el back y `estado` se calcula.

### Plan

```json
{ "id": 1, "nombre": "Mensual", "duracionDias": 30, "precio": 25000 }
```

### Pago

Lo que envía el front:

```json
{ "socioId": 1, "planId": 1, "monto": 25000, "metodo": "EFECTIVO" }
```

Lo que devuelve el back:

```json
{
  "id": 7,
  "fecha": "2026-10-22",
  "monto": 25000,
  "metodo": "EFECTIVO",
  "membresia": { "fechaInicio": "2026-10-22", "fechaFin": "2026-11-21", "estado": "ACTIVA" }
}
```

### Login

```json
{ "usuario": "admin", "password": "1234" }
```

Responde `200` si es correcto y `401` si no.

### Dashboard

```json
{ "sociosActivos": 42, "membresiasVencidas": 5, "vencenEstaSemana": 3, "ingresosMes": 980000 }
```

## Valores fijos

| Campo | Valores |
|---|---|
| `estado` | `ACTIVA`, `VENCIDA` |
| `metodo` | `EFECTIVO`, `TARJETA`, `TRANSFERENCIA` |
