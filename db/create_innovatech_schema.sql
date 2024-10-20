-- Crear el esquema si no existe
DROP SCHEMA IF EXISTS innovatech CASCADE;
CREATE SCHEMA innovatech;

-- Crear las tablas base sin claves foráneas
CREATE TABLE innovatech.role (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR
);

CREATE TABLE innovatech.user (
                                 id SERIAL PRIMARY KEY,
                                 id_card INTEGER UNIQUE,
                                 name VARCHAR,
                                 email VARCHAR UNIQUE,
                                 password VARCHAR,
                                 id_rol INTEGER
);

CREATE TABLE innovatech.plan (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR,
                                 price REAL,
                                 id_plan_payment INTEGER UNIQUE
);

CREATE TABLE innovatech.color (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR
);

CREATE TABLE innovatech.size (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR
);

CREATE TABLE innovatech.state (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR
);

CREATE TABLE innovatech.city (
                                 id SERIAL PRIMARY KEY,
                                 id_state INTEGER,
                                 name VARCHAR
);

CREATE TABLE innovatech.administrative_employee (
                                                    id SERIAL PRIMARY KEY,
                                                    id_user INTEGER
);

CREATE TABLE innovatech.entrepreneurship (
                                             id SERIAL PRIMARY KEY,
                                             id_plan INTEGER,
                                             id_user INTEGER,
                                             name VARCHAR,
                                             logo VARCHAR,
                                             nit INTEGER UNIQUE,
                                             description VARCHAR
);

CREATE TABLE innovatech.client (
                                   id SERIAL PRIMARY KEY,
                                   id_user INTEGER,
                                   id_card VARCHAR
);

CREATE TABLE innovatech.course (
                                   id SERIAL PRIMARY KEY,
                                   content VARCHAR,
                                   description VARCHAR,
                                   date DATE,
                                   score INTEGER
);

CREATE TABLE innovatech.functionality (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR,
                                          description VARCHAR
);

CREATE TABLE innovatech.plan_functionality (
                                               id SERIAL PRIMARY KEY,
                                               id_plan INTEGER,
                                               id_functionality INTEGER
);

CREATE TABLE innovatech.supplier (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR,
                                     description VARCHAR,
                                     contact_number VARCHAR
);

CREATE TABLE innovatech.payment (
                                    id SERIAL PRIMARY KEY,
                                    amount DOUBLE PRECISION,
                                    date DATE,
                                    responsible_entity VARCHAR,
                                    id_card INTEGER
);

CREATE TABLE innovatech."order" (
                                    id SERIAL PRIMARY KEY,
                                    id_client INTEGER,
                                    id_payment INTEGER,
                                    id_city INTEGER,
                                    id_state INTEGER,
                                    sale_number VARCHAR,
                                    additional_info VARCHAR,
                                    address VARCHAR
);

CREATE TABLE innovatech.service (
                                    id SERIAL PRIMARY KEY,
                                    id_inventory INTEGER,
                                    id_entrepreneurship INTEGER,
                                    id_review INTEGER,
                                    name VARCHAR,
                                    price DOUBLE PRECISION,
                                    quantity INTEGER,
                                    picture VARCHAR,
                                    description VARCHAR
);

CREATE TABLE innovatech.review (
                                   id SERIAL PRIMARY KEY,
                                   id_product INTEGER,
                                   id_service INTEGER,
                                   content VARCHAR,
                                   score INTEGER,
                                   id_client INTEGER,
                                   title VARCHAR
);

CREATE TABLE innovatech.product (
                                    id SERIAL PRIMARY KEY,
                                    id_entrepreneurship INTEGER,
                                    quantity INTEGER,
                                    price DOUBLE PRECISION,
                                    cost DOUBLE PRECISION,
                                    picture VARCHAR,
                                    description VARCHAR,
                                    id_color INTEGER,
                                    id_size INTEGER
);

CREATE TABLE innovatech.coupon (
                                   id SERIAL PRIMARY KEY,
                                   id_entrepreneurship INTEGER,
                                   description VARCHAR,
                                   discount DOUBLE PRECISION,
                                   code VARCHAR
);

CREATE TABLE innovatech.subscription (
                                         id SERIAL PRIMARY KEY,
                                         id_entrepreneurship INTEGER,
                                         id_plan INTEGER,
                                         initial_date DATE,
                                         expiration_date DATE,
                                         amount DOUBLE PRECISION
);

CREATE TABLE innovatech.order_state (
                                        id SERIAL PRIMARY KEY,
                                        state VARCHAR
);

CREATE TABLE innovatech.engagement (
                                       id SERIAL PRIMARY KEY,
                                       id_publication INTEGER,
                                       interaction INTEGER
);

CREATE TABLE innovatech.publication (
                                        id SERIAL PRIMARY KEY,
                                        title VARCHAR,
                                        description VARCHAR,
                                        multimedia VARCHAR,
                                        administrative_employee INTEGER
);

CREATE TABLE innovatech.product_supplier (
                                             id SERIAL PRIMARY KEY,
                                             id_product INTEGER,
                                             id_supplier INTEGER
);

CREATE TABLE innovatech.entrepreneurship_course (
                                                    id SERIAL PRIMARY KEY,
                                                    id_entrepreneurship INTEGER,
                                                    id_course INTEGER
);

CREATE TABLE innovatech.tag (
                                id SERIAL PRIMARY KEY,
                                id_order INTEGER,
                                id_product INTEGER,
                                tag VARCHAR
);

-- Agregar restricciones de claves foráneas
ALTER TABLE innovatech.user
    ADD CONSTRAINT fk_user_role FOREIGN KEY (id_rol) REFERENCES innovatech.role(id);

ALTER TABLE innovatech.city
    ADD CONSTRAINT fk_city_state FOREIGN KEY (id_state) REFERENCES innovatech.state(id);

ALTER TABLE innovatech.administrative_employee
    ADD CONSTRAINT fk_admin_user FOREIGN KEY (id_user) REFERENCES innovatech.user(id);

ALTER TABLE innovatech.entrepreneurship
    ADD CONSTRAINT fk_entrepreneurship_plan FOREIGN KEY (id_plan) REFERENCES innovatech.plan(id),
    ADD CONSTRAINT fk_entrepreneurship_user FOREIGN KEY (id_user) REFERENCES innovatech.user(id);

ALTER TABLE innovatech.client
    ADD CONSTRAINT fk_client_user FOREIGN KEY (id_user) REFERENCES innovatech.user(id);

ALTER TABLE innovatech.plan_functionality
    ADD CONSTRAINT fk_plan_functionality_plan FOREIGN KEY (id_plan) REFERENCES innovatech.plan(id),
    ADD CONSTRAINT fk_plan_functionality_functionality FOREIGN KEY (id_functionality) REFERENCES innovatech.functionality(id);

ALTER TABLE innovatech."order"
    ADD CONSTRAINT fk_order_client FOREIGN KEY (id_client) REFERENCES innovatech.client(id),
    ADD CONSTRAINT fk_order_payment FOREIGN KEY (id_payment) REFERENCES innovatech.payment(id),
    ADD CONSTRAINT fk_order_city FOREIGN KEY (id_city) REFERENCES innovatech.city(id),
    ADD CONSTRAINT fk_order_state FOREIGN KEY (id_state) REFERENCES innovatech.state(id);

ALTER TABLE innovatech.service
    ADD CONSTRAINT fk_service_entrepreneurship FOREIGN KEY (id_entrepreneurship) REFERENCES innovatech.entrepreneurship(id);

ALTER TABLE innovatech.review
    ADD CONSTRAINT fk_review_product FOREIGN KEY (id_product) REFERENCES innovatech.product(id),
    ADD CONSTRAINT fk_review_service FOREIGN KEY (id_service) REFERENCES innovatech.service(id),
    ADD CONSTRAINT fk_review_client FOREIGN KEY (id_client) REFERENCES innovatech.client(id);

ALTER TABLE innovatech.product
    ADD CONSTRAINT fk_product_entrepreneurship FOREIGN KEY (id_entrepreneurship) REFERENCES innovatech.entrepreneurship(id),
    ADD CONSTRAINT fk_product_color FOREIGN KEY (id_color) REFERENCES innovatech.color(id),
    ADD CONSTRAINT fk_product_size FOREIGN KEY (id_size) REFERENCES innovatech.size(id);

ALTER TABLE innovatech.coupon
    ADD CONSTRAINT fk_coupon_entrepreneurship FOREIGN KEY (id_entrepreneurship) REFERENCES innovatech.entrepreneurship(id);

ALTER TABLE innovatech.subscription
    ADD CONSTRAINT fk_subscription_entrepreneurship FOREIGN KEY (id_entrepreneurship) REFERENCES innovatech.entrepreneurship(id),
    ADD CONSTRAINT fk_subscription_plan FOREIGN KEY (id_plan) REFERENCES innovatech.plan(id);

ALTER TABLE innovatech.engagement
    ADD CONSTRAINT fk_engagement_publication FOREIGN KEY (id_publication) REFERENCES innovatech.publication(id);

ALTER TABLE innovatech.publication
    ADD CONSTRAINT fk_publication_admin FOREIGN KEY (administrative_employee) REFERENCES innovatech.administrative_employee(id);

ALTER TABLE innovatech.product_supplier
    ADD CONSTRAINT fk_product_supplier_product FOREIGN KEY (id_product) REFERENCES innovatech.product(id),
    ADD CONSTRAINT fk_product_supplier_supplier FOREIGN KEY (id_supplier) REFERENCES innovatech.supplier(id);

ALTER TABLE innovatech.entrepreneurship_course
    ADD CONSTRAINT fk_entrepreneurship_course FOREIGN KEY (id_entrepreneurship) REFERENCES innovatech.entrepreneurship(id),
    ADD CONSTRAINT fk_entrepreneurship_course_course FOREIGN KEY (id_course) REFERENCES innovatech.course(id);

ALTER TABLE innovatech.tag
    ADD CONSTRAINT fk_tag_order FOREIGN KEY (id_order) REFERENCES innovatech."order"(id),
    ADD CONSTRAINT fk_tag_product FOREIGN KEY (id_product) REFERENCES innovatech.product(id);


-------------------------------

-- Asegúrate de que las tablas estén pobladas en el orden correcto para evitar errores de claves foráneas

-- Insertar en la tabla role
INSERT INTO innovatech.role (name)
VALUES ('Administrador'), ('Usuario'), ('Cliente');

-- Insertar en la tabla user
INSERT INTO innovatech.user (id_card, name, email, password, id_rol)
VALUES
    (12345678, 'Juan Perez', 'juan.perez@example.com', 'password123', 1),
    (87654321, 'Maria Lopez', 'maria.lopez@example.com', 'password456', 2);

-- Insertar en la tabla plan
INSERT INTO innovatech.plan (name, price)
VALUES ('Plan Básico', 100.00), ('Plan Premium', 200.00);

-- Insertar en la tabla color
INSERT INTO innovatech.color (name)
VALUES ('Rojo'), ('Azul');

-- Insertar en la tabla size
INSERT INTO innovatech.size (name)
VALUES ('Pequeño'), ('Grande');

-- Insertar en la tabla state
INSERT INTO innovatech.state (name)
VALUES ('Activo'), ('Inactivo');

-- Insertar en la tabla city
INSERT INTO innovatech.city (id_state, name)
VALUES (1, 'Ciudad Tech'), (2, 'Ciudad Innovadora');

-- Insertar en la tabla administrative_employee
INSERT INTO innovatech.administrative_employee (id_user)
VALUES (1);

-- Insertar en la tabla entrepreneurship
INSERT INTO innovatech.entrepreneurship (id_plan, id_user, name, logo, nit, description)
VALUES
    (1, 1, 'Tech Innovate', 'logo.png', 987654321, 'Empresa de tecnología'),
    (2, 2, 'Innovative Solutions', 'logo2.png', 123456789, 'Soluciones innovadoras');

-- Insertar en la tabla client
INSERT INTO innovatech.client (id_user, id_card)
VALUES (1, '87654321'), (2, '12345678');

-- Insertar en la tabla course
INSERT INTO innovatech.course (content, description, date, score)
VALUES
    ('Contenido A', 'Curso de prueba A', '2024-09-01', 5),
    ('Contenido B', 'Curso de prueba B', '2024-09-02', 4);

-- Insertar en la tabla functionality
INSERT INTO innovatech.functionality (name, description)
VALUES ('Funcionalidad A', 'Descripción A');

-- Insertar en la tabla plan_functionality
INSERT INTO innovatech.plan_functionality (id_plan, id_functionality)
VALUES (1, 1);

-- Insertar en la tabla supplier
INSERT INTO innovatech.supplier (name, description, contact_number)
VALUES
    ('Proveedor 1', 'Proveedor de componentes', '123456789'),
    ('Proveedor 2', 'Proveedor de servicios', '987654321');

-- Insertar en la tabla payment
INSERT INTO innovatech.payment (amount, date, responsible_entity, id_card)
VALUES
    (500.00, '2024-09-01', 'Banco X', 12345678),
    (750.00, '2024-09-02', 'Banco Y', 87654321);

-- Insertar en la tabla order
INSERT INTO innovatech."order" (id_client, id_payment, id_city, id_state, sale_number, additional_info, address)
VALUES
    (1, 1, 1, 1, 'ORD123', 'Info adicional', 'Dirección 123');

-- Insertar en la tabla product
INSERT INTO innovatech.product (id_entrepreneurship, quantity, price, cost, picture, description, id_color, id_size)
VALUES
    (1, 100, 20.50, 15.00, 'producto.png', 'Producto A', 1, 1);

-- Insertar en la tabla service
INSERT INTO innovatech.service (id_entrepreneurship, name, price, quantity, picture, description)
VALUES
    (1, 'Servicio A', 100.00, 10, 'servicio.png', 'Servicio de soporte técnico');

-- Insertar en la tabla review
-- Asegúrate que los productos y servicios existan antes de esta inserción
INSERT INTO innovatech.review (id_product, id_service, content, score, id_client, title)
VALUES
    (1, 1, 'Buen producto', 5, 1, 'Recomendado');

-- Insertar en la tabla coupon
INSERT INTO innovatech.coupon (id_entrepreneurship, description, discount, code)
VALUES
    (1, 'Descuento del 10%', 10.00, 'DESC10');

-- Insertar en la tabla subscription
INSERT INTO innovatech.subscription (id_entrepreneurship, id_plan, initial_date, expiration_date, amount)
VALUES
    (1, 1, '2024-09-01', '2025-09-01', 1200.00);

-- Insertar en la tabla order_state
INSERT INTO innovatech.order_state (state)
VALUES ('En proceso'), ('Completado');

-- Insertar en la tabla publication
-- Asegúrate que los registros de administrative_employee existen antes de esta inserción
INSERT INTO innovatech.publication (title, description, multimedia, administrative_employee)
VALUES
    ('Publicación A', 'Descripción A', 'multimedia.png', 1);

-- Insertar en la tabla engagement
-- Asegúrate que la publicación exista antes de esta inserción
INSERT INTO innovatech.engagement (id_publication, interaction)
VALUES (1, 50);

-- Insertar en la tabla product_supplier
INSERT INTO innovatech.product_supplier (id_product, id_supplier)
VALUES (1, 1);

-- Insertar en la tabla entrepreneurship_course
INSERT INTO innovatech.entrepreneurship_course (id_entrepreneurship, id_course)
VALUES (1, 1);

-- Insertar en la tabla tag
INSERT INTO innovatech.tag (id_order, id_product, tag)
VALUES (1, 1, 'Nuevo');