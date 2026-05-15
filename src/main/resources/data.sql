-- 1. Insertamos Usuarios
-- Contraseña: password123456
INSERT INTO USUARIOS (nombre, apellidos, username, email, telefono, password, direccion, codigo_postal, ciudad, pais, is_deleted, is_suscriptor, created_at, updated_at) VALUES
       ('Carlos', 'García', 'carlos', 'carlos@test.com', '611111111', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Calle Falsa 123', '28001', 'Madrid', 'España', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Ana', 'Martinez', 'ana', 'ana@test.com', '622222222', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Avenida Principal 45', '08001', 'Barcelona', 'España', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Luis', 'Hernández', 'luis', 'luis@test.com', '633333333', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Plaza Mayor 1', '41001', 'Sevilla', 'España', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Administrador', 'Sistema', 'admin', 'admin@proyectocomida.es', '123456789', '$2a$10$864/MJvzco1RXzz0Gcz6wuvhlrYCpmnY/BDBtTRIgMj/0YxozSlbm', 'Calle Principal 1', '28001', 'Madrid', 'España', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. Insertamos los roles
INSERT INTO USUARIOS_ROLES (usuario_id, rol) VALUES
       (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (4, 'ADMIN');

-- 3. Insertamos Platos
-- Columnas: nombre, descripcion, tipo, categoria, pais, precio, variante, cantidad, is_deleted, is_premium

-- ===================== ESPAÑOL =====================
INSERT INTO PLATOS (nombre, descripcion, tipo, categoria, pais, precio, variante, cantidad, is_deleted, is_premium) VALUES
-- Entrantes
('Gazpacho Andaluz',      'Sopa fría de tomate, pepino, pimiento y ajo con un chorrito de aceite de oliva virgen extra',       'ALMUERZO', 'ENTRANTE', 'ESPANOL',  8.50, 'VEGANO',            50, false, false),
('Patatas Bravas',        'Patatas fritas crujientes con salsa brava picante y alioli casero',                                 'ALMUERZO', 'ENTRANTE', 'ESPANOL',  7.00, 'VEGANO',            60, false, false),
('Pan con Tomate',        'Rebanadas de pan tostado con tomate natural rallado, aceite de oliva y sal',                       'ALMUERZO', 'ENTRANTE', 'ESPANOL',  5.50, 'VEGANO',            80, false, false),
('Ensalada Mixta',        'Lechuga, tomate, cebolla, aceitunas, atún y huevo duro con vinagreta',                             'ALMUERZO', 'ENTRANTE', 'ESPANOL',  9.00, 'ESTANDAR',          50, false, false),
('Croquetas de Jamón',    'Croquetas cremosas de bechamel con jamón ibérico, rebozadas y fritas',                             'CENA',     'ENTRANTE', 'ESPANOL',  9.50, 'ESTANDAR',          40, false, false),
-- Principale
('Cocido Madrileño',      'Guiso tradicional de garbanzos con chorizo, morcilla, tocino y verduras de temporada',             'ALMUERZO', 'PRINCIPAL','ESPANOL', 15.50, 'ESTANDAR',          30, false, false),
('Paella Valenciana',     'Arroz con pollo, conejo, judías verdes y garrofó, cocinado con azafrán y sofrito de tomate',       'ALMUERZO', 'PRINCIPAL','ESPANOL', 17.00, 'SIN_GLUTEN',        25, false, false),
('Tortilla de Patatas',   'Tortilla española jugosa de patata y cebolla pochada a fuego lento',                               'ALMUERZO', 'PRINCIPAL','ESPANOL', 10.00, 'ESTANDAR',          40, false, false),
('Fabada Asturiana',      'Fabes con compango: chorizo, morcilla y lacón ahumado en caldo untuoso',                          'CENA',     'PRINCIPAL','ESPANOL', 16.00, 'ESTANDAR',          20, false, false),
('Bacalao al Pil-pil',    'Bacalao desalado cocinado en aceite de oliva con ajo hasta lograr una salsa cremosa',             'CENA',     'PRINCIPAL','ESPANOL', 19.00, 'SIN_GLUTEN',        20, false, false),
('Cordero Asado',         'Pierna de cordero lechal asada al horno con ajo, romero y vino blanco',                           'CENA',     'PRINCIPAL','ESPANOL', 23.00, 'SIN_GLUTEN',        15, false, true),
('Pulpo a la Gallega',    'Pulpo cocido sobre cachelos con pimentón de la Vera, sal gruesa y aceite de oliva',               'ALMUERZO', 'PRINCIPAL','ESPANOL', 21.00, 'SIN_GLUTEN',        20, false, true),
('Secreto Ibérico',       'Corte de cerdo ibérico a la brasa con romesco y pimientos asados de temporada',                   'CENA',     'PRINCIPAL','ESPANOL', 24.00, 'ESTANDAR',          15, false, true),
-- Postre
('Crema Catalana',        'Crema de yema con vainilla y canela, caramelizada al momento con soplete',                        'CENA',     'POSTRE',   'ESPANOL',  6.50, 'ESTANDAR',          40, false, false),
('Tarta de Santiago',     'Bizcocho húmedo de almendra molida con azúcar glass y la cruz de Santiago',                       'ALMUERZO', 'POSTRE',   'ESPANOL',  5.50, 'SIN_GLUTEN',        30, false, false),
('Arroz con Leche',       'Arroz cremoso cocido en leche entera con canela en rama, limón y azúcar',                         'CENA',     'POSTRE',   'ESPANOL',  5.00, 'ESTANDAR',          35, false, false),
('Churros con Chocolate', 'Churros crujientes acompañados de chocolate caliente espeso para mojar',                          'DESAYUNO', 'POSTRE',   'ESPANOL',  6.00, 'ESTANDAR',          50, false, false),
('Flan de Huevo',         'Flan casero de huevo con caramelo tostado y nata montada',                                        'CENA',     'POSTRE',   'ESPANOL',  5.00, 'ESTANDAR',          40, false, false),
('Pestiños con Miel',     'Masa frita de anís y sésamo bañada en miel de azahar, receta tradicional andaluza',              'MERIENDA', 'POSTRE',   'ESPANOL',  5.50, 'VEGANO',            30, false, false),
('Torrijas Caramelizadas','Pan brioche empapado en leche y huevo, frito y caramelizado con azúcar y canela',                 'MERIENDA', 'POSTRE',   'ESPANOL',  6.50, 'ESTANDAR',          25, false, false);

-- ===================== ITALIANO =====================
INSERT INTO PLATOS (nombre, descripcion, tipo, categoria, pais, precio, variante, cantidad, is_deleted, is_premium) VALUES
-- Entrantes
('Bruschetta al Pomodoro', 'Pan tostado con tomate fresco, ajo, albahaca y aceite de oliva toscano',                          'ALMUERZO', 'ENTRANTE', 'ITALIANO',  7.50, 'VEGANO',            60, false, false),
('Carpaccio di Manzo',     'Finas láminas de ternera cruda con rúcula, parmesano y alcaparras en aceite de trufa',            'CENA',     'ENTRANTE', 'ITALIANO', 13.00, 'SIN_GLUTEN',        30, false, false),
('Burrata con Prosciutto', 'Burrata cremosa con jamón de Parma, tomates cherry y reducción de balsámico',                    'ALMUERZO', 'ENTRANTE', 'ITALIANO', 12.50, 'SIN_GLUTEN',        25, false, false),
('Ribollita',              'Sopa toscana de pan, alubias blancas, col rizada y verduras de temporada rehogadas',              'CENA',     'ENTRANTE', 'ITALIANO',  9.00, 'VEGANO',            30, false, false),
('Caprese Classica',       'Mozzarella di bufala, tomates de temporada y albahaca fresca con aceite de oliva',                'ALMUERZO', 'ENTRANTE', 'ITALIANO',  9.50, 'ESTANDAR',          40, false, false),
-- Principales
('Spaghetti Carbonara',    'Pasta con guanciale crujiente, yema de huevo, pecorino romano y pimienta negra',                  'ALMUERZO', 'PRINCIPAL','ITALIANO', 14.00, 'ESTANDAR',          40, false, false),
('Risotto ai Funghi',      'Arroz arborio cremoso con setas porcini, vino blanco, mantequilla y parmesano',                   'CENA',     'PRINCIPAL','ITALIANO', 15.50, 'ESTANDAR',          30, false, false),
('Pizza Margherita',       'Masa de fermentación lenta con tomate San Marzano, fior di latte y albahaca fresca',              'ALMUERZO', 'PRINCIPAL','ITALIANO', 13.00, 'ESTANDAR',          50, false, false),
('Ossobuco alla Milanese', 'Jarrete de ternera estofado con gremolata de limón y azafrán sobre risotto',                     'CENA',     'PRINCIPAL','ITALIANO', 22.00, 'ESTANDAR',          15, false, false),
('Lasagna al Forno',       'Capas de pasta con ragú boloñés, bechamel cremosa y parmesano gratinado',                        'ALMUERZO', 'PRINCIPAL','ITALIANO', 16.00, 'ESTANDAR',          30, false, false),
('Branzino al Sale',       'Lubina entera cocinada en costra de sal con hierbas mediterráneas y limón',                      'CENA',     'PRINCIPAL','ITALIANO', 26.00, 'SIN_GLUTEN',        15, false, true),
('Bistecca Fiorentina',    'Chuleta de buey florentino a la brasa, de un kilo, con sal, aceite y romero',                    'CENA',     'PRINCIPAL','ITALIANO', 38.00, 'SIN_GLUTEN',        10, false, true),
('Tagliatelle al Tartufo', 'Pasta fresca artesanal con trufa negra, mantequilla y parmesano 36 meses',                      'CENA',     'PRINCIPAL','ITALIANO', 29.00, 'ESTANDAR',          15, false, true),
-- Postres
('Tiramisú Classico',      'Capas de bizcocho savoiardo empapado en café y mascarpone con cacao puro',                       'CENA',     'POSTRE',   'ITALIANO',  7.00, 'ESTANDAR',          40, false, false),
('Panna Cotta',            'Crema de nata cuajada con vainilla de Madagascar y coulis de frutos rojos',                      'CENA',     'POSTRE',   'ITALIANO',  6.50, 'SIN_GLUTEN',        35, false, false),
('Cannoli Siciliani',      'Tubos de masa frita rellenos de ricotta dulce con pistachos y naranja confitada',                'MERIENDA', 'POSTRE',   'ITALIANO',  6.00, 'ESTANDAR',          30, false, false),
('Gelato al Pistacchio',   'Helado artesanal de pistacho de Bronte con textura cremosa y sabor intenso',                     'MERIENDA', 'POSTRE',   'ITALIANO',  5.50, 'SIN_GLUTEN',        50, false, false),
('Torta della Nonna',      'Tarta de crema pastelera con piñones y limón, receta de la abuela toscana',                     'ALMUERZO', 'POSTRE',   'ITALIANO',  6.50, 'ESTANDAR',          25, false, false),
('Sfogliatelle',           'Pastel hojaldrado napolitano relleno de ricotta, naranja y canela recién horneado',              'MERIENDA', 'POSTRE',   'ITALIANO',  5.50, 'ESTANDAR',          30, false, false),
('Zabaione al Marsala',    'Crema batida de yemas, azúcar y vino Marsala servida tibia con bizcochos',                       'CENA',     'POSTRE',   'ITALIANO',  7.50, 'SIN_GLUTEN',        20, false, false);

-- ===================== MEXICANO =====================
INSERT INTO PLATOS (nombre, descripcion, tipo, categoria, pais, precio, variante, cantidad, is_deleted, is_premium) VALUES
-- Entrantes
('Guacamole con Totopos',  'Aguacate machacado con chile serrano, tomate, cebolla morada, cilantro y lima',                   'ALMUERZO', 'ENTRANTE', 'MEXICANO',  8.00, 'VEGANO',            60, false, false),
('Elotes a la Braza',      'Mazorca de maíz a la brasa untada con mayonesa, queso cotija, chile en polvo y lima',            'ALMUERZO', 'ENTRANTE', 'MEXICANO',  7.50, 'ESTANDAR',          40, false, false),
('Sopa de Lima',           'Caldo de lima yucateco con pollo desmenuzado, tortilla frita y chile habanero',                  'ALMUERZO', 'ENTRANTE', 'MEXICANO',  9.00, 'SIN_GLUTEN',        30, false, false),
('Quesadillas de Flor',    'Tortillas de maíz con flor de calabaza, quesillo oaxaqueño y epazote',                          'ALMUERZO', 'ENTRANTE', 'MEXICANO',  9.50, 'ESTANDAR',          35, false, false),
('Tostadas de Tinga',      'Tostadas crujientes con tinga de pollo, crema, lechuga, aguacate y queso fresco',               'ALMUERZO', 'ENTRANTE', 'MEXICANO',  9.00, 'ESTANDAR',          35, false, false),
-- Principales
('Tacos al Pastor',        'Tacos de cerdo marinado con achiote, piña asada, cilantro y cebolla en tortilla de maíz',       'ALMUERZO', 'PRINCIPAL','MEXICANO', 13.00, 'ESTANDAR',          50, false, false),
('Mole Poblano',           'Pechuga de pavo en salsa mole con más de 30 ingredientes, arroz y frijoles refritos',           'ALMUERZO', 'PRINCIPAL','MEXICANO', 17.00, 'SIN_GLUTEN',        25, false, false),
('Enchiladas Verdes',      'Tortillas rellenas de pollo bañadas en salsa de tomatillo con crema y queso',                   'ALMUERZO', 'PRINCIPAL','MEXICANO', 14.50, 'ESTANDAR',          30, false, false),
('Chiles en Nogada',       'Chile poblano relleno de picadillo frutal con salsa de nuez, granada y perejil',                'CENA',     'PRINCIPAL','MEXICANO', 18.00, 'ESTANDAR',          20, false, false),
('Cochinita Pibil',        'Cerdo marinado en achiote y naranja agria, cocinado en hojas de plátano al horno de tierra',    'ALMUERZO', 'PRINCIPAL','MEXICANO', 16.50, 'SIN_GLUTEN',        25, false, false),
('Camarones a la Diabla',  'Camarones en salsa roja picante con chile de árbol, ajo, mantequilla y limón',                 'CENA',     'PRINCIPAL','MEXICANO', 19.00, 'SIN_GLUTEN',        20, false, false),
('Birria de Res',          'Estofado de ternera con chile guajillo y especias, servido con consomé y tortillas',            'CENA',     'PRINCIPAL','MEXICANO', 20.00, 'SIN_GLUTEN',        20, false, true),
('Huachinango a la Veracruzana', 'Pargo rojo con salsa de tomate, aceitunas, alcaparras y chiles güeros',                  'CENA',     'PRINCIPAL','MEXICANO', 24.00, 'SIN_GLUTEN',        15, false, true),
-- Postres
('Churros con Cajeta',     'Churros crujientes bañados en cajeta de cabra y crema mexicana',                               'MERIENDA', 'POSTRE',   'MEXICANO',  6.50, 'ESTANDAR',          40, false, false),
('Tres Leches',            'Bizcocho empapado en leche evaporada, condensada y crema, con nata y canela',                   'CENA',     'POSTRE',   'MEXICANO',  6.00, 'ESTANDAR',          35, false, false),
('Flan Napolitano',        'Flan cremoso de queso crema y leche condensada con caramelo dorado',                            'CENA',     'POSTRE',   'MEXICANO',  5.50, 'SIN_GLUTEN',        35, false, false),
('Pastel de Elote',        'Bizcocho dulce de maíz tierno con crema y canela, textura húmeda y esponjosa',                 'MERIENDA', 'POSTRE',   'MEXICANO',  5.50, 'ESTANDAR',          30, false, false),
('Nieves de Limón',        'Sorbete artesanal de limón real con ralladura y menta fresca',                                  'MERIENDA', 'POSTRE',   'MEXICANO',  4.50, 'VEGANO',            50, false, false),
('Buñuelos con Piloncillo','Buñuelos crujientes con jarabe de piloncillo, canela y anís estrellado',                        'MERIENDA', 'POSTRE',   'MEXICANO',  5.00, 'VEGANO',            30, false, false),
('Marquesitas Yucatecas',  'Crêpe enrollado con queso Edam derretido y cajeta, crujiente por fuera',                       'MERIENDA', 'POSTRE',   'MEXICANO',  5.50, 'ESTANDAR',          25, false, true);

-- ===================== JAPONÉS =====================
INSERT INTO PLATOS (nombre, descripcion, tipo, categoria, pais, precio, variante, cantidad, is_deleted, is_premium) VALUES
-- Entrantes
('Edamame con Sal Marina', 'Vainas de soja joven hervidas y aliñadas con sal marina en escamas',                            'ALMUERZO', 'ENTRANTE', 'JAPONES',   6.00, 'VEGANO',            60, false, false),
('Gyozas de Cerdo',        'Empanadillas japonesas rellenas de cerdo y col, plancha y al vapor con salsa ponzu',            'ALMUERZO', 'ENTRANTE', 'JAPONES',   9.50, 'ESTANDAR',          40, false, false),
('Miso Shiru',             'Sopa de miso blanco con tofu sedoso, wakame, cebolleta y dashi de bonito',                     'ALMUERZO', 'ENTRANTE', 'JAPONES',   7.00, 'VEGANO',            50, false, false),
('Tataki de Atún',         'Lomo de atún rojo marcado a la llama con ponzu, sésamo y daikon rallado',                      'CENA',     'ENTRANTE', 'JAPONES',  14.00, 'SIN_GLUTEN',        25, false, false),
('Takoyaki',               'Bolas de masa con trozos de pulpo, salsa tonkatsu, mayonesa japonesa y bonito seco',           'ALMUERZO', 'ENTRANTE', 'JAPONES',   9.00, 'ESTANDAR',          35, false, false),
-- Principales
('Ramen Tonkotsu',         'Caldo cremoso de hueso de cerdo con chashu, huevo marinado, nori y bambú',                     'CENA',     'PRINCIPAL','JAPONES',  15.00, 'ESTANDAR',          30, false, false),
('Sushi Omakase (8 pzs)',  'Selección del chef de 8 nigiris con pescado del día, wasabi fresco y jengibre encurtido',      'CENA',     'PRINCIPAL','JAPONES',  22.00, 'SIN_GLUTEN',        20, false, false),
('Katsu Curry',            'Chuleta de cerdo empanada sobre arroz japonés con curry de verduras especiado',                'ALMUERZO', 'PRINCIPAL','JAPONES',  16.00, 'ESTANDAR',          25, false, false),
('Teriyaki de Salmón',     'Filete de salmón glaseado con salsa teriyaki casera, arroz y ensalada de pepino',              'ALMUERZO', 'PRINCIPAL','JAPONES',  18.00, 'SIN_GLUTEN',        25, false, false),
('Yakitori Moriawase',     'Brochetas variadas de pollo a la brasa: muslo, piel, corazón y espárrago con tare',            'CENA',     'PRINCIPAL','JAPONES',  17.00, 'SIN_GLUTEN',        20, false, false),
('Wagyu Teppanyaki',       'Ternera Wagyu A5 a la plancha de hierro con setas shiitake, verduras y salsa ponzu',           'CENA',     'PRINCIPAL','JAPONES',  48.00, 'SIN_GLUTEN',        10, false, true),
('Kaiseki Primavera',      'Menú degustación de 7 pasos con ingredientes de temporada, técnica kaiseki tradicional',       'CENA',     'PRINCIPAL','JAPONES',  55.00, 'SIN_GLUTEN',         8, false, true),
('Unagi Kabayaki',         'Anguila japonesa glaseada en salsa kabayaki sobre arroz con sansho en polvo',                  'CENA',     'PRINCIPAL','JAPONES',  32.00, 'SIN_GLUTEN',        12, false, true),
-- Postres
('Mochi de Matcha',        'Bolas de arroz glutinoso rellenas de helado de matcha ceremonial',                             'MERIENDA', 'POSTRE',   'JAPONES',   6.50, 'SIN_GLUTEN',        40, false, false),
('Dorayaki',               'Dos tortitas esponjosas de miel rellenas de pasta dulce de judía azuki',                       'MERIENDA', 'POSTRE',   'JAPONES',   5.50, 'ESTANDAR',          35, false, false),
('Anmitsu',                'Gelatina de agar con pasta de judía roja, mochi, frutas y almíbar de kuromitsu',               'MERIENDA', 'POSTRE',   'JAPONES',   6.00, 'VEGANO',            30, false, false),
('Taiyaki de Crema',       'Pastel en forma de pez relleno de crema pastelera de vainilla, recién horneado',              'MERIENDA', 'POSTRE',   'JAPONES',   5.00, 'ESTANDAR',          40, false, false),
('Kakigori de Fresa',      'Hielo raspado ultra fino con sirope de fresa, leche condensada y mochi de fresa',              'MERIENDA', 'POSTRE',   'JAPONES',   6.50, 'VEGANO',            35, false, false),
('Warabi Mochi',           'Mochi de helecho con kinako tostado y sirope de kuromitsu, textura gelatinosa única',          'CENA',     'POSTRE',   'JAPONES',   7.00, 'VEGANO',            25, false, false),
('Parfait de Matcha',      'Copa con helado de matcha, granola, pasta de azuki, nata y polvo de té verde ceremonial',      'CENA',     'POSTRE',   'JAPONES',   8.00, 'ESTANDAR',          20, false, true);

-- ===================== INDIO =====================
INSERT INTO PLATOS (nombre, descripcion, tipo, categoria, pais, precio, variante, cantidad, is_deleted, is_premium) VALUES
-- Entrantes
('Samosas de Patata',      'Empanadillas triangulares crujientes rellenas de patata especiada con comino y guisantes',     'ALMUERZO', 'ENTRANTE', 'INDIO',     7.00, 'VEGANO',            50, false, false),
('Pakoras de Verdura',     'Fritos de garbanzos con cebolla, espinacas y chiles verdes, servidos con chutney de menta',   'ALMUERZO', 'ENTRANTE', 'INDIO',     8.00, 'VEGANO',            40, false, false),
('Sopa de Dhal',           'Crema suave de lentejas rojas con cúrcuma, jengibre, comino y leche de coco',                 'ALMUERZO', 'ENTRANTE', 'INDIO',     8.50, 'VEGANO',            40, false, false),
('Papadum con Chutneys',   'Crackers de lenteja crujientes servidos con tres chutneys: menta, tamarindo y coco',          'ALMUERZO', 'ENTRANTE', 'INDIO',     6.00, 'VEGANO',            60, false, false),
('Aloo Tikki Chaat',       'Croquetas de patata sobre base de garbanzos con yogur, chutneys y chaat masala',              'ALMUERZO', 'ENTRANTE', 'INDIO',     9.00, 'VEGANO',            35, false, false),
-- Principales
('Pollo Tikka Masala',     'Pollo marinado en yogur y especias, en salsa cremosa de tomate, nata y garam masala',         'ALMUERZO', 'PRINCIPAL','INDIO',    15.00, 'ESTANDAR',          35, false, false),
('Dal Makhani',            'Lentejas negras cocinadas toda la noche con mantequilla, nata y especias de Punjab',          'ALMUERZO', 'PRINCIPAL','INDIO',    13.00, 'VEGANO',            30, false, false),
('Cordero Rogan Josh',     'Pierna de cordero estofada con chile de Cachemira, yogur y aromáticas especias',              'CENA',     'PRINCIPAL','INDIO',    18.00, 'ESTANDAR',          20, false, false),
('Palak Paneer',           'Queso fresco indio en salsa densa de espinacas con jengibre, ajo y cúrcuma',                  'ALMUERZO', 'PRINCIPAL','INDIO',    13.50, 'VEGANO',            30, false, false),
('Biryani de Cordero',     'Arroz basmati con cordero especiado, azafrán, frutos secos y hierbas frescas',                'ALMUERZO', 'PRINCIPAL','INDIO',    17.00, 'SIN_GLUTEN',        25, false, false),
('Butter Chicken Royal',   'Pollo tandoori en salsa de mantequilla ahumada con fenugreco y nata doble',                   'CENA',     'PRINCIPAL','INDIO',    19.00, 'SIN_GLUTEN',        20, false, true),
('Pescado Kerala',         'Curry de pescado con leche de coco, curry en hojas, tamarindo y especias del sur de India',  'CENA',     'PRINCIPAL','INDIO',    21.00, 'SIN_GLUTEN',        15, false, true),
('Dum Biryani Premium',    'Biryani de cordero cocinado en vasija sellada con especias de Hyderabad y azafrán iraní',     'CENA',     'PRINCIPAL','INDIO',    26.00, 'SIN_GLUTEN',        12, false, true),
-- Postres
('Gulab Jamun',            'Bolitas de leche reducida fritas, bañadas en almíbar de rosas y cardamomo',                   'CENA',     'POSTRE',   'INDIO',     5.50, 'ESTANDAR',          45, false, false),
('Kheer de Arroz',         'Pudín de arroz cocido en leche con cardamomo, azafrán, pistachos y pétalos de rosa',          'CENA',     'POSTRE',   'INDIO',     6.00, 'SIN_GLUTEN',        35, false, false),
('Kulfi de Pistacho',      'Helado indio denso de leche reducida con pistachos, cardamomo y agua de rosas',               'MERIENDA', 'POSTRE',   'INDIO',     6.50, 'SIN_GLUTEN',        30, false, false),
('Jalebi',                 'Espirales de masa fermentada fritas y bañadas en almíbar de azafrán, crujientes y dulces',    'MERIENDA', 'POSTRE',   'INDIO',     5.00, 'ESTANDAR',          35, false, false),
('Rasmalai',               'Bolas de queso fresco en leche perfumada con azafrán, cardamomo y pistachos laminados',       'CENA',     'POSTRE',   'INDIO',     6.50, 'SIN_GLUTEN',        25, false, false),
('Halwa de Zanahoria',     'Zanahoria rallada cocida en ghee, leche, azúcar y cardamomo, con frutos secos',               'MERIENDA', 'POSTRE',   'INDIO',     5.50, 'SIN_GLUTEN',        30, false, false),
('Shahi Tukda',            'Pan frito en ghee empapado en leche con azafrán y cubierto de rabri y pistachos',             'CENA',     'POSTRE',   'INDIO',     7.00, 'ESTANDAR',          20, false, false);

-- ===================== GRIEGO =====================
INSERT INTO PLATOS (nombre, descripcion, tipo, categoria, pais, precio, variante, cantidad, is_deleted, is_premium) VALUES
-- Entrantes
('Tzatziki con Pita',      'Yogur griego con pepino, ajo, eneldo y menta, servido con pan de pita caliente',              'ALMUERZO', 'ENTRANTE', 'GRIEGO',    7.50, 'ESTANDAR',          60, false, false),
('Spanakopita',            'Hojaldre crujiente relleno de espinacas, queso feta, cebolla y huevo',                        'ALMUERZO', 'ENTRANTE', 'GRIEGO',    8.50, 'ESTANDAR',          40, false, false),
('Dolmades',               'Hojas de parra rellenas de arroz, piñones, pasas y hierbas, servidas con limón',              'ALMUERZO', 'ENTRANTE', 'GRIEGO',    9.00, 'VEGANO',            35, false, false),
('Taramasalata',           'Crema de huevas de bacalao con miga de pan, limón y aceite de oliva del Peloponeso',          'ALMUERZO', 'ENTRANTE', 'GRIEGO',    8.00, 'ESTANDAR',          30, false, false),
('Saganaki',               'Queso kefalograviera frito en sartén con limón, crujiente por fuera y fundente por dentro',   'ALMUERZO', 'ENTRANTE', 'GRIEGO',    9.50, 'SIN_GLUTEN',        35, false, false),
-- Principales
('Moussaka Tradicional',   'Capas de berenjena, carne picada especiada y bechamel gratinada al horno',                   'ALMUERZO', 'PRINCIPAL','GRIEGO',   15.00, 'ESTANDAR',          30, false, false),
('Souvlaki de Cordero',    'Brochetas de cordero marinado en limón y orégano, con pita, tzatziki y cebolla',              'ALMUERZO', 'PRINCIPAL','GRIEGO',   16.00, 'ESTANDAR',          30, false, false),
('Pastitsio',              'Pasta larga con carne de ternera especiada y bechamel horneada, versión griega de la lasaña', 'ALMUERZO', 'PRINCIPAL','GRIEGO',   14.50, 'ESTANDAR',          25, false, false),
('Stifado de Conejo',      'Conejo estofado con cebollitas perla, vino tinto, canela y clavo al estilo chipriota',        'CENA',     'PRINCIPAL','GRIEGO',   17.00, 'SIN_GLUTEN',        20, false, false),
('Briam',                  'Verduras de temporada asadas al horno con aceite de oliva, ajo y hierbas del monte',          'ALMUERZO', 'PRINCIPAL','GRIEGO',   12.00, 'VEGANO',            30, false, false),
('Pulpo a la Brasa',       'Tentáculos de pulpo a la brasa con aceite de oliva, vinagre de vino y alcaparras',            'CENA',     'PRINCIPAL','GRIEGO',   22.00, 'SIN_GLUTEN',        15, false, true),
('Arnaki Kleftiko',        'Pierna de cordero cocinada en papillote con patatas, queso feta, aceitunas y hierbas',        'CENA',     'PRINCIPAL','GRIEGO',   26.00, 'SIN_GLUTEN',        12, false, true),
('Lavraki sto Fourno',     'Lubina salvaje al horno con patatas, tomates, aceitunas negras y aceite de oliva virgen',     'CENA',     'PRINCIPAL','GRIEGO',   28.00, 'SIN_GLUTEN',        10, false, true),
-- Postres
('Baklava de Pistachos',   'Hojaldre fino con pistachos de Egina, miel de tomillo y agua de azahar',                     'MERIENDA', 'POSTRE',   'GRIEGO',    6.00, 'ESTANDAR',          40, false, false),
('Galaktoboureko',         'Sémola cremosa en hojaldre crujiente bañada en almíbar de limón y canela',                   'CENA',     'POSTRE',   'GRIEGO',    6.50, 'ESTANDAR',          30, false, false),
('Loukoumades',            'Buñuelos esponjosos bañados en miel de Creta con canela y nueces troceadas',                  'MERIENDA', 'POSTRE',   'GRIEGO',    5.50, 'ESTANDAR',          40, false, false),
('Rizogalo',               'Arroz con leche griego aromatizado con canela, piel de limón y vainilla',                    'CENA',     'POSTRE',   'GRIEGO',    5.00, 'ESTANDAR',          35, false, false),
('Halva de Sémola',        'Postre de sémola tostada en aceite con miel, almendras y canela, receta tradicional',        'MERIENDA', 'POSTRE',   'GRIEGO',    5.00, 'VEGANO',            30, false, false),
('Kourabiedes',            'Galletas de mantequilla y almendra con anís, cubiertas de azúcar glass, receta navideña',    'MERIENDA', 'POSTRE',   'GRIEGO',    5.50, 'ESTANDAR',          35, false, false),
('Portokalopita',          'Bizcocho jugoso de naranja con hojaldre y almíbar de canela y clavo',                        'MERIENDA', 'POSTRE',   'GRIEGO',    6.00, 'ESTANDAR',          25, false, false);

-- 4. Insertamos Métodos de Pago
INSERT INTO METODOS_PAGO (tipo, numero_tarjeta, fecha_expiracion, is_default, usuario_id, saldo_disponible, created_at, updated_at) VALUES
                                                                                                                                        ('TARJETA_CREDITO', '************1111', '12/25', true,  1, 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                        ('TARJETA_DEBITO',  '************2222', '06/26', false, 1, 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                        ('PAYPAL',          'ana@paypal.com',   'N/A',   true,  2, 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                        ('TARJETA_CREDITO', '************3333', '01/24', true,  3, 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 5. Actualizamos el usuario con su método de pago por defecto
UPDATE USUARIOS SET default_metodo_pago_id = 1 WHERE id = 1;
UPDATE USUARIOS SET default_metodo_pago_id = 3 WHERE id = 2;
UPDATE USUARIOS SET default_metodo_pago_id = 4 WHERE id = 3;

-- 6. Insertamos Carritos
INSERT INTO CARRITOS (usuario_id, estado, codigo_cupon, descuento, impuestos_calc, total, is_deleted) VALUES
                                                                                                          (1, 'Vacio',     NULL,     0.0, 0.0, 0.0,  false),
                                                                                                          (2, 'Contenido', 'VERANO', 5.0, 2.5, 50.0, false),
                                                                                                          (3, 'Contenido', NULL,     0.0, 1.5, 30.0, false);

-- 7. Insertamos Items de carrito
INSERT INTO CARRITO_ITEMS(carrito_id, plato_id, cantidad, precio_unitario) VALUES
                                                                               (2, 1, 2, 8.50),
                                                                               (2, 3, 1, 9.00),
                                                                               (3, 2, 1, 7.00);

-- 8. Insertamos Pedidos
INSERT INTO PEDIDOS (fecha_pedido, estado, total, usuario_id, direccion, metodo_pago_id) VALUES
                                                                                             (CURRENT_TIMESTAMP, 'Completado', 26.00, 2, 'Avenida Principal 45', 3),
                                                                                             (CURRENT_TIMESTAMP, 'EnProceso',  7.00,  3, 'Plaza Mayor 1',        4);