CREATE DATABASE `user_0`;

CREATE TABLE `user_info_1`
(
    `user_id`   bigint(19)  NOT NULL,
    `user_name` varchar(45) DEFAULT NULL,
    `account`   varchar(45) NOT NULL,
    `password`  varchar(45) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `user_info_0`
(
    `user_id`   bigint(19)  NOT NULL,
    `user_name` varchar(45) DEFAULT NULL,
    `account`   varchar(45) NOT NULL,
    `password`  varchar(45) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE DATABASE `user_1` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `user_info_1`
(
    `user_id`   bigint(19)  NOT NULL,
    `user_name` varchar(45) DEFAULT NULL,
    `account`   varchar(45) NOT NULL,
    `password`  varchar(45) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `user_info_0`
(
    `user_id`   bigint(19)  NOT NULL,
    `user_name` varchar(45) DEFAULT NULL,
    `account`   varchar(45) NOT NULL,
    `password`  varchar(45) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;