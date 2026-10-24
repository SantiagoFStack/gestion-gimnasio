let editandoId = null;   // null = creando uno nuevo; un número = editando ese plan

function dinero(valor) {
  return "$" + valor.toLocaleString("es-CO");
}

async function mostrarPlanes() {
  const lista = await getPlanes();
  const tbody = document.getElementById("tabla-planes");
  tbody.innerHTML = "";

  for (const p of lista) {
    tbody.innerHTML += `
      <tr>
        <td>${p.nombre}</td>
        <td>${p.duracionDias} días</td>
        <td>${dinero(p.precio)}</td>
        <td class="text-end">
          <button onclick="editar(${p.id})" class="btn btn-sm btn-outline-primary">Editar</button>
          <button onclick="borrar(${p.id})" class="btn btn-sm btn-outline-danger">Eliminar</button>
        </td>
      </tr>`;
  }
}

async function editar(id) {
  const plan = (await getPlanes()).find(p => p.id === id);
  editandoId = id;
  document.getElementById("titulo-form").textContent = "Editar plan";
  document.getElementById("nombre").value = plan.nombre;
  document.getElementById("duracionDias").value = plan.duracionDias;
  document.getElementById("precio").value = plan.precio;
}

async function borrar(id) {
  if (!confirm("¿Eliminar este plan?")) return;
  await eliminarPlan(id);
  mostrarPlanes();
}

function limpiarFormulario() {
  editandoId = null;
  document.getElementById("form-plan").reset();
  document.getElementById("titulo-form").textContent = "Nuevo plan";
}

document.getElementById("form-plan").addEventListener("submit", async (e) => {
  e.preventDefault();

  const datos = {
    nombre: document.getElementById("nombre").value.trim(),
    duracionDias: Number(document.getElementById("duracionDias").value),
    precio: Number(document.getElementById("precio").value)
  };

  if (editandoId === null) {
    await crearPlan(datos);
  } else {
    await editarPlan(editandoId, datos);
  }

  limpiarFormulario();
  mostrarPlanes();
});

document.getElementById("cancelar").addEventListener("click", limpiarFormulario);

mostrarPlanes();