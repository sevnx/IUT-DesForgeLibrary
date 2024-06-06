CREATE DATABASE IF NOT EXISTS deslibrary;

DROP TABLE IF EXISTS deslibrary.DocumentState;
DROP TABLE IF EXISTS deslibrary.Document;
DROP TABLE IF EXISTS deslibrary.Subscriber;
DROP TABLE IF EXISTS deslibrary.DocumentLog;
DROP TABLE IF EXISTS deslibrary.Dvd;

CREATE TABLE deslibrary.DocumentState
(
    id    INT AUTO_INCREMENT,
    label VARCHAR(30) UNIQUE,
    CONSTRAINT PK_DocumentState PRIMARY KEY (id)
);

CREATE TABLE deslibrary.Document
(
    id      INT AUTO_INCREMENT,
    title   VARCHAR(100) NOT NULL,
    idState INT          NOT NULL DEFAULT 1,
    CONSTRAINT PK_Document PRIMARY KEY (id),
    CONSTRAINT FK_DocumentState FOREIGN KEY (idState) REFERENCES DocumentState (id)
);

CREATE TABLE deslibrary.DocumentLog
(
    idDocument   INT      NOT NULL,
    idSubscriber INT,
    idNewState   INT      NOT NULL,
    date         TIMESTAMP NOT NULL,
    CONSTRAINT PK_Command PRIMARY KEY (idDocument),
    CONSTRAINT FK_Command_idSubscriber FOREIGN KEY (idSubscriber) REFERENCES Subscriber (id),
    CONSTRAINT FK_Command_idDocument FOREIGN KEY (idDocument) REFERENCES Document (id),
    CONSTRAINT FK_Command_idNewState FOREIGN KEY (idNewState) REFERENCES DocumentState (id)
);

CREATE TABLE deslibrary.Subscriber
(
    id          INTEGER AUTO_INCREMENT,
    firstName   VARCHAR(50)  NOT NULL,
    lastName    VARCHAR(100) NOT NULL,
    birthdate   DATE         NOT NULL,
    isBanned    BOOLEAN  DEFAULT FALSE,
    bannedUntil TIMESTAMP DEFAULT NULL,
    CONSTRAINT PK_Subcriber PRIMARY KEY (id)
);



CREATE TABLE deslibrary.Dvd
(
    id         INT,
    isForAdult BOOLEAN,
    CONSTRAINT PK_Dvd PRIMARY KEY (id),
    CONSTRAINT FK_Dvd_id FOREIGN KEY (id) REFERENCES Document (id)
);

INSERT INTO deslibrary.DocumentState (id, label)
VALUES (1, 'FREE');
INSERT INTO deslibrary.DocumentState (id, label)
VALUES (2, 'RESERVED');
INSERT INTO deslibrary.DocumentState (id, label)
VALUES (3, 'BORROWED');

-- Adult User

INSERT INTO deslibrary.Document (id, title)
VALUES (1, "Psychose");
INSERT INTO deslibrary.Dvd (id, isForAdult)
VALUES (1, true);

INSERT INTO deslibrary.Document (id, title)
VALUES (2, "Cars");
INSERT INTO deslibrary.Dvd (id, isForAdult)
VALUES (2, true);

-- Non-adult User

INSERT INTO deslibrary.Document (id, title)
VALUES (3, "Wall-E");
INSERT INTO deslibrary.Dvd (id, isForAdult)
VALUES (3, false);

INSERT INTO deslibrary.Document (id, title)
VALUES (4, "L'âge de Glace");
INSERT INTO deslibrary.Dvd (id, isForAdult)
VALUES (4, false);

-- > 16 years old

INSERT INTO deslibrary.Subscriber (firstName, lastName, birthdate)
VALUES ('René', 'Descartes', '1596-01-31');
INSERT INTO deslibrary.Subscriber (firstName, lastName, birthdate)
VALUES ('Olympe', 'de Gouges', '1748-05-07');
INSERT INTO deslibrary.Subscriber (firstName, lastName, birthdate)
VALUES ('Corentin', 'de Lenclos', '2004-04-07');

-- < 16 years old

INSERT INTO deslibrary.Subscriber (firstName, lastName, birthdate)
VALUES ('Séverin', 'Czykinowski', '2009-08-28');
