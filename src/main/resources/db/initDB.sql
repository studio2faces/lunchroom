DROP SCHEMA IF EXISTS lunchroom;

DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS users;

CREATE SCHEMA lunchroom;

DROP SEQUENCE IF EXISTS GLOBAL_SEQ;

CREATE SEQUENCE GLOBAL_SEQ START WITH 100000;

CREATE TABLE users
(
    id       INTEGER DEFAULT GLOBAL_SEQ.nextval PRIMARY KEY,
    name     VARCHAR(25)                  NOT NULL,
    email    VARCHAR(100)                 NOT NULL,
    password VARCHAR(500)                 NOT NULL,
    date     DATE    DEFAULT CURRENT_DATE NOT NULL,
    enabled  BOOL    DEFAULT TRUE         NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id      INTEGER DEFAULT GLOBAL_SEQ.nextval PRIMARY KEY,
    name    VARCHAR(50)  NOT NULL,
    phone   VARCHAR(16)  NOT NULL,
    address VARCHAR(300) NOT NULL,
    website VARCHAR(100)
);

CREATE TABLE menu
(
    id      INTEGER DEFAULT GLOBAL_SEQ.nextval PRIMARY KEY,
    date    DATE    DEFAULT CURRENT_DATE NOT NULL,
    rest_id INTEGER                      NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE dish
(
    id      INTEGER DEFAULT GLOBAL_SEQ.nextval PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    price   INTEGER      NOT NULL,
    menu_id INTEGER      NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    id      INTEGER   DEFAULT GLOBAL_SEQ.nextval PRIMARY KEY,
    date    TIMESTAMP DEFAULT now() NOT NULL,
    rest_id INTEGER                 NOT NULL,
    menu_id INTEGER                 NOT NULL,
    user_id INTEGER                 NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);