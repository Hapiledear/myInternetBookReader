/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.7.10-log : Database - e_book_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`e_book_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `e_book_db`;

/*Table structure for table `tb_book` */

DROP TABLE IF EXISTS `tb_book`;

CREATE TABLE `tb_book` (
  `id` char(50) NOT NULL,
  `url` char(50) DEFAULT 'img/nopicture.jpg' COMMENT '封面路径',
  `author` char(50) DEFAULT '未知',
  `lab` char(50) DEFAULT '其它' COMMENT '分类标签，&号分隔',
  `click_num` int(10) DEFAULT '0' COMMENT '点击数',
  `collection_num` int(10) DEFAULT '0' COMMENT '收藏数',
  `update` date DEFAULT NULL COMMENT '更新日期',
  `state` int(1) DEFAULT '0' COMMENT '状态：连载or完结',
  `name` char(50) DEFAULT NULL,
  `Introduction` varchar(1000) NOT NULL DEFAULT '暂无简介' COMMENT '简介，50个字之内',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_book` */

insert  into `tb_book`(`id`,`url`,`author`,`lab`,`click_num`,`collection_num`,`update`,`state`,`name`,`Introduction`) values ('1','img/book/1.jpg','我','测试',100,100,'2016-02-13',1,'我的第一个测试文件','走出便利商店要回家的高中生‧菜月昴突然被召唤到异世界。 这莫非就是很流行的异世界召唤!?可是眼前没有召唤者就算了，还遭遇强盗迅速面临性命危机。 这时，一名神秘银发美少女和猫精灵拯救了一筹莫展的他。 以报恩为名义，昴自告奋勇要帮助少女找东西。 然而，好不容易才掌握到线索，昴和少女却被不明人士攻击而殒命──本来应该是这样，但回过神来，昴却发现自己置身在第一次被召唤到这个异世界时的所在位置。 「死亡回归」──无力的少年得到的唯一能力，是死后时间会倒转回到一开始。跨越无数绝望，从死亡的命运中拯救少女！'),('2','img/nopicture.jpg','未知','其它&测试',100,100,'2016-02-18',0,'我的第二个测试文件','暂无简介');

/*Table structure for table `tb_chapter` */

DROP TABLE IF EXISTS `tb_chapter`;

CREATE TABLE `tb_chapter` (
  `b_id` char(50) NOT NULL COMMENT '书籍id',
  `c_id` int(50) NOT NULL COMMENT '章节id',
  `c_name` char(50) NOT NULL COMMENT '章节名称',
  `url` char(50) NOT NULL COMMENT '相对地址 book/b_id/c_id',
  `date` date DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`b_id`,`c_id`),
  CONSTRAINT `tb_chapter_ibfk_1` FOREIGN KEY (`b_id`) REFERENCES `tb_book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_chapter` */

insert  into `tb_chapter`(`b_id`,`c_id`,`c_name`,`url`,`date`) values ('1',1,'测试','book/1/1.txt','2016-04-08'),('1',2,'长文本测试','book/1/2.txt','2016-04-08'),('1',3,'多章节测试','book/1/1.txt','2016-04-08');

/*Table structure for table `tb_recommend` */

DROP TABLE IF EXISTS `tb_recommend`;

CREATE TABLE `tb_recommend` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `url` char(50) DEFAULT NULL,
  `b_id` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `b_id` (`b_id`),
  CONSTRAINT `tb_recommend_ibfk_1` FOREIGN KEY (`b_id`) REFERENCES `tb_book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `tb_recommend` */

insert  into `tb_recommend`(`id`,`url`,`b_id`) values (1,'img/recommend/p1.jpg','1'),(2,'img/recommend/p2.jpg',NULL),(3,'img/recommend/p3.jpg',NULL),(4,'img/recommend/p4.jpg',NULL);

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `name` char(50) NOT NULL COMMENT '账号',
  `password` char(50) NOT NULL COMMENT '密码',
  `email` varchar(50) DEFAULT NULL,
  `phone` char(50) DEFAULT NULL,
  `state` int(1) DEFAULT '0' COMMENT '是否被验证',
  `acidcode` char(50) DEFAULT NULL COMMENT '验证码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `tb_user` */

insert  into `tb_user`(`id`,`name`,`password`,`email`,`phone`,`state`,`acidcode`) values (1,'admin','admin','admin','admin',1,NULL),(2,'aaaaa','aaaaa',NULL,NULL,1,NULL),(3,'qwert','qqert',NULL,NULL,1,NULL),(6,'123456','123456',NULL,NULL,1,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
