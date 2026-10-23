-- Base de datos del sistema de gestión del gimnasio (issue #8)
-- Modelo en docs/modelo-datos.md
-- Se puede ejecutar varias veces: borra y vuelve a crear las tablas.

CREATE DATABASE IF NOT EXISTS gimnasio
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gimnasio;

-- Primero se borran las tablas que tienen FK, luego a las que apuntan
DROP TABLE IF EXISTS pago;
DROP TABLE IF EXISTS membresia;
DROP TABLE IF EXISTS plan;
DROP TABLE IF EXISTS socio;
DROP TABLE IF EXISTS usuario;

-- ---------------------------------------------------------------
-- Tablas
-- ---------------------------------------------------------------

CREATE TABLE socio (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    dni             VARCHAR(15)  NOT NULL UNIQUE,
    nombre          VARCHAR(50)  NOT NULL,
    apellido        VARCHAR(50)  NOT NULL,
    email           VARCHAR(100),
    telefono        VARCHAR(20),
    fecha_registro  DATE         NOT NULL
) ENGINE = InnoDB;

CREATE TABLE plan (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(50)    NOT NULL UNIQUE,
    duracion_dias  INT            NOT NULL,
    precio         DECIMAL(10,2)  NOT NULL
) ENGINE = InnoDB;

CREATE TABLE membresia (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    socio_id      INT   NOT NULL,
    plan_id       INT   NOT NULL,
    fecha_inicio  DATE  NOT NULL,
    fecha_fin     DATE  NOT NULL,
    CONSTRAINT fk_membresia_socio FOREIGN KEY (socio_id) REFERENCES socio (id),
    CONSTRAINT fk_membresia_plan  FOREIGN KEY (plan_id)  REFERENCES plan (id)
) ENGINE = InnoDB;

CREATE TABLE pago (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    membresia_id  INT            NOT NULL,
    fecha         DATE           NOT NULL,
    monto         DECIMAL(10,2)  NOT NULL,
    metodo        VARCHAR(20)    NOT NULL,
    CONSTRAINT fk_pago_membresia FOREIGN KEY (membresia_id) REFERENCES membresia (id)
) ENGINE = InnoDB;

CREATE TABLE usuario (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    usuario   VARCHAR(30)   NOT NULL UNIQUE,
    password  VARCHAR(100)  NOT NULL,
    rol       VARCHAR(20)   NOT NULL
) ENGINE = InnoDB;

-- ---------------------------------------------------------------
-- Datos de prueba
-- ---------------------------------------------------------------

INSERT INTO plan (nombre, duracion_dias, precio) VALUES
    ('Mensual',     30,  80000.00),
    ('Trimestral',  90, 220000.00),
    ('Anual',      365, 800000.00);

INSERT INTO socio (dni, nombre, apellido, email, telefono, fecha_registro) VALUES
    ('1012345678', 'Ana',       'Pérez',     'ana.perez@mail.com',     '3001234567', '2026-09-01'),
    ('1023456789', 'Carlos',    'Gómez',     'carlos.gomez@mail.com',  '3109876543', '2026-09-01'),
    ('1034567890', 'Laura',     'Martínez',  'laura.martinez@mail.com','3155551234', '2026-08-15'),
    ('1045678901', 'Andrés',    'Rodríguez', NULL,                     '3204445566', '2026-01-10'),
    ('1056789012', 'Valentina', 'López',     'vale.lopez@mail.com',    NULL,         '2026-09-25');

-- fecha_fin = fecha_inicio + duracion_dias del plan
INSERT INTO membresia (socio_id, plan_id, fecha_inicio, fecha_fin) VALUES
    (1, 1, '2026-09-01', '2026-10-01'),  -- Ana, mes anterior (vencida)
    (1, 1, '2026-10-01', '2026-10-31'),  -- Ana, mes actual (activa)
    (2, 2, '2026-09-01', '2026-11-30'),  -- Carlos, trimestral (activa)
    (3, 1, '2026-08-15', '2026-09-14'),  -- Laura (vencida)
    (4, 3, '2026-01-10', '2027-01-10'),  -- Andrés, anual (activa)
    (5, 1, '2026-09-25', '2026-10-25');  -- Valentina (vence esta semana)

INSERT INTO pago (membresia_id, fecha, monto, metodo) VALUES
    (1, '2026-09-01',  80000.00, 'EFECTIVO'),
    (2, '2026-10-01',  80000.00, 'TRANSFERENCIA'),
    (3, '2026-09-01', 220000.00, 'TARJETA'),
    (4, '2026-08-15',  80000.00, 'EFECTIVO'),
    (5, '2026-01-10', 800000.00, 'TRANSFERENCIA'),
    (6, '2026-09-25',  80000.00, 'EFECTIVO');

-- Usuario de prueba: admin / admin123 (contraseña cifrada con BCrypt)
INSERT INTO usuario (usuario, password, rol) VALUES
    ('admin', '$2y$10$yxkm//sApcH0OIH5S1yHsOrHoS3y/C5cxmHltujbIiQ951K5rzeVW', 'ADMIN');
