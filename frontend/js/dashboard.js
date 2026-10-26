function dinero(valor) {
  return "$" + Number(valor ?? 0).toLocaleString("es-CO");
}

async function cargarDashboard() {
  try {
    const datos = await getDashboard();

    document.getElementById("activos").textContent = datos.sociosActivos ?? 0;
    document.getElementById("vencidas").textContent = datos.membresiasVencidas ?? 0;
    document.getElementById("por-vencer").textContent = datos.vencenEstaSemana ?? 0;
    document.getElementById("ingresos").textContent = dinero(datos.ingresosMes);
  } catch (err) {
    document.querySelector(".contenido").insertAdjacentHTML(
      "beforeend",
      `<div class="alert alert-danger mt-3">${err.message}</div>`
    );
  }
}

cargarDashboard();