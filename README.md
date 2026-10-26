# Gestión de Gimnasio

Sistema web para gestionar las membresías de un gimnasio: socios, planes, pagos y vencimientos.

![Java](https://img.shields.io/badge/Java-21-orange)
![JDBC](https://img.shields.io/badge/JDBC-MySQL-4479a1)
![Frontend](https://img.shields.io/badge/HTML%20%C2%B7%20CSS%20%C2%B7%20JS-Bootstrap%205-7952b3)
![Estado](https://img.shields.io/badge/estado-en%20desarrollo-yellow)

**👉 Web: https://santiagofstack.github.io/gestion-gimnasio/** · usuario de prueba `admin` / `admin123`

> La primera visita del día puede tardar hasta 1 minuto: la API está en un plan gratuito que se duerme si nadie la usa.

## Problemática

Muchos gimnasios pequeños llevan los socios y los pagos a mano, en cuadernos o en hojas de cálculo. Eso trae tres problemas:

- **No se sabe rápido quién tiene la cuota vencida.** Hay que revisar fechas una por una, y muchos socios siguen entrenando sin pagar.
- **Se pierden cobros y datos.** Un pago apuntado mal o un cuaderno perdido significa dinero que no se reclama.
- **No hay una visión del negocio.** Saber cuántos socios activos hay o cuánto se ingresó en el mes exige sumar a mano.

## Solución

Una web que usa el personal del gimnasio para:

- **Dar de alta socios y planes** (Mensual, Trimestral, Anual…), con validaciones como el DNI único.
- **Registrar pagos.** Cada pago crea la membresía y **calcula solo la fecha de vencimiento**. Si el socio aún estaba al día, la nueva empieza cuando vence la anterior, para que no pierda días.
- **Ver al momento el estado de cada socio**: `ACTIVA` o `VENCIDA`.
- **Consultar un panel** con socios activos, vencidos, los que vencen esta semana y los ingresos del mes.

Solo entra el personal: la web pide usuario y contraseña, y la API rechaza cualquier petición sin sesión iniciada.

## Funcionalidades

- [x] Inicio de sesión del personal, con contraseña cifrada (BCrypt) y sesión por token
- [x] Alta, edición, búsqueda y baja de socios
- [x] Gestión de planes
- [x] Registro de pagos con cálculo automático del vencimiento
- [x] Estado de cada socio: `ACTIVA` o `VENCIDA`
- [x] Panel con socios activos, vencidos, que vencen esta semana e ingresos del mes (API lista)
- [ ] Pantallas de Dashboard, Lista de socios y Registrar pago (frontend en curso)

## Cómo funciona

```
Navegador ──► Web (GitHub Pages) ──► API Java (Render) ──► Base de datos MySQL (Aiven)
              HTML · CSS · JS          JDBC · HttpServer         5 tablas
```

| Pieza | Tecnología | Dónde está |
|---|---|---|
| Frontend | HTML, CSS, JavaScript (`fetch`), Bootstrap 5 | [GitHub Pages](https://santiagofstack.github.io/gestion-gimnasio/) |
| API | Java 21, JDBC, `HttpServer` de Java, Gson, BCrypt, Maven | [Render](https://gimnasio-api-imsn.onrender.com/api/salud) (Docker) |
| Base de datos | MySQL 8 | Aiven (plan gratuito) |
| Desarrollo | VS Code, NetBeans, XAMPP (MySQL local), Git y GitHub | — |

Cada cambio que llega a `main` se publica solo: el frontend con GitHub Actions y la API con Render. Otra tarea de GitHub Actions llama a la API cada 10 minutos para que no se duerma.

## Estructura del repositorio

```
gestion-gimnasio/
├── backend/
│   ├── bd/gimnasio.sql          Crea la base de datos con datos de prueba
│   ├── Dockerfile               Cómo Render construye y arranca la API
│   └── src/main/java/
│       ├── conexion/            Conexión a la base de datos (JDBC)
│       ├── modelo/              Socio, Plan, Membresia, Pago, Usuario, Dashboard
│       ├── dao/                 Consultas SQL de cada tabla
│       ├── api/                 Servidor HTTP y rutas de la API
│       └── Test*.java           Programas de prueba de cada parte
├── frontend/                    Pantallas HTML, CSS y JS
├── docs/                        Documentación (abajo)
├── .github/workflows/           Publicar el front y despertador de la API
└── render.yaml                  Configuración del servicio en Render
```

## Documentación

- [Modelo de datos](docs/modelo-datos.md): tablas, columnas, relaciones y diagrama entidad-relación.
- [Contrato de la API](docs/api.md): endpoints, JSON y sesión con token.
- [Conectar front y back](docs/conectar-front-con-back.md): cómo trabaja el front con la API.
- [Publicar el frontend](docs/publicar-frontend.md): GitHub Pages.

## Cómo ejecutarlo en tu PC

**Requisitos:** Java 21 (JDK), Maven (o VS Code con *Extension Pack for Java*) y XAMPP.

1. **Base de datos:** arranca MySQL en XAMPP y ejecuta `backend/bd/gimnasio.sql` en phpMyAdmin (pestaña SQL).
2. **API:** abre `backend/src/main/java/api/Servidor.java` y pulsa **Run**. Queda en `http://localhost:8080/api`.
   - Sin más configuración usa el MySQL de XAMPP.
   - Para usar la base en la nube, copia `backend/src/main/resources/conexion.properties.example` como `conexion.properties` y pon la clave. Ese archivo no se sube a GitHub.
3. **Web:** en `frontend/js/api.js` cambia `URL_API` por `"http://localhost:8080/api"` y abre `frontend/index.html` con **Live Server**. Entra con `admin` / `admin123`.

Para probar solo el backend hay programas con `main`: `Testconnection`, `TestSocios`, `TestPlanes`, `TestMembresias`, `TestPagos`, `TestLogin` y `TestDashboard`.

## Cómo trabajamos

- Cada tarea es un [issue](../../issues), con etiqueta `frontend`, `backend` o `documentacion`, y está agrupada en [hitos](../../milestones) por fase.
- **Una rama por tarea.** Se une a `main` con un Pull Request que la cierra con `Closes #N`.
- Front y back se ponen de acuerdo en el [contrato de la API](docs/api.md).

## Equipo

| Integrante | Rol | Qué hizo |
|---|---|---|
| [@SantiagoFStack](https://github.com/SantiagoFStack) | Backend | Modelo de datos, base de datos, conexión JDBC, CRUD de socios y planes, membresías, pagos, login, dashboard, API REST, seguridad y publicación en Aiven y Render |
| [@hasbleidy](https://github.com/hasbleidy) | Frontend | Pantallas de login, planes y formulario de socio; dashboard, lista de socios y pagos en curso |
