DELIMITER //
CREATE PROCEDURE ADD_NEW_PRODUCT
    (IN product_id varchar(4),IN product_name nvarchar(30),IN product_importPrice double, IN product_exportPrice double, IN profit double, IN product_des text,IN product_status bit, IN product_categoryId int, IN quantity int,
     OUT result nvarchar(4))
BEGIN
	INSERT INTO products (id,name,importPrice,exportPrice,profit,description,status,categoryId,quantity) VALUES
	                    (product_id,product_name,product_importPrice,product_exportPrice,profit,product_des,product_status,product_categoryId,quantity);
    SET result = product_id;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE UPDATE_PRODUCT
    (IN product_id varchar(4),IN new_name nvarchar(30),IN new_importPrice double, IN new_exportPrice double, IN new_profit double, IN new_des text,IN new_status bit, IN new_categoryId int, IN new_quantity int,
     OUT result bit)
BEGIN
    UPDATE products
    SET name = new_name,
        importPrice =new_importPrice,
        exportPrice = new_exportPrice,
        profit = new_profit,
        description = new_des,
        status = new_status,
        categoryId = new_categoryId,
        quantity = new_quantity
    WHERE id = product_id;
    SET result = 1;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_PRODUCT
(IN product_id int,
 OUT result bit)
BEGIN
    DELETE FROM categories
    WHERE id = product_id;
    SET result = 1;
END;
//
DELIMITER ;