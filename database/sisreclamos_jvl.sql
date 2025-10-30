-- Base de datos: sisreclamos_jvl
-- Script de creación compatible con MySQL 8.x

DROP DATABASE IF EXISTS `sisreclamos_jvl`;
CREATE DATABASE `sisreclamos_jvl` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `sisreclamos_jvl`;

-- Tabla de roles
CREATE TABLE `roles` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `nombreRol` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`idRol`),
  UNIQUE KEY `uk_roles_nombre` (`nombreRol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de usuarios
CREATE TABLE `usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `correo` VARCHAR(120) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `idRol` INT NOT NULL,
  `ipAutorizada` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `uk_usuarios_correo` (`correo`),
  CONSTRAINT `fk_usuarios_roles` FOREIGN KEY (`idRol`) REFERENCES `roles`(`idRol`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Catálogo de categorías
CREATE TABLE `categorias` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT,
  `nombreCategoria` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`idCategoria`),
  UNIQUE KEY `uk_categorias_nombre` (`nombreCategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla principal de reclamos
CREATE TABLE `reclamos` (
  `idReclamo` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `idCategoria` INT NOT NULL,
  `descripcion` TEXT NOT NULL,
  `fechaRegistro` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` ENUM('Pendiente','En atención','Resuelto') NOT NULL DEFAULT 'Pendiente',
  PRIMARY KEY (`idReclamo`),
  KEY `idx_reclamos_usuario` (`idUsuario`),
  KEY `idx_reclamos_categoria` (`idCategoria`),
  CONSTRAINT `fk_reclamos_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios`(`idUsuario`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reclamos_categoria` FOREIGN KEY (`idCategoria`) REFERENCES `categorias`(`idCategoria`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seguimiento de atenciones
CREATE TABLE `seguimientos` (
  `idSeguimiento` INT NOT NULL AUTO_INCREMENT,
  `idReclamo` INT NOT NULL,
  `idUsuario` INT NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `observacion` TEXT,
  `nuevoEstado` ENUM('Pendiente','En atención','Resuelto') NOT NULL,
  PRIMARY KEY (`idSeguimiento`),
  KEY `idx_seguimientos_reclamo` (`idReclamo`),
  KEY `idx_seguimientos_usuario` (`idUsuario`),
  CONSTRAINT `fk_seguimientos_reclamo` FOREIGN KEY (`idReclamo`) REFERENCES `reclamos`(`idReclamo`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_seguimientos_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios`(`idUsuario`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos iniciales
INSERT INTO `roles` (`nombreRol`) VALUES
  ('ADMINISTRADOR'),
  ('USUARIO');

INSERT INTO `usuarios` (`nombre`, `correo`, `password`, `idRol`, `ipAutorizada`) VALUES
  ('Administrador General', 'admin@sisreclamos.com', 'admin123', 1, NULL),
  ('Usuario Demo', 'usuario@sisreclamos.com', 'usuario123', 2, NULL);

INSERT INTO `categorias` (`nombreCategoria`, `descripcion`) VALUES
  ('Servicios Generales', 'Incidencias generales en las instalaciones'),
  ('Tecnología', 'Problemas con equipos tecnológicos'),
  ('Seguridad', 'Reportes relacionados con la seguridad');

INSERT INTO `reclamos` (`idUsuario`, `idCategoria`, `descripcion`, `estado`) VALUES
  (2, 1, 'Falla en el suministro eléctrico del tercer piso.', 'Pendiente'),
  (2, 2, 'La impresora de la oficina 201 no funciona.', 'En atención');

INSERT INTO `seguimientos` (`idReclamo`, `idUsuario`, `observacion`, `nuevoEstado`) VALUES
  (2, 1, 'Se solicitó revisión al área de soporte técnico.', 'En atención');

