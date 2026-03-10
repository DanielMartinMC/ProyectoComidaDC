-- Borramos los datos para evitar conflictos si se ejecuta varias veces
DELETE FROM CARRITO_ITEMS;
DELETE FROM CARRITOS;
DELETE FROM USUARIOS_ROLES;
DELETE FROM USUARIOS;
DELETE FROM PLATOS;

-- Insertamos Usuarios
INSERT INTO USUARIOS (id, nombre, apellidos, username, email, telefono, password, direccion, codigo_postal, ciudad, pais, is_deleted, is_suscriptor) VALUES
(1, 'Carlos', 'García', 'carlos', 'carlos@test.com', '611111111', 'password123456', 'Calle Falsa 123', '28001', 'Madrid', 'España', false, false),
(2, 'Ana', 'Martinez', 'ana', 'ana@test.com', '622222222', 'password123456', 'Avenida Principal 45', '08001', 'Barcelona', 'España', false, true),
(3, 'Luis', 'Hernández', 'luis', 'luis@test.com', '633333333', 'password123456', 'Plaza Mayor 1', '41001', 'Sevilla', 'España', false, false);

-- Insertamos los roles para cada usuario en la tabla de unión
-- CORREGIDO: La columna se llama 'rol' según la anotación @Column(name="rol")
INSERT INTO USUARIOS_ROLES (usuario_id, rol) VALUES
(1, 'USER'),
(2, 'USER'),
(3, 'USER');

-- Insertamos Platos
INSERT INTO PLATOS (id, nombre, descripcion, tipo, categoria, precio, variante, cantidad, is_deleted) VALUES
(1, 'Ensalada César', 'Lechuga romana, crutones, queso parmesano y salsa césar casera', 'ALMUERZO', 'ENTRANTE', 12.50, 'ESTANDAR', 50, false),
(2, 'Solomillo a la Pimienta', 'Corte de res tierno bañado en salsa de pimienta negra con guarnición de patatas', 'CENA', 'PRINCIPAL', 24.00, 'ESTANDAR', 20, false),
(3, 'Salmón a la Plancha', 'Filete de salmón fresco con espárragos trigueros y limón', 'ALMUERZO', 'PRINCIPAL', 18.50, 'SIN_GLUTEN', 30, false);

-- Insertamos Carritos
INSERT INTO CARRITOS (id, usuario_id, estado, codigo_cupon, descuento, impuestos_calc, total, is_deleted) VALUES
(1, 1, 'Vacio', NULL, 0.0, 0.0, 0.0, false),
(2, 2, 'Contenido', 'VERANO', 5.0, 2.5, 50.0, false),
(3, 3, 'Contenido', NULL, 0.0, 1.5, 30.0, false);

-- Insertamos Items de Carrito
INSERT INTO CARRITO_ITEMS(carrito_id, plato_id, cantidad, precio_unitario) VALUES
(2, 1, 2, 12.50),
(2, 3, 1, 18.50),
(3, 2, 1, 24.00);
