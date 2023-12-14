/*
 Oliver Fabian Stetcu | 2ºDAM
 
 Script para Crear la Base de Datos empleosdb (MySQL 8.0)
*/

CREATE DATABASE empleosdb2;
USE empleosdb2;

DROP TABLE IF EXISTS `Categoria`;
CREATE TABLE `Categoria` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `Usuario`;
CREATE TABLE `Usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `telefono` varchar(9) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `Candidato`;
CREATE TABLE `Candidato` (
  `usuario_id` int NOT NULL,
  `experiencia` varchar(500) DEFAULT NULL,
  `educacion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`usuario_id`),
  CONSTRAINT `fk_candidato_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `Empresa`;
CREATE TABLE `Empresa` (
  `usuario_id` int NOT NULL,
  `sector` varchar(100) DEFAULT NULL,
  `web` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`usuario_id`),
  CONSTRAINT `fk_empresa_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `Usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `Vacante`;
CREATE TABLE `Vacante` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) NOT NULL,
  `fecha` date NOT NULL,
  `salario` double NOT NULL,
  `destacado` int NOT NULL,
  `detalles` text,
  `idCategoria` int NOT NULL,
  `idUsuario` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vacante_categoria1_idx` (`idCategoria`),
  KEY `fk_vacante_Usuario1_idx` (`idUsuario`),
  CONSTRAINT `fk_vacante_categoria1` FOREIGN KEY (`idCategoria`) REFERENCES `Categoria` (`id`),
  CONSTRAINT `fk_vacante_Usuario1` FOREIGN KEY (`idUsuario`) REFERENCES `Empresa` (`usuario_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `Solicitud`;
CREATE TABLE `Solicitud` (
  `id` int NOT NULL,	
  `fecha` date NOT NULL,
  `archivo` BLOB NOT NULL,
  `idVacante` int NOT NULL,
  `idUsuario` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Solicitud_Vacante1_idx` (`idVacante`),
  KEY `fk_Solicitud_Usuario1_idx` (`idUsuario`),
  CONSTRAINT `fk_Solicitud_Usuario1` FOREIGN KEY (`idUsuario`) REFERENCES `Candidato` (`usuario_id`),
  CONSTRAINT `fk_Solicitud_Vacante1` FOREIGN KEY (`idVacante`) REFERENCES `Vacante` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
