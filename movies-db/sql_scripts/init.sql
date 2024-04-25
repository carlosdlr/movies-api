CREATE TABLE `academy_awards` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `year` int NOT NULL,
                                  `edition` varchar(500) NOT NULL,
                                  `category` varchar(500) NOT NULL,
                                  `nominee` varchar(500) NOT NULL,
                                  `additional_info` text,
                                  `won` tinyint(1) DEFAULT '0',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB;