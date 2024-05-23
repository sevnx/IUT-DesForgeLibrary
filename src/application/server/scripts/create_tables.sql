CREATE TABLE deslibrary.DocumentState (
    id INT AUTO_INCREMENT,
    label VARCHAR(30) UNIQUE,
    CONSTRAINT PK_DocumentState PRIMARY KEY (id)
)

CREATE TABLE deslibrary.Document (
    id INT AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    idState INT NOT NULL DEFAULT 1,
    CONSTRAINT PK_Document PRIMARY KEY (id),
    CONSTRAINT FK_DocumentState FOREIGN KEY (idState) REFERENCES DocumentState(id)
)

CREATE TABLE deslibrary.Subscriber (
    id INTEGER AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,
    isBanned BOOLEAN DEFAULT FALSE,
    bannedUntil DATETIME DEFAULT NULL,
    CONSTRAINT PK_Subcriber PRIMARY KEY (id) 
)

CREATE TABLE deslibrary.DocumentLog (
    id INTEGER AUTO_INCREMENT,
    idDocument INT NOT NULL,
    idSubscriber INT,
    idNewState INT NOT NULL,
    date DATETIME NOT NULL,
    CONSTRAINT PK_Command PRIMARY KEY (id),
    CONSTRAINT FK_Command_idSubscriber FOREIGN KEY (idSubscriber) REFERENCES Subscriber(id),
    CONSTRAINT FK_Command_idDocument FOREIGN KEY (idDocument) REFERENCES Document(id),
    CONSTRAINT FK_Command_idNewState FOREIGN KEY (idNewState) REFERENCES DocumentState(id)
)

CREATE TABLE deslibrary.Dvd ( 
    id INT,
    isForAdult BOOLEAN,
    CONSTRAINT PK_Dvd PRIMARY KEY(id),
    CONSTRAINT FK_Dvd_id FOREIGN KEY (id) REFERENCES Document(id)
)