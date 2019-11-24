<INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('tavo', 'San Martín', 'tavo@gmail.com', '2019-10-21', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('adolfo', 'Guerrero', 'adolfo@gmail.com', '2019-10-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('tavo', 'San Martín', 'tavo@gmail.com', '2019-10-21', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('adolfo', 'Guerrero', 'adolfo@gmail.com', '2019-10-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('tavo', 'San Martín', 'tavo@gmail.com', '2019-10-21', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('adolfo', 'Guerrero', 'adolfo@gmail.com', '2019-10-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('tavo', 'San Martín', 'tavo@gmail.com', '2019-10-21', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('adolfo', 'Guerrero', 'adolfo@gmail.com', '2019-10-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('tavo', 'San Martín', 'tavo@gmail.com', '2019-10-21', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('adolfo', 'Guerrero', 'adolfo@gmail.com', '2019-10-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('tavo', 'San Martín', 'tavo@gmail.com', '2019-10-21', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('adolfo', 'Guerrero', 'adolfo@gmail.com', '2019-10-20', '');

/* productos*/
INSERT INTO productos (nombre, precio, create_at) VALUES('Bateria mapex', 500000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Platillo Crash', 100000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Platillo stack', 80000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Platillo china', 90000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Platillo splash', 50000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Diabolo', 30000, NOW());

/* Facturas */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura productos musicales', null, 1, NOW());
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura productos malabares', 'Malabares cabeza de martillo', 1, NOW());

/* factura items*/
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 3);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(3, 2, 6);

/* Creamos usuarios con sus roles*/
INSERT INTO `users` (username, password, enabled) VALUES ('tavo', '$2a$10$p.pvn/DiNE7WKW7qc9cz0uRqffPAiFzu6XcN2tyGFHBLagzkNwJKC', 1)
INSERT INTO `users` (username, password, enabled) VALUES ('admin', '$2a$10$0kYJUO9.TcWJtrCGLMYsZe2EVH4OMPLbOR0e8FDHkEfcPHiwyZyA2', 1)

INSERT INTO `authorities` (user_id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO `authorities` (user_id, authority) VALUES (2, 'ROLE_ADMIN');
INSERT INTO `authorities` (user_id, authority) VALUES (2, 'ROLE_USER');


