create database libreria_tecnica;
CREATE TABLE `ciudad` (
  `idciudad` int(11) NOT NULL AUTO_INCREMENT,
  `ciudad` varchar(45) NOT NULL,
  PRIMARY KEY (`idciudad`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
CREATE TABLE `usuario` (
  `usuario` varchar(20) NOT NULL,
  `password` varchar(40) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `direccion` varchar(80) NOT NULL,
  `email` varchar(45) NOT NULL,
  `telefono` varchar(9) NOT NULL,
  `ciudad` int(11) NOT NULL,
  PRIMARY KEY (`usuario`),
  UNIQUE KEY `telefono_UNIQUE` (`telefono`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `idioma` (
  `ididioma` int(11) NOT NULL AUTO_INCREMENT,
  `idioma` varchar(2) NOT NULL DEFAULT 'SP',
  PRIMARY KEY (`ididioma`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
CREATE TABLE `seccion` (
  `idseccion` int(11) NOT NULL AUTO_INCREMENT,
  `seccion` varchar(45) NOT NULL,
  PRIMARY KEY (`idseccion`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
CREATE TABLE `producto` (
  `idproducto` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(60) NOT NULL,
  `autor` varchar(80) NOT NULL,
  `editorial` varchar(45) NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `año` varchar(4) DEFAULT NULL,
  `paginas` int(11) DEFAULT NULL,
  `resumen` varchar(400) NOT NULL,
  `descripcion` varchar(600) DEFAULT NULL,
  `precio` float NOT NULL,
  `seccion` int(11) NOT NULL DEFAULT '1',
  `lengua` int(11) DEFAULT NULL,
  `stock` int(11) NOT NULL DEFAULT '1',
  `descatalogado` int(11) NOT NULL DEFAULT '0',
  `imagen` blob,
  PRIMARY KEY (`idproducto`),
  UNIQUE KEY `isbn_UNIQUE` (`isbn`),
  KEY `idseccion_idx` (`seccion`),
  KEY `fk_idioma_idx` (`lengua`),
  CONSTRAINT `fk_idioma` FOREIGN KEY (`lengua`) REFERENCES `idioma` (`ididioma`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `idseccion` FOREIGN KEY (`seccion`) REFERENCES `seccion` (`idseccion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
CREATE TABLE `recibo` (
  `idrecibo` int(11) NOT NULL AUTO_INCREMENT,
  `cliente` varchar(20) DEFAULT NULL,
  `fecha` date NOT NULL,
  `total` double DEFAULT NULL,
  PRIMARY KEY (`idrecibo`),
  KEY `for_cliente` (`cliente`),
  CONSTRAINT `cliente` FOREIGN KEY (`cliente`) REFERENCES `usuario` (`usuario`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;
CREATE TABLE `detalle` (
  `iddetalle` int(11) NOT NULL AUTO_INCREMENT,
  `producto` int(11) NOT NULL,
  `unidades` int(11) NOT NULL,
  `recibo` int(11) DEFAULT NULL,
  PRIMARY KEY (`iddetalle`),
  KEY `recibo_idx` (`recibo`),
  KEY `fk_producto_idx` (`producto`),
  CONSTRAINT `fk_producto` FOREIGN KEY (`producto`) REFERENCES `producto` (`idproducto`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `recibo` FOREIGN KEY (`recibo`) REFERENCES `recibo` (`idrecibo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8;
