CREATE DATABASE warehouse;
DROP DATABASE WarehouseManagement;
Use warehouse;

CREATE TABLE Categories(
	id int PRIMARY KEY auto_increment,
    name nvarchar(30),
    description text,
    status bit
);

CREATE TABLE Products(
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

ALTER TABLE products 
ADD constraint FK_Category_Id foreign key (categoryId) REFERENCES categories(id);