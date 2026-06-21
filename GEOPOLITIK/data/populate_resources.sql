CREATE TABLE IF NOT EXISTS ProvinceResources (
    id_recurso INTEGER PRIMARY KEY AUTOINCREMENT,
    id_provincia INTEGER NOT NULL,
    "Gas natural" INTEGER DEFAULT 0,
    "Petróleo" INTEGER DEFAULT 0,
    "Madera" INTEGER DEFAULT 0,
    "Pesca" INTEGER DEFAULT 0,
    "Uranio" INTEGER DEFAULT 0,
    "Oro" INTEGER DEFAULT 0,
    "Agricultura" INTEGER DEFAULT 0,
    "Acero" INTEGER DEFAULT 0,
    "Industria" INTEGER DEFAULT 0,
    "electricidad" INTEGER DEFAULT 0,
    FOREIGN KEY (id_provincia) REFERENCES Provinces(id_province)
);

INSERT INTO ProvinceResources (id_provincia, "Gas natural", "Petróleo", "Madera", "Pesca", "Uranio", "Oro", "Agricultura", "Acero", "Industria", "electricidad")
SELECT
    p.id_province,
    -- Gas natural
    CASE
        WHEN c.name IN ('Irán', 'Turkmenistán', 'Arabia Saudita', 'Qatar', 'Omán') AND p.environment IN ('Desert', 'Coast', 'Plains') THEN 4
        WHEN c.name IN ('Irak', 'Emiratos Árabes Unidos', 'Kuwait', 'Baréin') AND p.environment IN ('Desert', 'Coast') THEN 3
        WHEN c.name IN ('Siria', 'Egipto', 'Yemen', 'Uzbekistán', 'Azerbaiyán') AND p.environment IN ('Desert', 'Coast') THEN 2
        ELSE 0
    END,
    -- Petróleo
    CASE
        WHEN c.name IN ('Arabia Saudita', 'Irak', 'Kuwait', 'Emiratos Árabes Unidos') AND p.environment IN ('Desert', 'Coast') THEN 5
        WHEN c.name IN ('Irán', 'Qatar', 'Omán') AND p.environment IN ('Desert', 'Coast') THEN 4
        WHEN c.name IN ('Turkmenistán', 'Azerbaiyán', 'Siria', 'Yemen') AND p.environment IN ('Desert', 'Coast') THEN 2
        ELSE 0
    END,
    -- Madera
    CASE
        WHEN p.environment = 'Forest' THEN 4
        WHEN p.environment = 'Montains' THEN 2
        WHEN p.environment = 'Hills' THEN 1
        ELSE 0
    END,
    -- Pesca
    CASE
        WHEN p.is_coastal = 1 AND p.environment = 'Coast' THEN 4
        WHEN p.is_coastal = 1 AND p.environment = 'City' THEN 3
        WHEN p.is_coastal = 1 THEN 2
        ELSE 0
    END,
    -- Uranio
    CASE
        WHEN c.name = 'Irán' AND p.environment = 'Montains' THEN 2
        WHEN c.name IN ('Uzbekistán', 'Turkmenistán') AND p.environment = 'Desert' THEN 1
        ELSE 0
    END,
    -- Oro
    CASE
        WHEN p.environment = 'Montains' AND c.name IN ('Turquía', 'Irán', 'Arabia Saudita', 'Uzbekistán') THEN 3
        WHEN p.environment = 'Montains' THEN 2
        WHEN p.environment = 'Hills' AND c.name IN ('Turquía', 'Irán') THEN 2
        WHEN p.environment = 'Hills' THEN 1
        ELSE 0
    END,
    -- Agricultura
    CASE
        WHEN p.environment = 'Plains' AND p.is_coastal = 1 THEN 5
        WHEN p.environment = 'Plains' THEN 4
        WHEN p.environment = 'Hills' THEN 3
        WHEN p.environment = 'Forest' THEN 3
        WHEN p.environment = 'City' THEN 2
        ELSE 1
    END,
    -- Acero (todo 0)
    0,
    -- Industria (todo 0)
    0,
    -- electricidad (todo 0)
    0
FROM Provinces p
JOIN Countries c ON p.id_country = c.id_country;
