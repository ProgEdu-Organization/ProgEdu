-- MySQL dump 10.13  Distrib 5.7.31, for Linux (x86_64)
--
-- Host: localhost    Database: ProgEdu
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE SCHEMA ProgEdu DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;

use ProgEdu;
--
-- Table structure for table `Assignment`
--

DROP TABLE IF EXISTS `Assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `description` varchar(3000) CHARACTER SET utf8 DEFAULT NULL,
  `hasTemplate` tinyint(1) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `zipChecksum` bigint(19) DEFAULT NULL,
  `zipUrl` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `releaseTime` datetime DEFAULT NULL,
  `display` tinyint(1) DEFAULT NULL,
  `gitLabId` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `A_type_idx` (`type`),
  CONSTRAINT `A_type` FOREIGN KEY (`type`) REFERENCES `Assignment_Type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Assignment`
--

LOCK TABLES `Assignment` WRITE;
/*!40000 ALTER TABLE `Assignment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Assignment_Review_Metrics`
--

DROP TABLE IF EXISTS `Assignment_Review_Metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assignment_Review_Metrics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aId` int(11) DEFAULT NULL,
  `rmId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `A_R_M_aId_idx` (`aId`),
  KEY `A_R_M_rmId_idx` (`rmId`),
  CONSTRAINT `A_R_M_aId` FOREIGN KEY (`aId`) REFERENCES `Assignment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `A_R_M_rmId` FOREIGN KEY (`rmId`) REFERENCES `Review_Metrics` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Assignment_Review_Metrics`
--

LOCK TABLES `Assignment_Review_Metrics` WRITE;
/*!40000 ALTER TABLE `Assignment_Review_Metrics` DISABLE KEYS */;
/*!40000 ALTER TABLE `Assignment_Review_Metrics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Assignment_Type`
--

DROP TABLE IF EXISTS `Assignment_Type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assignment_Type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Assignment_Type`
--

LOCK TABLES `Assignment_Type` WRITE;
/*!40000 ALTER TABLE `Assignment_Type` DISABLE KEYS */;
INSERT INTO `Assignment_Type` VALUES (1,'javac'),(2,'maven'),(3,'web'),(4,'android');
/*!40000 ALTER TABLE `Assignment_Type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Assignment_User`
--

DROP TABLE IF EXISTS `Assignment_User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Assignment_User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aId` int(11) DEFAULT NULL,
  `uId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `A_id_idx` (`aId`),
  KEY `A_U_uid_idx` (`uId`),
  CONSTRAINT `A_U_aid` FOREIGN KEY (`aId`) REFERENCES `Assignment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `A_U_uid` FOREIGN KEY (`uId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Assignment_User`
--

LOCK TABLES `Assignment_User` WRITE;
/*!40000 ALTER TABLE `Assignment_User` DISABLE KEYS */;
/*!40000 ALTER TABLE `Assignment_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Commit_Record`
--

DROP TABLE IF EXISTS `Commit_Record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Commit_Record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auId` int(11) DEFAULT NULL,
  `commitNumber` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `A_U_id_idx` (`auId`),
  KEY `C_S_id_idx` (`status`),
  CONSTRAINT `C_R_auid` FOREIGN KEY (`auId`) REFERENCES `Assignment_User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `C_R_status` FOREIGN KEY (`status`) REFERENCES `Commit_Status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Commit_Record`
--

LOCK TABLES `Commit_Record` WRITE;
/*!40000 ALTER TABLE `Commit_Record` DISABLE KEYS */;
/*!40000 ALTER TABLE `Commit_Record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Commit_Status`
--

DROP TABLE IF EXISTS `Commit_Status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Commit_Status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Commit_Status`
--

LOCK TABLES `Commit_Status` WRITE;
/*!40000 ALTER TABLE `Commit_Status` DISABLE KEYS */;
INSERT INTO `Commit_Status` VALUES (1,'bs'),(2,'csf'),(3,'cpf'),(4,'ini'),(5,'utf'),(6,'whf'),(7,'wsf'),(8,'wef'),(9,'uitf');
/*!40000 ALTER TABLE `Commit_Status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Group`
--

DROP TABLE IF EXISTS `Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gitLabId` int(11) DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `leader` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Group`
--

LOCK TABLES `Group` WRITE;
/*!40000 ALTER TABLE `Group` DISABLE KEYS */;
/*!40000 ALTER TABLE `Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Group_User`
--

DROP TABLE IF EXISTS `Group_User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Group_User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uId` int(11) DEFAULT NULL,
  `gId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `U_id_idx` (`uId`),
  KEY `G_id_idx` (`gId`),
  KEY `G_U_uid_idx` (`uId`),
  KEY `G_U_gid_idx` (`gId`),
  CONSTRAINT `G_U_gid` FOREIGN KEY (`gId`) REFERENCES `Group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `G_U_uid` FOREIGN KEY (`uId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Group_User`
--

LOCK TABLES `Group_User` WRITE;
/*!40000 ALTER TABLE `Group_User` DISABLE KEYS */;
/*!40000 ALTER TABLE `Group_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pair_Matching`
--

DROP TABLE IF EXISTS `Pair_Matching`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Pair_Matching` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auId` int(11) DEFAULT NULL,
  `reviewId` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `P_M_reviewId_idx` (`reviewId`),
  KEY `P_M_auId_idx` (`auId`),
  KEY `P_M_status_idx` (`status`),
  CONSTRAINT `P_M_auId` FOREIGN KEY (`auId`) REFERENCES `Assignment_User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `P_M_reviewId` FOREIGN KEY (`reviewId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `P_M_status` FOREIGN KEY (`status`) REFERENCES `Review_Status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pair_Matching`
--

LOCK TABLES `Pair_Matching` WRITE;
/*!40000 ALTER TABLE `Pair_Matching` DISABLE KEYS */;
/*!40000 ALTER TABLE `Pair_Matching` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `description` varchar(3000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `gitLabId` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `releaseTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `A_T_id_idx` (`type`),
  KEY `P_type_idx` (`type`),
  CONSTRAINT `P_type` FOREIGN KEY (`type`) REFERENCES `Assignment_Type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project_Commit_Record`
--

DROP TABLE IF EXISTS `Project_Commit_Record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project_Commit_Record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pgId` int(11) DEFAULT NULL,
  `commitNumber` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `commitStudent` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `P_C_R_pgid_idx` (`pgId`),
  KEY `P_C_R_status_idx` (`status`),
  CONSTRAINT `P_C_R_pgid` FOREIGN KEY (`pgId`) REFERENCES `Project_Group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `P_C_R_status` FOREIGN KEY (`status`) REFERENCES `Commit_Status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project_Commit_Record`
--

LOCK TABLES `Project_Commit_Record` WRITE;
/*!40000 ALTER TABLE `Project_Commit_Record` DISABLE KEYS */;
/*!40000 ALTER TABLE `Project_Commit_Record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project_Group`
--

DROP TABLE IF EXISTS `Project_Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project_Group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pId` int(11) DEFAULT NULL,
  `gId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `P_G_pid_idx` (`pId`),
  KEY `P_G_gid_idx` (`gId`),
  CONSTRAINT `P_G_gid` FOREIGN KEY (`gId`) REFERENCES `Group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `P_G_pid` FOREIGN KEY (`pId`) REFERENCES `Project` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project_Group`
--

LOCK TABLES `Project_Group` WRITE;
/*!40000 ALTER TABLE `Project_Group` DISABLE KEYS */;
/*!40000 ALTER TABLE `Project_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project_Screenshot_Record`
--

DROP TABLE IF EXISTS `Project_Screenshot_Record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project_Screenshot_Record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pcrid` int(11) DEFAULT NULL,
  `pngUrl` varchar(2083) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `P_S_R_pcrid_idx` (`pcrid`),
  CONSTRAINT `P_S_R_pcrid` FOREIGN KEY (`pcrid`) REFERENCES `Project_Commit_Record` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project_Screenshot_Record`
--

LOCK TABLES `Project_Screenshot_Record` WRITE;
/*!40000 ALTER TABLE `Project_Screenshot_Record` DISABLE KEYS */;
/*!40000 ALTER TABLE `Project_Screenshot_Record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Review_Category`
--

DROP TABLE IF EXISTS `Review_Category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Review_Category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `metrics` varchar(3000) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Review_Category`
--

LOCK TABLES `Review_Category` WRITE;
/*!40000 ALTER TABLE `Review_Category` DISABLE KEYS */;
/*!40000 ALTER TABLE `Review_Category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Review_Metrics`
--

DROP TABLE IF EXISTS `Review_Metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Review_Metrics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` int(11) DEFAULT NULL,
  `mode` int(11) DEFAULT NULL,
  `description` varchar(3000) COLLATE utf8_bin DEFAULT NULL,
  `link` varchar(3000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `R_M_category_idx` (`category`),
  KEY `R_M_mode_idx` (`mode`),
  CONSTRAINT `R_M_category` FOREIGN KEY (`category`) REFERENCES `Review_Category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `R_M_mode` FOREIGN KEY (`mode`) REFERENCES `Score_Mode` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Review_Metrics`
--

LOCK TABLES `Review_Metrics` WRITE;
/*!40000 ALTER TABLE `Review_Metrics` DISABLE KEYS */;
/*!40000 ALTER TABLE `Review_Metrics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Review_Record`
--

DROP TABLE IF EXISTS `Review_Record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Review_Record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pmId` int(11) DEFAULT NULL,
  `armId` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `feedback` varchar(3000) COLLATE utf8_bin DEFAULT NULL,
  `reviewOrder` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `R_R_pmId_idx` (`pmId`),
  KEY `R_R_armId_idx` (`armId`),
  CONSTRAINT `R_R_armId` FOREIGN KEY (`armId`) REFERENCES `Assignment_Review_Metrics` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `R_R_pmId` FOREIGN KEY (`pmId`) REFERENCES `Pair_Matching` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Review_Record`
--

LOCK TABLES `Review_Record` WRITE;
/*!40000 ALTER TABLE `Review_Record` DISABLE KEYS */;
/*!40000 ALTER TABLE `Review_Record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Review_Setting`
--

DROP TABLE IF EXISTS `Review_Setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Review_Setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aId` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `releaseTime` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `R_S_aId` FOREIGN KEY (`id`) REFERENCES `Assignment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Review_Setting`
--

LOCK TABLES `Review_Setting` WRITE;
/*!40000 ALTER TABLE `Review_Setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `Review_Setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Review_Status`
--

DROP TABLE IF EXISTS `Review_Status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Review_Status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Review_Status`
--

LOCK TABLES `Review_Status` WRITE;
/*!40000 ALTER TABLE `Review_Status` DISABLE KEYS */;
/*!40000 ALTER TABLE `Review_Status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES (1,'teacher'),(2,'student');
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role_User`
--

DROP TABLE IF EXISTS `Role_User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role_User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uId` int(11) DEFAULT NULL,
  `rId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `R_U_uid_idx` (`uId`),
  KEY `R_U_rid_idx` (`rId`),
  CONSTRAINT `R_U_rid` FOREIGN KEY (`rId`) REFERENCES `Role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `R_U_uid` FOREIGN KEY (`uId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Role_User`
--

LOCK TABLES `Role_User` WRITE;
/*!40000 ALTER TABLE `Role_User` DISABLE KEYS */;
/*!40000 ALTER TABLE `Role_User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Score_Mode`
--

DROP TABLE IF EXISTS `Score_Mode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Score_Mode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mode` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Score_Mode`
--

LOCK TABLES `Score_Mode` WRITE;
/*!40000 ALTER TABLE `Score_Mode` DISABLE KEYS */;
/*!40000 ALTER TABLE `Score_Mode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Screenshot_Record`
--

DROP TABLE IF EXISTS `Screenshot_Record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Screenshot_Record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `crId` int(11) DEFAULT NULL,
  `pngUrl` varchar(2083) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `S_R_crid_idx` (`crId`),
  CONSTRAINT `S_R_crid` FOREIGN KEY (`crId`) REFERENCES `Commit_Record` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Screenshot_Record`
--

LOCK TABLES `Screenshot_Record` WRITE;
/*!40000 ALTER TABLE `Screenshot_Record` DISABLE KEYS */;
/*!40000 ALTER TABLE `Screenshot_Record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gitLabId` int(11) DEFAULT NULL,
  `username` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gitLabToken` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `display` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-03  2:54:55
