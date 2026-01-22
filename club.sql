CREATE DATABASE IF NOT EXISTS `cyberclub` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cyberclub`;

-- 1. Таблица пользователей (теперь с балансом и без роли employee)
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','client') NOT NULL,
  `balance` decimal(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. Таблица клиентов (оставляем как было, но убираем связь с employee)
CREATE TABLE IF NOT EXISTS `clients` (
  `client_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `phone_number` varchar(15) NOT NULL,
  PRIMARY KEY (`client_id`),
  UNIQUE KEY `unique_user` (`user_id`),
  CONSTRAINT `fk_users_clients` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Таблица групп клиентов (упрощенная)
CREATE TABLE IF NOT EXISTS `client_groups` (
  `group_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `discount_percent` decimal(5,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. Связь многие-ко-многим между клиентами и группами
CREATE TABLE IF NOT EXISTS `client_group_membership` (
  `client_id` int NOT NULL,
  `group_id` int NOT NULL,
  PRIMARY KEY (`client_id`,`group_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `client_group_membership_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`) ON DELETE CASCADE,
  CONSTRAINT `client_group_membership_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `client_groups` (`group_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 5. Таблица компьютеров
CREATE TABLE IF NOT EXISTS `computers` (
  `computer_id` int NOT NULL AUTO_INCREMENT,
  `specs` text NOT NULL COMMENT 'Полные характеристики ПК',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Доступен для бронирования',
  PRIMARY KEY (`computer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Таблица тарифов (упрощенная)
CREATE TABLE IF NOT EXISTS `tariffs` (
  `tariff_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `price_per_hour` decimal(10,2) NOT NULL,
  PRIMARY KEY (`tariff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 7. Таблица бронирований (бывшая appointments)
CREATE TABLE IF NOT EXISTS `bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `client_id` int NOT NULL,
  `computer_id` int NOT NULL,
  `tariff_id` int NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `status` enum('pending','confirmed','completed') NOT NULL DEFAULT 'pending',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`booking_id`),
  KEY `client_id` (`client_id`),
  KEY `computer_id` (`computer_id`),
  KEY `tariff_id` (`tariff_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`) ON DELETE CASCADE,
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`computer_id`) REFERENCES `computers` (`computer_id`),
  CONSTRAINT `bookings_ibfk_3` FOREIGN KEY (`tariff_id`) REFERENCES `tariffs` (`tariff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 8. Таблица отзывов (оставляем как было)
CREATE TABLE IF NOT EXISTS `reviews` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `review_text` text NOT NULL,
  `rating` tinyint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`),
  KEY `fk_users_reviews` (`user_id`),
  CONSTRAINT `fk_users_reviews` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reviews_chk_1` CHECK (`rating` between 1 and 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;