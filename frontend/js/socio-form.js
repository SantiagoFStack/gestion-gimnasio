// Lee el id de la dirección: socio-form.html?id=3  →  3
const params = new URLSearchParams(window.location.search);
const id = params.get("id") ? Number(params.get("id")) : null;

const campos = ["dni", "nombre", "apellido", "email", "telefono"];

async function cargarSiEsEdicion() {
  if (id === null) return;   // es un socio nuevo: no hay nada que cargar

  document.getElementById("titulo").textContent = "Editar socio";
  const socio = await getSocio(id);
  for (const c of campos) {
    document.getElementById(c).value = socio[c] ?? "";
  }
}

document.getElementById("form-socio").addEventListener("submit", async (e) => {
  e.preventDefault();

  // Junta lo escrito en un objeto: { dni: "...", nombre: "...", ... }
  const datos = {};
  for (const c of campos) {
    datos[c] = document.getElementById(c).value.trim();
  }

  try {
    if (id === null) {
      await crearSocio(datos);
    } else {
      await editarSocio(id, datos);
    }
    window.location.href = "socios.html";
  } catch (err) {
    document.getElementById("error").textContent = err.message;
  }
});

cargarSiEsEdicion();