DELIMITER //
CREATE PROCEDURE ADD_NEW_CATEGORY
    (IN category_name nvarchar(30), IN category_des text,IN category_status bit,
     OUT result int)
BEGIN
	INSERT INTO categories (name,description,status) VALUES
	                    (category_name,category_des,category_status);
    SET result = last_insert_id();
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE UPDATE_CATEGORY
    (IN category_id int,IN new_category_name nvarchar(30), IN new_category_des text,IN new_category_status bit,
     OUT result bit)
BEGIN
	UPDATE categories
	SET name = new_category_name,
	    description = new_category_des,
	    status = new_category_status
	WHERE id = category_id;
    SET result = 1;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_CATEGORY
    (IN category_id int,
     OUT result bit)
BEGIN
	DELETE FROM categories
	WHERE id = category_id;
    SET result = 1;
END;
//
DELIMITER ;

