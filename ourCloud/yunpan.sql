/*
SQLyog Trial v13.1.8 (64 bit)
MySQL - 5.7.19 : Database - yunpan
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`yunpan` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `yunpan`;

/*Table structure for table `file` */

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `fileId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `recyclePath` varchar(255) DEFAULT NULL,
  `deleteTime` datetime DEFAULT NULL,
  PRIMARY KEY (`fileId`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

/*Data for the table `file` */

insert  into `file`(`fileId`,`userName`,`filePath`,`recyclePath`,`deleteTime`) values 
(27,'222','\\\\我的资源\\复习资料(1)','\\\\我的资源\\复习资料(1)(1)','2022-10-20 15:14:14'),
(28,'222','\\\\music\\红蔷薇白玫瑰.mp3','\\\\music\\红蔷薇白玫瑰.mp3','2022-10-20 15:14:19'),
(29,'222','\\\\我的资源\\学习资料','\\\\我的资源\\学习资料','2022-10-20 14:14:22'),
(30,'222','\\\\我的资源\\红蔷薇白玫瑰.mp3','\\\\我的资源\\红蔷薇白玫瑰(1).mp3','2022-10-20 15:14:30');

/*Table structure for table `office` */

DROP TABLE IF EXISTS `office`;

CREATE TABLE `office` (
  `officeid` varchar(32) NOT NULL,
  `officeMd5` varchar(32) NOT NULL,
  PRIMARY KEY (`officeMd5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `office` */

insert  into `office`(`officeid`,`officeMd5`) values 
('doc-njhrzsrdwvkyzbf','01C3F40F791E71B5A1C57D35ED2F01F9'),
('doc-njhrz2h8rbzncbd','1784B41E487AEE7B8FB80A1DE9AF58F5'),
('doc-njfx95a283xzsbf','21CC6238611193136F572E4E9001FEED'),
('doc-njhrzs584ur9985','3076E4E307C5A133B16A51E6E147D10E'),
('doc-njhrz1jatqarnud','320C918C95B4F786FED8571EC766D5B5'),
('doc-njhs002xsa28i1c','631DBE140E8180D7C19439DB6DB36C17'),
('doc-njfx7zavjw755md','98CDA24D4F5EC28DA35D015642DD1FB6'),
('doc-njhs00bs2i74zxp','CA61B070AC55E32C1A3ABBACB14BC99E'),
('doc-njhrz3ymbib60nu','E36C9807525C3B80F12CC36C4FF86568'),
('doc-njhrz27etkr7km6','EBDF0EAE3E529B16C36418EE8761D84E');

/*Table structure for table `share` */

DROP TABLE IF EXISTS `share`;

CREATE TABLE `share` (
  `shareId` int(11) NOT NULL AUTO_INCREMENT,
  `shareUrl` varchar(32) NOT NULL,
  `path` varchar(255) NOT NULL,
  `shareUser` varchar(20) NOT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '1公开 2加密 -1已取消',
  `shareTime` datetime DEFAULT NULL,
  `command` varchar(4) DEFAULT NULL COMMENT '提取码',
  PRIMARY KEY (`shareId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `share` */

insert  into `share`(`shareId`,`shareUrl`,`path`,`shareUser`,`status`,`shareTime`,`command`) values 
(1,'82b04d85','\\\\学习资料','111',0,'2022-10-19 19:15:57',NULL),
(2,'0e7fea02','\\\\学习资料\\复习','111',1,'2022-10-20 18:15:12',NULL),
(3,'0e7fea02','\\\\学习资料\\实验报告-学生用.docx','111',1,'2021-08-11 07:29:17',NULL),
(4,'ca62b7d8','\\\\music\\DuoYuanDouYao.mp3','111',1,'2022-10-20 19:22:15',NULL),
(5,'ca62b7d8','\\\\music\\qifengle.mp3','111',1,'2022-10-20 19:22:15',NULL);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) DEFAULT NULL,
  `countSize` varchar(20) DEFAULT '0.0B',
  `totalSize` varchar(20) DEFAULT '10.0GB',
  PRIMARY KEY (`id`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`countSize`,`totalSize`) values 
(42,'111','698D51A19D8A121CE581499D7B701668','114.1MB','10.0GB'),
(46,'333','310DCBBF4CCE62F762A2AAA148D556BD','0.0B','10.0GB');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
