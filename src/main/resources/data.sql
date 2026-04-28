-- Los DELETE no son necesarios si usas ddl-auto=create-drop en application.properties,
-- pero los dejamos comentados por si acaso.
-- DELETE FROM CARRITO_ITEMS;
-- DELETE FROM CARRITOS;
-- DELETE FROM USUARIOS_ROLES;
-- DELETE FROM USUARIOS;
-- DELETE FROM PLATOS;

-- 1. Insertamos Usuarios
-- Contraseña: password123456
INSERT INTO USUARIOS (nombre, apellidos, username, email, telefono, password, direccion, codigo_postal, ciudad, pais, is_deleted, is_suscriptor) VALUES
    ('Carlos', 'García', 'carlos', 'carlos@test.com', '611111111', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Calle Falsa 123', '28001', 'Madrid', 'España', false, false),
    ('Ana', 'Martinez', 'ana', 'ana@test.com', '622222222', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Avenida Principal 45', '08001', 'Barcelona', 'España', false, true),
    ('Luis', 'Hernández', 'luis', 'luis@test.com', '633333333', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Plaza Mayor 1', '41001', 'Sevilla', 'España', false, false),
    ('Administrador', 'Sistema', 'admin', 'admin@proyectocomida.es', '123456789', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Calle Principal 1', '28001', 'Madrid', 'España', false, false);

-- 2. Insertamos los roles
-- Sabemos que Carlos es el 1, Ana es la 2 y Luis el 3
INSERT INTO USUARIOS_ROLES (usuario_id, rol) VALUES
    (1, 'USER'),
    (2, 'USER'),
    (3, 'USER'),
    (4, 'ADMIN');

-- 3. Insertamos Platos
-- H2 asignará automáticamente: Ensalada(1), Solomillo(2), Salmón(3)
INSERT INTO PLATOS (nombre, descripcion, tipo, categoria, precio, variante, cantidad, is_deleted) VALUES
    ('Ensalada César', 'Lechuga romana, crutones, queso parmesano y salsa césar casera', 'ALMUERZO', 'ENTRANTE', 12.50, 'ESTANDAR', 50, false),
    ('Solomillo a la Pimienta', 'Corte de res tierno bañado en salsa de pimienta negra con guarnición de patatas', 'CENA', 'PRINCIPAL', 24.00, 'ESTANDAR', 20, false),
    ('Salmón a la Plancha', 'Filete de salmón fresco con espárragos trigueros y limón', 'ALMUERZO', 'PRINCIPAL', 18.50, 'SIN_GLUTEN', 30, false);

-- 4. Insertamos Carritos vinculándolos a los usuarios correctos
-- Carrito 1 es de Carlos, Carrito 2 es de Ana, Carrito 3 es de Luis
INSERT INTO CARRITOS (usuario_id, estado, codigo_cupon, descuento, impuestos_calc, total, is_deleted) VALUES
    (1, 'Vacio', NULL, 0.0, 0.0, 0.0, false),
    (2, 'Contenido', 'VERANO', 5.0, 2.5, 50.0, false),
    (3, 'Contenido', NULL, 0.0, 1.5, 30.0, false);

-- 5. Insertamos Items (relacionando el carrito con el plato)
-- Al carrito 2 (de Ana) le metemos 2 ensaladas (plato 1) y 1 salmón (plato 3)
-- Al carrito 3 (de Luis) le metemos 1 solomillo (plato 2)
INSERT INTO CARRITO_ITEMS(carrito_id, plato_id, cantidad, precio_unitario) VALUES
    (2, 1, 2, 12.50),
    (2, 3, 1, 18.50),
    (3, 2, 1, 24.00);