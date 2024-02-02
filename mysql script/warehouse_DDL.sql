CREATE DATABASE warehouse;
DROP DATABASE WarehouseManagement;
Use warehouse;

CREATE TABLE categories(
	id int PRIMARY KEY auto_increment,
    name nvarchar(30),
    description text,
    status bit
);
SELECT * FROM categories;

CREATE TABLE products(
	id varchar(4),
    name nvarchar(30),
    importPrice double,
    exportPrice double,
    profit double,
    description text,
    status bit,
    categoryId int,
    quantity int
);
SELECT * FROM products;


UPDATE categories SET name = 'clothes1' WHERE id = 1;

ALTER TABLE products 
ADD constraint FK_Category_Id foreign key (categoryId) REFERENCES categories(id);