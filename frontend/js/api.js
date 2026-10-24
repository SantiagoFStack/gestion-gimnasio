// =====================================================
// api.js — todas las llamadas al back pasan por aquí.
// Por ahora son datos falsos. En el #18 se cambian por fetch.
// =====================================================

let socios = [
  { id: 1, dni: "1012345678", nombre: "Ana", apellido: "Pérez",
    email: "ana.perez@mail.com", telefono: "3001234567",
    fechaRegistro: "2026-09-01", estado: "ACTIVA" },
  { id: 2, dni: "1023456789", nombre: "Carlos", apellido: "Gómez",
    email: "carlos.gomez@mail.com", telefono: "3109876543",
    fechaRegistro: "2026-09-01", estado: "ACTIVA" },
  { id: 3, dni: "1034567890", nombre: "Laura", apellido: "Martínez",
    email: "laura.martinez@mail.com", telefono: "3155551234",
    fechaRegistro: "2026-08-15", estado: "VENCIDA" }
];

let planes = [
  { id: 1, nombre: "Mensual", duracionDias: 30, precio: 80000 },
  { id: 2, nombre: "Trimestral", duracionDias: 90, precio: 220000 },
  { id: 3, nombre: "Anual", duracionDias: 365, precio: 800000 }
];

let pagos = [];

function hoy() {
  return new Date().toISOString().slice(0, 10);   // "2026-10-22"
}

// ---------- Login ----------
async function login(usuario, password) {
  if (usuario === "admin" && password === "admin123") {
    return { id: 1, usuario: "admin", rol: "ADMIN" };
  }
  throw new Error("Usuario o contraseña incorrectos");
}

// ---------- Dashboard ----------
async function getDashboard() {
  return {
    sociosActivos: 2,
    membresiasVencidas: 1,
    vencenEstaSemana: 1,
    ingresosMes: 160000
  };
}

// ---------- Socios ----------
async function getSocios(buscar = "") {
  const texto = buscar.toLowerCase();
  return socios.filter(s =>
    s.nombre.toLowerCase().includes(texto) ||
    s.apellido.toLowerCase().includes(texto) ||
    s.dni.includes(texto)
  );
}

async function getSocio(id) {
  return socios.find(s => s.id === id);
}

async function crearSocio(datos) {
  if (socios.some(s => s.dni === datos.dni)) {
    throw new Error("Ya existe un socio con ese DNI");
  }
  const nuevo = { ...datos, id: Date.now(), fechaRegistro: hoy(), estado: "VENCIDA" };
  socios.push(nuevo);
  return nuevo;
}

async function editarSocio(id, datos) {
  const socio = socios.find(s => s.id === id);
  Object.assign(socio, datos);
  return socio;
}

async function eliminarSocio(id) {
  socios = socios.filter(s => s.id !== id);
}

// ---------- Planes ----------
async function getPlanes() {
  return planes;
}

async function crearPlan(datos) {
  const nuevo = { ...datos, id: Date.now() };
  planes.push(nuevo);
  return nuevo;
}

async function editarPlan(id, datos) {
  const plan = planes.find(p => p.id === id);
  Object.assign(plan, datos);
  return plan;
}

async function eliminarPlan(id) {
  planes = planes.filter(p => p.id !== id);
}

// ---------- Pagos ----------
async function registrarPago(datos) {
  const plan = planes.find(p => p.id === datos.planId);
  const fin = new Date();
  fin.setDate(fin.getDate() + plan.duracionDias);
  const pago = {
    id: Date.now(),
    socioId: datos.socioId,
    fecha: hoy(),
    monto: datos.monto,
    metodo: datos.metodo,
    membresia: {
      fechaInicio: hoy(),
      fechaFin: fin.toISOString().slice(0, 10),
      estado: "ACTIVA"
    }
  };
  pagos.push(pago);
  return pago;
}

async function getPagos(socioId) {
  return pagos.filter(p => p.socioId === socioId);
}