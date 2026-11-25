CREATE DATABASE IF NOT EXISTS warranty_repair_db;
USE warranty_repair_db;

CREATE TABLE IF NOT EXISTS customers (
  customer_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(15) NOT NULL,
  email VARCHAR(100),
  address VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS products (
  product_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT NOT NULL,
  product_name VARCHAR(100) NOT NULL,
  brand VARCHAR(100),
  serial_number VARCHAR(100) NOT NULL UNIQUE,
  purchase_date DATE NOT NULL,
  warranty_years INT NOT NULL,
  CONSTRAINT fk_products_customer FOREIGN KEY(customer_id) REFERENCES customers(customer_id)
);
CREATE TABLE IF NOT EXISTS repairs (
  repair_id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT NOT NULL,
  issue_description VARCHAR(500) NOT NULL,
  repair_date DATE NOT NULL,
  repair_cost DECIMAL(10,2) DEFAULT 0,
  status VARCHAR(30) NOT NULL,
  remarks VARCHAR(500),
  CONSTRAINT fk_repairs_product FOREIGN KEY(product_id) REFERENCES products(product_id),
  CONSTRAINT chk_repair_status CHECK (status IN ('PENDING','IN_PROGRESS','COMPLETED'))
);

-- Optional demo records. Run once on a fresh database; remove these INSERTs if you want an empty DB.
INSERT INTO customers(name,phone,email,address) VALUES
('Aarav Sharma','9000000001','aarav@example.test','Jaipur'),
('Meera Joshi','9000000002','meera@example.test','Ajmer'),
('Rohan Verma','9000000003','rohan@example.test','Kota'),
('Isha Patel','9000000004','isha@example.test','Udaipur'),
('Kabir Singh','9000000005','kabir@example.test','Jodhpur');
INSERT INTO products(customer_id,product_name,brand,serial_number,purchase_date,warranty_years) VALUES
(1,'Laptop','Dell','DEMO-LAP-001','2025-10-01',2),
(2,'Smartphone','Samsung','DEMO-PHN-002','2022-05-15',1),
(3,'Tablet','Lenovo','DEMO-TAB-003','2025-01-10',1),
(4,'Monitor','LG','DEMO-MON-004','2023-03-20',3),
(5,'Printer','HP','DEMO-PRT-005','2024-08-12',1);
INSERT INTO repairs(product_id,issue_description,repair_date,repair_cost,status,remarks) VALUES
(1,'Keyboard keys not responding','2026-01-10',0,'COMPLETED','Covered in warranty (demo)'),
(2,'Battery drains quickly','2026-02-04',1200,'COMPLETED','Battery replaced'),
(3,'Screen flickering','2026-03-12',0,'PENDING','Awaiting inspection'),
(4,'Display color issue','2026-04-02',800,'IN_PROGRESS','Diagnostics underway'),
(5,'Paper feed problem','2026-05-08',450,'PENDING','Roller inspection needed');
