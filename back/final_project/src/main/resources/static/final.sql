-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema floating
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema floating
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `floating` DEFAULT CHARACTER SET utf8 ;
USE `floating` ;

-- -----------------------------------------------------
-- Table `floating`.`mbti`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `floating`.`mbti` (
  `mbti_pk` INT NOT NULL,
  `name` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`mbti_pk`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `floating`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `floating`.`user` (
  `user_pk` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `age` INT NOT NULL,
  `gender` CHAR(5) NOT NULL,
  `height` INT NOT NULL,
  `weight` INT NOT NULL,
  `ebti_id` INT NOT NULL,
  `id` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`user_pk`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_user_mbti_idx` (`ebti_id` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  CONSTRAINT `fk_user_mbti`
    FOREIGN KEY (`ebti_id`)
    REFERENCES `floating`.`mbti` (`mbti_pk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `floating`.`plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `floating`.`plan` (
  `plan_pk` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `category` TEXT NOT NULL,
  `detail` TEXT NOT NULL,
  `time` INT NOT NULL,
  `complete_date` DATE NULL,
  `shifted` INT NULL,
  `user_pk` INT NOT NULL,
  PRIMARY KEY (`plan_pk`),
  UNIQUE INDEX `id_UNIQUE` (`plan_pk` ASC) VISIBLE,
  INDEX `fk_plan_user1_idx` (`user_pk` ASC) VISIBLE,
  CONSTRAINT `fk_plan_user1`
    FOREIGN KEY (`user_pk`)
    REFERENCES `floating`.`user` (`user_pk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `floating`.`wbti`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `floating`.`wbti` (
  `social_type` INT NOT NULL,
  `motivation_type` INT NOT NULL,
  `execution_type` INT NOT NULL,
  `activity_type` INT NOT NULL,
  `user_pk` INT NOT NULL,
  INDEX `fk_ebti_user1_idx` (`user_pk` ASC) VISIBLE,
  PRIMARY KEY (`user_pk`),
  CONSTRAINT `fk_ebti_user1`
    FOREIGN KEY (`user_pk`)
    REFERENCES `floating`.`user` (`user_pk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `floating`.`pet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `floating`.`pet` (
  `pet_pk` INT NOT NULL AUTO_INCREMENT,
  `score` INT NOT NULL,
  `user_pk` INT NOT NULL,
  PRIMARY KEY (`pet_pk`),
  UNIQUE INDEX `id_UNIQUE` (`pet_pk` ASC) VISIBLE,
  INDEX `fk_pet_user1_idx` (`user_pk` ASC) VISIBLE,
  CONSTRAINT `fk_pet_user1`
    FOREIGN KEY (`user_pk`)
    REFERENCES `floating`.`user` (`user_pk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `floating`.`review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `floating`.`review` (
  `review_pk` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT NULL,
  `plan_pk` INT NOT NULL,
  PRIMARY KEY (`review_pk`),
  INDEX `fk_review_plan1_idx` (`plan_pk` ASC) VISIBLE,
  CONSTRAINT `fk_review_plan1`
    FOREIGN KEY (`plan_pk`)
    REFERENCES `floating`.`plan` (`plan_pk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `floating`.`img`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `floating`.`img` (
  `img_pk` INT NOT NULL AUTO_INCREMENT,
  `img_path` VARCHAR(255) NULL,
  `review_pk` INT NOT NULL,
  PRIMARY KEY (`img_pk`),
  INDEX `fk_img_review1_idx` (`review_pk` ASC) VISIBLE,
  CONSTRAINT `fk_img_review1`
    FOREIGN KEY (`review_pk`)
    REFERENCES `floating`.`review` (`review_pk`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
