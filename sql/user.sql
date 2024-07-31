-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.34 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for identify_service
CREATE DATABASE IF NOT EXISTS `identify_service` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `identify_service`;

-- Dumping structure for table identify_service.role
CREATE TABLE IF NOT EXISTS `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table identify_service.role: ~2 rows (approximately)
INSERT INTO `role` (`id`, `name`) VALUES
	(1, 'ADMIN'),
	(2, 'USER');

-- Dumping structure for table identify_service.token
CREATE TABLE IF NOT EXISTS `token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `experiationDate` datetime(6) DEFAULT NULL,
  `isMobile` bit(1) DEFAULT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `revoked` bit(1) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `tokenType` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `refreshExpirationDate` datetime(6) DEFAULT NULL,
  `expired` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj8rfw4x0wjjyibfqq566j4qng` (`user_id`),
  CONSTRAINT `FKj8rfw4x0wjjyibfqq566j4qng` FOREIGN KEY (`user_id`) REFERENCES `users` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table identify_service.token: ~3 rows (approximately)
INSERT INTO `token` (`id`, `experiationDate`, `isMobile`, `refreshToken`, `revoked`, `token`, `tokenType`, `user_id`, `refreshExpirationDate`, `expired`) VALUES
	(28, '2024-08-06 23:38:40.150045', b'1', 'df9bcc36-a152-4880-a070-bacdabb71bc2', b'0', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWJqZWN0IjoibHkucGhpbWluaEBnbWFpbC5jb20iLCJ1c2VySWQiOiJseS5waGltaW5oQGdtYWlsLmNvbSIsInN1YiI6Imx5LnBoaW1pbmhAZ21haWwuY29tIiwiZXhwIjoxNzIyOTYyMzIwfQ.Nr5laz44NAHBfZeiXVsugAdpr9iEo4Wy6uETCJDXY9s', 'Bearer', 'ly.phiminh@gmail.com', '2024-09-05 23:38:40.150045', b'0'),
	(29, '2024-08-06 23:45:06.425978', b'1', 'cef5439b-3fae-467d-96df-66ed191d55f3', b'0', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWJqZWN0IjoibHkucGhpbWluaEBnbWFpbC5jb20iLCJ1c2VySWQiOiJseS5waGltaW5oQGdtYWlsLmNvbSIsInN1YiI6Imx5LnBoaW1pbmhAZ21haWwuY29tIiwiZXhwIjoxNzIyOTYyNzA2fQ.hpdKL6c_9nszXTAIjE-C29KEXNt2iR1SPVU_9rsNbJM', 'Bearer', 'ly.phiminh@gmail.com', '2024-09-05 23:45:06.425978', b'0'),
	(44, '2024-08-26 23:24:43.732831', b'0', 'b9d65db9-9244-4977-a3dd-a190b24009ae', b'0', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWJqZWN0IjoibHkucGhpbWluaEBnbWFpbC5jb20iLCJ1c2VySWQiOiJseS5waGltaW5oQGdtYWlsLmNvbSIsInN1YiI6Imx5LnBoaW1pbmhAZ21haWwuY29tIiwiZXhwIjoxNzI0Njg5NDgzfQ.ZFMQlU2HLiee3f8RpK-PvhQnj5g7HciOfgUUf0LfUd8', 'Bearer', 'ly.phiminh@gmail.com', '2024-09-25 23:24:43.733865', b'0');

-- Dumping structure for table identify_service.users
CREATE TABLE IF NOT EXISTS `users` (
  `email` varchar(255) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `lob` date DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role_Id` int DEFAULT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `updateAt` bigint DEFAULT NULL,
  PRIMARY KEY (`email`),
  KEY `FKjlb84bcmenxp2888o1kh98y5e` (`role_Id`),
  CONSTRAINT `FKjlb84bcmenxp2888o1kh98y5e` FOREIGN KEY (`role_Id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table identify_service.users: ~1 rows (approximately)
INSERT INTO `users` (`email`, `active`, `firstName`, `lastName`, `lob`, `password`, `phone`, `role_Id`, `fullName`, `otp`, `updateAt`) VALUES
	('ly.phiminh@gmail.com', b'1', NULL, NULL, NULL, '$2a$10$ya1idWnzb12x9dwtfqtrSurntiuNnD8L9ycKEfOyVhWl/PaNGjboS', NULL, 1, 'minh', '92186', 1720367579093),
	('lyphiminh244@gmail.com', b'0', NULL, NULL, NULL, '$2a$10$k55Fp6C0616n2ipSE/cKEeO2.UXuktTRpjVBCt7AMCfdSV7JloR4G', NULL, 2, 'de', '82076', 1721499482993);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
