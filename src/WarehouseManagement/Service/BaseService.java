package WarehouseManagement.Service;

import java.sql.SQLException;
import java.util.Scanner;

public interface BaseService<C> {
    void add(C object);
    void update(Scanner sc, C updateObject) throws SQLException;
    void delete(C object);
}
