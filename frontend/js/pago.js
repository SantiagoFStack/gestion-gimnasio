let listaPlanes = [];

function dinero(valor) {
  return "$" + Number(valor ?? 0).toLocaleString("es-CO");
}

function mostrarMensaje(html, tipo) {
  document.getElementById("resultado").innerHTML =
    `<div class="alert alert-${tipo} mb-0">${html}</div>`;
}

async function llenarListas() {
  try {
    const socios = await getSocios();
    listaPlanes = await getPlanes();

    document.getElementById("socio").innerHTML =
      socios.map(s => `<option value="${s.id}">${s.nombre} ${s.apellido} — ${s.dni}</option>`).join("");

    document.getElementById("plan").innerHTML =
      listaPlanes.map(p => `<option value="${p.id}">${p.nombre} (${p.duracionDias} días)</option>`).join("");

    ponerMonto();
    mostrarHistorial();
  } catch (err) {
    mostrarMensaje(err.message, "danger");
  }
}

function ponerMonto() {
  const planId = Number(document.getElementById("plan").value);
  const plan = listaPlanes.find(p => p.id === planId);
  document.getElementById("monto").value = plan ? plan.precio : "";
}

async function mostrarHistorial() {
  const socioId = Number(document.getElementById("socio").value);
  const tbody = document.getElementById("historial");
  if (!socioId) { tbody.innerHTML = ""; return; }

  try {
    const lista = await getPagos(socioId);
    tbody.innerHTML = lista.length === 0
      ? `<tr><td colspan="4" class="text-muted">Este socio aún no tiene pagos</td></tr>`
      : lista.map(p => `
        <tr>
          <td>${p.fecha}</td>
          <td>${dinero(p.monto)}</td>
          <td>${p.metodo}</td>
          <td>${p.membresia?.fechaFin ?? ""}</td>
        </tr>`).join("");
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="4" class="text-danger">${err.message}</td></tr>`;
  }
}

document.getElementById("plan").addEventListener("change", ponerMonto);
document.getElementById("socio").addEventListener("change", () => {