CREATE TABLE personajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(50) NOT NULL
);

INSERT INTO personajes (categoria) VALUES
('Aliado'),
('Antagonista');


CREATE TABLE personaje (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    origen VARCHAR(80) NOT NULL,
    tipo_origen VARCHAR(50) NOT NULL,
    personajes_id INT NOT NULL,
    CONSTRAINT fk_personaje_personajes
    FOREIGN KEY (personajes_id) REFERENCES personajes(id)
);

INSERT INTO personaje (nombre, origen, tipo_origen, personajes_id) VALUES
('Ouro Kronii', 'Hololive', 'VTuber', 1),
('Yelan', 'Genshin Impact', 'Videojuego', 1),
('Raora Panthera', 'Hololive', 'VTuber', 1),
('Heinz Doofenshmirtz', 'Phineas and Ferb', 'Animación', 1),
('Bip', 'Kenshi', 'Videojuego', 1),
('Severus Snape', 'Harry Potter', 'Fantasía', 1),
('Maes Hughes', 'Fullmetal Alchemist', 'Anime', 1),
('Jack Sparrow', 'Piratas del Caribe', 'Película', 1),
('Dominic Toretto', 'Rápidos y Furiosos', 'Película', 1),
('Paul Atreides', 'Dune', 'Ciencia ficción', 2);