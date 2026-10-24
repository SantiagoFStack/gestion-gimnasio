const form = document.getElementById("form-login");
const error = document.getElementById("error");

form.addEventListener("submit", async (e) => {
  e.preventDefault();   // evita que la página se recargue al enviar

  const usuario = document.getElementById("usuario").value.trim();
  const password = document.getElementById("password").value;

  if (usuario === "" || password === "") {
    error.textContent = "Rellena usuario y contraseña";
    return;
  }

  try {
    const user = await login(usuario, password);
    sessionStorage.setItem("usuario", user.usuario);   // recuerda quién entró
    window.location.href = "dashboard.html";
  } catch (err) {
    error.textContent = err.message;
  }
});