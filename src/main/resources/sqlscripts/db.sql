-- MySQL Workbench Forward Engineering

SET
@OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET
@OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET
@OLD_SQL_MODE=@@SQL_MODE, SQL_MODE=''ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ca2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ca2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ca2` DEFAULT CHARACTER SET utf8mb3;
USE
`ca2` ;

-- -----------------------------------------------------
-- Table `ca2`.`movies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ca2`.`movies`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `title`
    VARCHAR
(
    100
) NOT NULL,
    `year` INT NOT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ca2`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ca2`.`roles`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `role`
    VARCHAR
(
    45
) NOT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ca2`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ca2`.`users`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `name`
    VARCHAR
(
    45
) NOT NULL,
    `password` VARCHAR
(
    255
) NOT NULL,
    `age` INT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ca2`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ca2`.`users_roles`
(
    `user_id`
    INT
    NOT
    NULL,
    `role_id`
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    `user_id`,
    `role_id`
),
    INDEX `fk_users_has_roles_roles1_idx`
(
    `role_id` ASC
) VISIBLE,
    INDEX `fk_users_has_roles_users_idx`
(
    `user_id` ASC
) VISIBLE,
    CONSTRAINT `fk_users_has_roles_roles1`
    FOREIGN KEY
(
    `role_id`
)
    REFERENCES `ca2`.`roles`
(
    `id`
),
    CONSTRAINT `fk_users_has_roles_users`
    FOREIGN KEY
(
    `user_id`
)
    REFERENCES `ca2`.`users`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ca2`.`watchlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ca2`.`watchlist`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `users_id`
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    `id`,
    `users_id`
),
    INDEX `fk_watchlist_users1_idx`
(
    `users_id` ASC
) VISIBLE,
    CONSTRAINT `fk_watchlist_users1`
    FOREIGN KEY
(
    `users_id`
)
    REFERENCES `ca2`.`users`
(
    `id`
)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ca2`.`movies_has_watchlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ca2`.`movies_has_watchlist`
(
    `movies_id`
    INT
    NOT
    NULL,
    `watchlist_id`
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    `movies_id`,
    `watchlist_id`
),
    INDEX `fk_movies_has_watchlist_watchlist1_idx`
(
    `watchlist_id` ASC
) VISIBLE,
    INDEX `fk_movies_has_watchlist_movies1_idx`
(
    `movies_id` ASC
) VISIBLE,
    CONSTRAINT `fk_movies_has_watchlist_movies1`
    FOREIGN KEY
(
    `movies_id`
)
    REFERENCES `ca2`.`movies`
(
    `id`
)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_movies_has_watchlist_watchlist1`
    FOREIGN KEY
(
    `watchlist_id`
)
    REFERENCES `ca2`.`watchlist`
(
    `id`
)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


SET
SQL_MODE=@OLD_SQL_MODE;
SET
FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET
UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
