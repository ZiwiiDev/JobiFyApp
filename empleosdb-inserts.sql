/*
 Oliver Fabian Stetcu | 2ºDAM
 
 Script para Insertar Datos en la BD empleosdb (MySQL 8.0)
*/

/* Inserto datos en la tabla "Categoria" */
INSERT INTO `Categoria` (`id`,`nombre`) VALUES (1,'Arquitectura');
INSERT INTO `Categoria` (`id`,`nombre`) VALUES (2,'Recursos Humanos');
INSERT INTO `Categoria` (`id`,`nombre`) VALUES (3,'Desarrollo de software');

/* Inserto datos en la tabla "Usuarios" */
INSERT INTO `Usuario` (`id`, `nombre`, `email`, `username`, `password`, `telefono`, `direccion`) VALUES
(1, 'Juan Flores', 'juanflores@gmail.com', 'juanflores02', '123456', '641293124', 'C/ Tagliatella, 9'),
(2, 'Digital 55', 'digital55@hotmail.com', 'digital55', '123456', '901294312', 'C/ Alberto Verdas, 2'),
(3, 'Admin', 'admin@jobify.com', 'admin', 'admin', '449875432', 'C/ Buenavida, 20');

/* Inserto datos en la tabla "Candidato" */
INSERT INTO `Candidato` (`usuario_id`, `experiencia`, `educacion`) VALUES 
(1, 'Programador Java', 'DAM - DAW');

/* Inserto datos en la tabla "Empresa" */
INSERT INTO `Empresa` (`usuario_id`, `sector`, `web`) VALUES
(2, 'Desarrollo', 'www.digital55.com');

/* Inserto datos en la tabla "Vacante" */
INSERT INTO `Vacante` (`id`,`nombre`,`fecha`,`salario`,`destacado`,`detalles`,`idCategoria`, `idUsuario`) VALUES 
(1,'Gerente de RRHH','2023-06-10',17000.0,0,'- REQUISITOS - Licenciatura en Administración o Marketing. Experiencia mínima de 2 años en puesto similar.
Edad entre 22 y 35 años. Inglés avanzado. Administración de personal (altas, bajas y modificación de salario,
elaboración y revisión de contratos, cálculo de nómina...',2,2);
INSERT INTO `Vacante` (`id`,`nombre`,`fecha`,`salario`,`destacado`,`detalles`,`idCategoria`, `idUsuario`) VALUES 
(2,'Ingeniero Civil','2023-06-10',20000.0,0,'Se requiere estar titulado como Arquitecto y/o Ingeniero Civil. 
Función principal: Supervisión y control de obra, cuantificación de avances, 
estimaciones de avance de obra... Habilidades: Negociación, liderazgo, 
capacidad para resolver conflictos, Excel...',1,2);
INSERT INTO `Vacante` (`id`,`nombre`,`fecha`,`salario`,`destacado`,`detalles`,`idCategoria`, `idUsuario`) VALUES 
(3,'Programador Java','2023-06-10',25000.0,0,'OBJETIVO: DISEÑAR, CREAR Y REALIZAR MANTENIMIENTO A PÁGINAS Y APLICACIONES WEB. 
REQUISITOS: EDAD: 25 A 35 AÑOS. LICENCIATURA: DESARROLLO DE SOFTWARE. EXPERIENCIA EN JAVA (2 AÑOS).',3,2);

/* Inserto datos en la tabla "Solicitud" */
INSERT INTO `Solicitud` (`id`, `fecha`, `archivo`, `idVacante`, `idUsuario`) VALUES
(131451,'2023-06-10','programadorjava-131451.pdf',3,1);
