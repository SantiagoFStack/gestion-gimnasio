// =====================================================
// api.js — todas las llamadas al back pasan por aquí.
// Versión real: habla con la API del backend (Java) publicada en Render.
// =====================================================

// La API en internet. Para probar con la API encendida en tu PC (api.Servidor)
// cambia esta línea por: const URL_API = "http://localhost:8080/api";
const URL_API = "https://gimnasio-api-imsn.onrender.com/api";

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
    throw new Error("No se pudo conectar con el servidor. Si lleva un rato sin usarse, espera un minuto y recarga.");
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
