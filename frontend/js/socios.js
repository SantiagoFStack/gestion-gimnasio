async function mostrarSocios() {
  const buscar = document.getElementById("buscador").value;
  const lista = await getSocios(buscar);
  const tbody = document.getElementById("tabla-socios");
  tbody.innerHTML = "";   // vacía la tabla antes de llenarla

  for (const s of lista) {
    const color = s.estado === "ACTIVA" ? "bg-success" : "bg-danger";
    tbody.innerHTML += `
      <tr>
        <td>${s.dni}</td>
        <td>${s.nombre} ${s.apellido}</td>
        <td>${s.telefono ?? ""}</td>
        <td><span class="badge ${color}">${s.estado}</span></td>
        <td class="text-end">
          <a href="socio-form.html?id=${s.id}" class="btn btn-sm btn-outline-primary">Editar</a>
          <button onclick="borrar(${s.id})" class="btn btn-sm btn-outline-danger">Eliminar</button>
        </td>
      </tr>`;
  }
}

async function borrar(id) {
  if (!confirm("¿Seguro que quieres eliminar este socio?")) return;
  await eliminarSocio(id);
  mostrarSocios();   // vuelve a dibujar la tabla sin ese socio
}

// Cada vez que se escribe en el buscador, se filtra la tabla
document.getElementById("buscador").addEventListener("input", mostrarSocios);

mostrarSocios();