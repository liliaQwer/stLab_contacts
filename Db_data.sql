CREATE DATABASE  IF NOT EXISTS `contacts_karavaichyk` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `contacts_karavaichyk`;
-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: contacts_karavaichyk
-- ------------------------------------------------------
-- Server version	5.7.26-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `address` (
  `contact_id` int(10) unsigned NOT NULL,
  `country` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
  `city` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
  `street` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  `postal_code` char(6) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `address_contact_idx` (`contact_id`),
  CONSTRAINT `address_contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,NULL,NULL,NULL,NULL),(2,'Russia','Moscow','Belaya 1','230230'),(3,'Belarus',NULL,NULL,NULL),(7,NULL,NULL,NULL,NULL),(22,NULL,NULL,NULL,NULL),(29,NULL,'Minsk',NULL,NULL),(30,NULL,'Minsk',NULL,NULL),(31,'1','2','3','4'),(32,'Belarus','Grodno','Belaya','5'),(36,'Belarus','Minsk','st','123'),(37,'Belarus','Minsk','st','123'),(38,'country',NULL,'street',NULL),(39,'country',NULL,'street',NULL),(40,'country',NULL,'street',NULL),(41,'country',NULL,'street',NULL),(43,'c',NULL,'s',NULL),(44,'c','2','s','2'),(45,NULL,NULL,NULL,NULL),(46,NULL,'ds',NULL,'c'),(53,NULL,NULL,NULL,NULL),(55,'Беларусь','Минск',NULL,NULL),(66,'Belarus','Minsk',NULL,NULL),(67,'Belarus','Minsk',NULL,NULL);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `attachment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `contact_id` int(10) unsigned NOT NULL,
  `file_name` varchar(80) CHARACTER SET utf8mb4 NOT NULL,
  `upload_date` date NOT NULL,
  `comment` varchar(150) CHARACTER SET utf8mb4 DEFAULT NULL,
  `uuid` varchar(36) COLLATE utf8mb4_unicode_520_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `attachment_contact_idx` (`contact_id`),
  CONSTRAINT `attachment_contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
INSERT INTO `attachment` VALUES (47,71,'edit1.png','2019-07-05','rjvvtyn','1a5bdec5-0e19-49ab-abab-f6335827d460'),(56,80,'edit1.png','2019-07-05','','e7b4629f-46e0-46c5-bc13-9c89827496dd'),(57,81,'edit1.png','2019-07-05','','abbda9a0-455a-4d1e-8c04-67a5a6644d27'),(58,81,'edit1.png','2019-07-05','','c6e127aa-b28e-4646-bbfb-81f77513621f'),(60,2,'fetch.umd.js','2019-07-06',NULL,'3fbdf955-87fc-450e-bfdb-8b8409f89d9c'),(63,22,'edit1.png','2019-07-07',NULL,'f974ee65-c70a-49ec-a59f-4f8fde2a12a7'),(64,22,'IMG_20180823_200658.jpg','2019-07-07','','971334b4-d699-43d0-8769-ef3c29ed81f4'),(65,22,'IMG_20180823_200658.jpg','2019-07-07','','b2c7ee3d-336a-47e1-aeed-d98a156d9ee4'),(66,22,'edit2.png','2019-07-07',NULL,'93527375-8c5c-473c-807c-8f6edaf4a857');
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `contact` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 NOT NULL,
  `patronymic` varchar(40) CHARACTER SET utf8mb4 DEFAULT NULL,
  `surname` varchar(40) CHARACTER SET utf8mb4 NOT NULL,
  `birthday` date DEFAULT NULL,
  `company` varchar(80) CHARACTER SET utf8mb4 DEFAULT NULL,
  `nationality` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `marital_status_id` int(11) unsigned DEFAULT NULL,
  `site` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `gender_id` int(11) unsigned DEFAULT NULL,
  `active_status` enum('0','1') COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '1',
  `profile_photo` varchar(80) COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `contact_gender_idx` (`gender_id`),
  KEY `contact_maritalStatus_idx` (`marital_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (1,'Lili','Eduardovna','Karavaichyk','1986-09-12','Beltelecom','belarus',2,NULL,'liliaqwer@gmail.com',2,'0','IMG_20180823_200658.jpg'),(2,'Andrey','Ivanovich','Potapov','1990-06-29','iTechArt','belarus',1,NULL,'liliaqwer@gmail.com',1,'1',NULL),(3,'Ivan','Petrovich','Ivanov','2000-03-03','Amazon','belarus',2,NULL,'liliaqwer@gmail.com',1,'0',NULL),(4,'Irina','Pavlovna','Soroka','1995-05-05','Integral','belarus',2,NULL,'liliaqwer@gmail.com',2,'1','20140515_212033.jpg'),(5,'Irina','Ivanova','Petrova','2000-01-01','A-1','belarus',2,NULL,'liliaqwer@gmail.com',2,'1',NULL),(6,'Egor','Michailovich','Kuznecov','1950-01-02','Atlant','belarus',2,NULL,'liliaqwer@gmail.com',1,'0','20140628_135033.jpg'),(7,'Egor2','Michailovich2','Kuznecov2',NULL,'Atlant','belarus',2,NULL,'liliaqwer@gmail.com',1,'0','qw.jpg'),(8,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',1,NULL,'kuznec@mail.ru',1,'1',NULL),(9,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',2,NULL,'kuznec@mail.ru',1,'1',NULL),(10,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',1,NULL,'kuznec@mail.ru',1,'1',NULL),(11,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',2,NULL,'kuznec@mail.ru',1,'1',NULL),(12,'Egor2','Michailovich2','Kuznecov2','1950-06-30','Atlant','belarus',1,NULL,'liliaqwer@gmail.com',1,'1',NULL),(13,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',2,NULL,'liliaqwer@gmail.com',1,'1',NULL),(14,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',1,NULL,'kuznec@mail.ru',1,'1',NULL),(15,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',2,NULL,'kuznec@mail.ru',1,'1',NULL),(16,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',1,NULL,'kuznec@mail.ru',1,'1',NULL),(17,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',2,NULL,'kuznec@mail.ru',1,'1',NULL),(18,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',1,NULL,'kuznec@mail.ru',1,'1',NULL),(19,'Egor2','Michailovich2','Kuznecov2','1950-01-02','Atlant','belarus',2,NULL,'kuznec@mail.ru',1,'0',NULL),(20,'lili','cgfgbf','zdfddf','2010-12-25','sony inc','bel',2,NULL,'ilili@mail.ru',1,'0',NULL),(21,'lili','cgfgbf','zdfddf','2010-12-25','sony inc','bel',2,NULL,'ilili@mail.ru',1,'0',NULL),(22,'Alesya',NULL,'Karavaychik',NULL,NULL,NULL,1,NULL,'lisr87@gmail.com',2,'1',NULL),(23,'Alesya',NULL,'Karavaychik',NULL,NULL,NULL,NULL,NULL,'lis87@gmail.com',1,'1','edit1.png'),(24,'Alesya',NULL,'Karavaychik',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','20140426_111827.jpg'),(25,'lili',NULL,'karavaicchyk',NULL,NULL,NULL,NULL,NULL,'alesya@mail.ru',NULL,'1',NULL),(26,'lili',NULL,'kar',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(27,'lili','Noemail','lili',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(28,'lili','Noemail','lili',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(29,'lili12','dfgd','lillilililika','2000-10-02','btk','bel',1,'http','lilia@gmail.com',1,'1',NULL),(30,'lili',NULL,'Kononvich',NULL,NULL,NULL,1,NULL,NULL,1,'1',NULL),(31,'test',NULL,'test',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(32,'test',NULL,'test',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(33,'Lilka',NULL,'Kilka',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(34,'Lilka',NULL,'Kilka',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(35,'Lilka2',NULL,'Kilka2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(36,'MAsha','1','MAsha',NULL,'2','3',1,NULL,'liliaqwer@gamail.com',1,'1',NULL),(37,'MAsha','1','MAsha',NULL,'2','3',1,NULL,'liliaqwer@gamail.com',1,'1',NULL),(38,'Final',NULL,'Test',NULL,NULL,NULL,NULL,NULL,NULL,1,'1',NULL),(39,'Final',NULL,'Test',NULL,NULL,NULL,NULL,NULL,NULL,1,'1',NULL),(40,'Final',NULL,'Test',NULL,'C','N',NULL,'https://google.com','final.qwer@mail.ru',1,'1',NULL),(41,'Final',NULL,'Test',NULL,'C','N',2,'https://google.com','final.qwer@mail.ru',1,'1',NULL),(43,'Final22',NULL,'Test22',NULL,NULL,NULL,2,'https://google.com',NULL,1,'1',NULL),(44,'d2',NULL,'a2','2019-06-03',NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(45,'Phone',NULL,'Test',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(46,'Attach',NULL,'Test',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(50,'лдлдлываы','ываываыв','ыаыаы',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(53,'Лилька','длжждлждл','Килька',NULL,'Пулька',NULL,NULL,NULL,NULL,NULL,'1','20180628_102739.jpg'),(55,'Лилия',NULL,'каравайчик','2019-06-17',NULL,'белорус',NULL,NULL,NULL,2,'1',NULL),(60,'hkjhlk',NULL,'k;lk\';\'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(61,'ПЕрвый',NULL,'Пример','2019-07-01',NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(62,'Второй',NULL,'ПРимер','2019-07-02',NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(63,'Третий',NULL,'примемр',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(64,'Проверка Присоединений',NULL,'ап',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(65,'Gdfg',NULL,'segd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(66,'Svetlana','Andreevna','Karavaychik','2016-09-21',NULL,NULL,2,'https://sveta.com','sveta.konfeta@mail.ru',2,'1','20140515_212033.jpg'),(67,'Svetlana','Andreevna','Karavaychik','2016-09-21',NULL,NULL,1,'https://sveta.com','sveta.konfeta@mail.ru',2,'1',NULL),(69,'lili','Karavaichyk','Edv','2015-05-31',NULL,NULL,NULL,'http://wer.com','liliaqwer@gmail.com',NULL,'1','374673-1920x1080.jpg'),(70,'multipart',NULL,'ex',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(71,'attachment',NULL,'test',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(80,'dgs',NULL,'sd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL),(81,'Save Attachment',NULL,'save Attach',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL);
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gender`
--

DROP TABLE IF EXISTS `gender`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gender` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(10) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gender`
--

LOCK TABLES `gender` WRITE;
/*!40000 ALTER TABLE `gender` DISABLE KEYS */;
INSERT INTO `gender` VALUES (1,'male'),(2,'female');
/*!40000 ALTER TABLE `gender` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marital_status`
--

DROP TABLE IF EXISTS `marital_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `marital_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(20) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marital_status`
--

LOCK TABLES `marital_status` WRITE;
/*!40000 ALTER TABLE `marital_status` DISABLE KEYS */;
INSERT INTO `marital_status` VALUES (1,'unmarried'),(2,'married');
/*!40000 ALTER TABLE `marital_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `phone` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contact_id` int(10) unsigned NOT NULL,
  `country_code` char(3) CHARACTER SET utf8mb4 NOT NULL,
  `oper_code` char(4) CHARACTER SET utf8mb4 NOT NULL,
  `number` char(12) CHARACTER SET utf8mb4 NOT NULL,
  `type` int(10) unsigned DEFAULT NULL,
  `comment` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phone_phoneTypes_idx` (`type`),
  KEY `phone_contact_idx` (`contact_id`),
  CONSTRAINT `phone_contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `phone_phoneTypes` FOREIGN KEY (`type`) REFERENCES `phone_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone`
--

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone` DISABLE KEYS */;
INSERT INTO `phone` VALUES (1,2,'123','12','1254',1,'2fdgs'),(2,30,'12','12','123654',1,NULL),(3,35,'22','33','44',1,NULL),(4,37,'123','12','1213131',2,NULL),(5,38,'555','66','777',1,NULL),(6,38,'111','22','333',2,NULL),(7,39,'555','66','777',1,NULL),(8,39,'111','22','333',2,NULL),(9,40,'555','66','777',1,'home c'),(10,40,'111','22','333',2,NULL),(11,41,'555','66','777',1,'home c'),(12,41,'111','22','333',2,NULL),(15,43,'222','33','444',1,NULL),(16,43,'111','22','333',2,'3'),(17,45,'222','33','44',1,NULL),(19,45,'555','55','555',2,'5'),(20,2,'222','22','222222',1,NULL),(22,55,'375','44','123457',2,'пример'),(23,60,'123','45','1234567',NULL,NULL),(24,66,'222','56','1234567',1,NULL),(26,7,'373','44','1234567',2,NULL),(27,22,'123','12','2324343',NULL,NULL);
/*!40000 ALTER TABLE `phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_type`
--

DROP TABLE IF EXISTS `phone_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `phone_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(10) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_type`
--

LOCK TABLES `phone_type` WRITE;
/*!40000 ALTER TABLE `phone_type` DISABLE KEYS */;
INSERT INTO `phone_type` VALUES (1,'home'),(2,'mobile');
/*!40000 ALTER TABLE `phone_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-08 11:37:54
