# Publicar el frontend en internet (GitHub Pages)

La carpeta `frontend/` se publica sola como página web cada vez que cambia en `main`.

👉 **https://santiagofstack.github.io/gestion-gimnasio/**

## Cómo funciona

```
Cambias algo en frontend/  →  PR a main  →  Merge  →  GitHub publica  →  En 1–2 min se ve en el enlace
```

1. Haces tus cambios en `frontend/` en tu rama, como siempre.
2. Abres el Pull Request y se une a `main` (Merge).
3. GitHub detecta que cambió `frontend/` y ejecuta el archivo `.github/workflows/pages.yml`.
   Ese archivo son las "instrucciones": bajar el código, empaquetar la carpeta `frontend/` y publicarla.
4. En 1 o 2 minutos, cualquiera que abra el enlace (desde el PC o el celular) ve la versión nueva.

Los cambios que están solo en una rama **no** se publican: hay que unirlos a `main`.

## Ver si se publicó bien

En GitHub, pestaña **Actions** → **Publicar el frontend**:

| Icono | Qué significa |
|---|---|
| 🟡 círculo amarillo | Publicando, espera un momento |
| ✅ check verde | Publicado: recarga el enlace (Ctrl + F5 si sigue viéndose lo viejo) |
| ❌ cruz roja | Falló: entra en la ejecución para ver el error |

Para volver a publicar sin cambiar nada: **Actions** → **Publicar el frontend** → **Run workflow**.

## Mientras programas: Live Server

GitHub Pages es para **compartir** la web con el grupo o el profe. Para ver los cambios **al instante** mientras programas, sigue usando **Live Server** en VS Code (clic derecho en `index.html` → *Open with Live Server*): recarga en cuanto guardas, sin esperar a GitHub.

| | Live Server | GitHub Pages |
|---|---|---|
| Dónde se ve | Solo en tu PC | Desde cualquier lugar, con el enlace |
| Cuándo se actualiza | Al guardar | 1–2 min después de unir a `main` |
| Para qué | Programar y probar | Compartir y entregar |

## Importante: datos falsos

La web publicada usa los **datos falsos** de `frontend/js/api.js`. El backend Java corre en el PC (no en internet), así que todavía no guarda nada en la base de datos. Las pantallas se ven y se pueden usar, pero los cambios se pierden al recargar. Eso se resuelve en el issue #18 (conectar el front con el back).

## Reglas para que funcione

- La página de inicio debe llamarse `frontend/index.html` (el login).
- Los enlaces entre páginas deben ser **relativos**: `href="socios.html"`, no `href="/socios.html"`. Con `/` delante, en GitHub Pages se rompen porque la web vive en `/gestion-gimnasio/`.
- Las rutas a CSS y JS también relativas: `css/estilos.css`, `js/api.js`.
