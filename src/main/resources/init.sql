CREATE DATABASE horsefeeder

CREATE TABLE `stable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `food` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `horse` (
  `id` int NOT NULL AUTO_INCREMENT,
  `guid` varchar(36) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `breed` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `stable_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `guid` (`guid`),
  KEY `stable_id` (`stable_id`),
  CONSTRAINT `horse_ibfk_1` FOREIGN KEY (`stable_id`) REFERENCES `stable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `feeding_schedule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `horse_id` int DEFAULT NULL,
  `food_id` int DEFAULT NULL,
  `feeding_start` varchar(255) DEFAULT NULL,
  `feeding_end` varchar(255) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `horse_id` (`horse_id`),
  KEY `food_id` (`food_id`),
  CONSTRAINT `feeding_schedule_ibfk_1` FOREIGN KEY (`horse_id`) REFERENCES `horse` (`id`),
  CONSTRAINT `feeding_schedule_ibfk_2` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `feeding_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `horse_id` int DEFAULT NULL,
  `food_id` int DEFAULT NULL,
  `feeding_time` varchar(255) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `horse_id` (`horse_id`),
  KEY `food_id` (`food_id`),
  CONSTRAINT `feeding_history_ibfk_1` FOREIGN KEY (`horse_id`) REFERENCES `horse` (`id`),
  CONSTRAINT `feeding_history_ibfk_2` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci