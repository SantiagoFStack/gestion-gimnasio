# Front y back conectados (issue #18)

**Ya están conectados.** `frontend/js/api.js` ya no usa datos falsos: llama a la API del backend, que está publicada en internet. Todo lo que se crea en las pantallas se guarda en la base de datos de verdad.

```
Web (GitHub Pages)  →  js/api.js  →  API Java (Render)  →  Base de datos (Aiven)
```

| Pieza | Dónde está |
|---|---|
| Web | https://santiagofstack.github.io/gestion-gimnasio/ |
| API | https://gimnasio-api-imsn.onrender.com/api (prueba: [/api/planes](https://gimnasio-api-imsn.onrender.com/api/planes)) |
| Base de datos | Aiven (MySQL, plan gratis) |

Usuario para entrar: **admin / admin123**.

## Para el front: qué hay que hacer ahora

1. Bajar lo último: `git switch main` y `git pull`.
2. Trabajar como siempre con **Live Server**. No hace falta instalar Java ni arrancar nada: la API ya está en internet.
3. Las funciones de `api.js` se llaman **igual** que las de los datos falsos y devuelven lo mismo, así que las pantallas que ya funcionaban siguen igual (ya probado: login, planes y formulario de socio).

### Lo que falta completar

Estos archivos están vacíos; hay que hacerlos siguiendo la *Guía del frontend*:

| Archivo | Qué falta | Funciones de `api.js` que usa |
|---|---|---|
| `js/dashboard.js` | Rellenar las 4 tarjetas (ahora salen "–") | `getDashboard()` |
| `socios.html` | La tabla de socios con buscador | `getSocios(buscar)`, `eliminarSocio(id)` |
| `pago.html` y `js/pago.js` | Registrar pago e historial | `getSocios()`, `getPlanes()`, `registrarPago(datos)`, `getPagos(socioId)` |

Cada una en su rama, con su PR y `Closes #N` (#2 dashboard, #3 socios, #6 pago).

## Cosas a saber

- **La API "se duerme".** En el plan gratis de Render, si nadie la usa en 15 minutos se apaga. La primera petición después tarda **unos 50 segundos**; las siguientes van normal. Si sale "No se pudo conectar con el servidor", esperar un minuto y recargar.
- **Los datos son compartidos.** Todos (el front, el back, la web publicada) usan la misma base. Lo que borres desde una pantalla se borra para todos.
- **La base de Aiven** también se apaga si pasa mucho tiempo sin uso. Si la API responde "No se pudo conectar a la base de datos", Santiago la enciende en console.aiven.io (*Power on*).

## Para el back: probar cambios de la API en el PC

1. Tener `backend/src/main/resources/conexion.properties` (no está en GitHub: tiene la contraseña).
2. Arrancar `backend/src/main/java/api/Servidor.java` con **Run** en VS Code.
3. En `frontend/js/api.js` cambiar temporalmente `URL_API` por `"http://localhost:8080/api"` (**no subir ese cambio**).

Cuando los cambios del backend se unen a `main`, hay que volver a publicar la API: en Render → *gimnasio-api* → **Manual Deploy** → *Deploy latest commit*.

## Problemas comunes

| Qué pasa | Por qué | Solución |
|---|---|---|
| "No se pudo conectar con el servidor" | La API estaba dormida | Esperar 1 minuto y recargar |
| "No se pudo conectar a la base de datos" | La base de Aiven se apagó | Encenderla en console.aiven.io |
| En la consola (F12): `CORS` | Se abrió el HTML con doble clic (`file://`) | Abrirlo con Live Server |
| Un campo sale `undefined` | El nombre del campo no coincide con el contrato | Revisar [`docs/api.md`](api.md) |
