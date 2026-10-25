# Conectar el frontend con el backend (issue #18)

El backend ya tiene una **API**: un pequeño servidor en Java que recibe las peticiones del front y las guarda en la base de datos. Para conectarlos solo hay que **arrancar la API** y **cambiar `frontend/js/api.js`** por la versión real. Las pantallas no se tocan.

```
Pantalla (HTML/JS)  →  js/api.js  →  fetch http://localhost:8080/api/...  →  API Java  →  Base de datos (Aiven)
```

## 1. Preparar el PC (una sola vez)

1. Instalar **Java 21** (JDK): https://adoptium.net → *Temurin 21 (LTS)*.
2. En VS Code, instalar la extensión **Extension Pack for Java** (de Microsoft).
3. Bajar lo último del repo: `git switch main` y `git pull`.
4. Pedirle a Santiago el archivo **`conexion.properties`** (por WhatsApp o correo, **nunca por GitHub**: tiene la contraseña de la base) y guardarlo en:
   ```
   backend/src/main/resources/conexion.properties
   ```
   Con ese archivo, la API usa la base en la nube, la misma que usa Santiago.

## 2. Arrancar la API

1. En VS Code abrir `backend/src/main/java/api/Servidor.java`.
2. Pulsar **Run** encima de `public static void main`.
3. En la terminal debe salir:
   ```
   API del gimnasio funcionando en http://localhost:8080/api
   Base de datos: gimnasio (en la nube, Aiven)
   ```
4. Comprobar en el navegador: http://localhost:8080/api/planes debe mostrar los planes en JSON.

La API tiene que **quedarse encendida** mientras se usa la web. Para pararla: botón ⏹ de VS Code o **Ctrl + C** en su terminal.

## 3. Cambiar `api.js` por la versión real

1. Crear la rama de la tarea: `git switch -c conectar-front`
2. Reemplazar **todo** el contenido de `frontend/js/api.js` por este:

```js
// =====================================================
// api.js — versión real: habla con la API del backend (Java).
// El backend tiene que estar encendido (api.Servidor).
// =====================================================

const URL_API = "http://localhost:8080/api";

// Hace la petición y devuelve el JSON, o lanza un error con el mensaje del back
async function pedir(metodo, ruta, datos) {
  let respuesta;
  try {
    respuesta = await fetch(URL_API + ruta, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: datos ? JSON.stringify(datos) : undefined
    });
  } catch (e) {
    throw new Error("No se pudo conectar con el servidor. ¿Está encendido el backend?");
  }

  if (!respuesta.ok) {
    const error = await respuesta.json().catch(() => ({}));
    throw new Error(error.error || "Error del servidor (" + respuesta.status + ")");
  }

  const texto = await respuesta.text();   // DELETE no devuelve nada
  return texto ? JSON.parse(texto) : null;
}

// ---------- Login y dashboard ----------
async function login(usuario, password) { return pedir("POST", "/login", { usuario, password }); }
async function getDashboard()          { return pedir("GET", "/dashboard"); }

// ---------- Socios ----------
async function getSocios(buscar = "")  { return pedir("GET", "/socios?buscar=" + encodeURIComponent(buscar)); }
async function getSocio(id)            { return pedir("GET", "/socios/" + id); }
async function crearSocio(datos)       { return pedir("POST", "/socios", datos); }
async function editarSocio(id, datos)  { return pedir("PUT", "/socios/" + id, datos); }
async function eliminarSocio(id)       { return pedir("DELETE", "/socios/" + id); }

// ---------- Planes ----------
async function getPlanes()             { return pedir("GET", "/planes"); }
async function crearPlan(datos)        { return pedir("POST", "/planes", datos); }
async function editarPlan(id, datos)   { return pedir("PUT", "/planes/" + id, datos); }
async function eliminarPlan(id)        { return pedir("DELETE", "/planes/" + id); }

// ---------- Pagos y membresías ----------
async function registrarPago(datos)    { return pedir("POST", "/pagos", datos); }
async function getPagos(socioId)       { return pedir("GET", "/pagos?socioId=" + socioId); }
async function getMembresias(socioId)  { return pedir("GET", "/membresias?socioId=" + socioId); }
```

Las funciones se llaman **igual** que las de los datos falsos y devuelven lo mismo, por eso las pantallas siguen funcionando sin cambios.

## 4. Probar

Abrir `frontend/index.html` con **Live Server** y entrar con **admin / admin123**.

- [ ] El login entra con admin / admin123 y falla con otra contraseña
- [ ] Planes muestra Mensual, Trimestral y Anual (vienen de la base)
- [ ] Un socio o plan creado **sigue ahí al recargar** la página (ahora se guarda de verdad)
- [ ] Crear un socio con un DNI repetido muestra "Ya existe un socio con el DNI ..."

Ya probado con las pantallas actuales: **login, planes y formulario de socio funcionan con la API**.

## 5. Lo que falta en el front

Estos archivos están vacíos en el repo; hay que completarlos siguiendo la *Guía del frontend* (las funciones de `api.js` ya existen):

| Archivo | Qué falta | Funciones de `api.js` que usa |
|---|---|---|
| `js/dashboard.js` | Rellenar las 4 tarjetas (ahora salen "–") | `getDashboard()` |
| `socios.html` | La tabla de socios con buscador | `getSocios(buscar)`, `eliminarSocio(id)` |
| `pago.html` y `js/pago.js` | Registrar pago e historial | `getSocios()`, `getPlanes()`, `registrarPago(datos)`, `getPagos(socioId)` |

## 6. Subir el trabajo

```bash
git add frontend
git commit -m "Front conectado con la API. Closes #18"
git push -u origin conectar-front
```

Y abrir el Pull Request en GitHub.

## Problemas comunes

| Qué pasa | Por qué | Solución |
|---|---|---|
| "No se pudo conectar con el servidor" | La API no está encendida | Arrancar `api.Servidor` (paso 2) |
| En la consola (F12): `CORS` | Se abrió el HTML con doble clic (`file://`) | Abrirlo con Live Server |
| "No se pudo conectar a la base de datos" | Falta `conexion.properties`, o la base de Aiven se apagó por inactividad | Revisar el archivo; Santiago la enciende en console.aiven.io (*Power on*) |
| Un campo sale `undefined` | El nombre del campo no coincide con el contrato | Revisar `docs/api.md` (`duracionDias`, `fechaRegistro`...) |
| La web de GitHub Pages no guarda nada | La API corre en el PC, no en internet | Para que la web publicada guarde datos, la API tiene que estar encendida en el PC de quien la usa |

## Endpoints

Todos los endpoints, los datos que reciben y lo que devuelven están en [`docs/api.md`](api.md).
