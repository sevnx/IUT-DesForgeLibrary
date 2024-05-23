-- Adult User

INSERT INTO deslibrary.Document (id, title) VALUES (1,"Psychose");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (1, true);

INSERT INTO deslibrary.Document (id, title) VALUES (2, "Cars");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (2, true);

-- Non-adult User

INSERT INTO deslibrary.Document (id, title) VALUES (3,"Wall-E");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (3, false);

INSERT INTO deslibrary.Document (id, title) VALUES (4, "L'âge de Glace");
INSERT INTO deslibrary.Dvd (id, isForAdult) VALUES (4, false);