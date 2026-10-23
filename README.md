# Gestión de Gimnasio

Sistema web para gestionar las membresías de un gimnasio: socios, planes, pagos y vencimientos.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-6db33f)
![MySQL](https://img.shields.io/badge/MySQL-8-4479a1)
![Estado](https://img.shields.io/badge/estado-en%20desarrollo-yellow)

## Sobre el proyecto

Muchos gimnasios pequeños llevan los socios y los pagos a mano o en papel. Así es difícil saber quién tiene la cuota vencida, y se pierden cobros.

Esta web permite dar de alta socios y planes, registrar pagos y ver al momento quién está al día y quién no.

> La problemática y la solución completas se redactan en el issue #14.

## Funcionalidades

- [ ] Inicio de sesión del personal
- [ ] Panel con socios activos, vencidos e ingresos del mes
- [ ] Alta, edición, búsqueda y baja de socios
- [ ] Gestión de planes (mensual, trimestral, anual…)
- [ ] Registro de pagos con cálculo automático del vencimiento
- [ ] Estado de cada socio: `ACTIVA` o `VENCIDA`

## Tecnologías

| Parte | Tecnología |
|---|---|
| Backend | Java, Spring Boot (Web + Data JPA), Maven |
| Base de datos | MySQL |
| Frontend | HTML, CSS, JavaScript (`fetch`) |
| Herramientas | NetBeans, VS Code, Postman, MySQL Workbench |

## Estructura del repositorio

```
gestion-gimnasio/
├── backend/          API REST en Java (Spring Boot)
│   └── bd/           Script SQL de la base de datos
├── frontend/         Pantallas HTML, CSS y JS
└── docs/             Modelo de datos y contrato de la API
```

## Documentación

- [Modelo de datos](docs/modelo-datos.md): tablas, columnas y relaciones.
- [Contrato de la API](docs/api.md): endpoints y JSON que comparten front y back.

## Cómo ejecutarlo

> Se completará cuando el backend y el frontend estén listos.

1. Crear la base de datos ejecutando `backend/bd/gimnasio.sql` en MySQL.
2. Abrir `backend/` en NetBeans, configurar `application.properties` y ejecutar. La API queda en `http://localhost:8080/api`.
3. Abrir `frontend/index.html` con Live Server.

## Cómo trabajamos

- Cada tarea es un [issue](../../issues), etiquetado como `frontend`, `backend` o `documentacion` y agrupado en hitos por fase.
- Front y back avanzan por separado y se unen en el [contrato de la API](docs/api.md).
- Cada commit cierra su issue con `Closes #N`.

## Equipo

| Integrante | Rol |
|---|---|
| [@SantiagoFStack](https://github.com/SantiagoFStack) | Backend |
| *por completar* | Frontend |
