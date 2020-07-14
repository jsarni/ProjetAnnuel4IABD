CREATE TABLE `HORODATEURS_CAPACITY` (
  `horodateur_id` int(11) NOT NULL,
  `horodateur_nb_places_reel` int(11) NOT NULL,
  `horodateur_nb_places_calcul` int(11) NOT NULL,
  `horodateur_latitude` decimal(11,8) DEFAULT NULL,
  `horodateur_longitude` decimal(11,8) DEFAULT NULL,
  PRIMARY KEY (`horodateur_id`),
  UNIQUE KEY `horodateur_id_UNIQUE` (`horodateur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Cette table contient pour chaque horodateur , le nombre de places réelle , et le nombre de places calculées		