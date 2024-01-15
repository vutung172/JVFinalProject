package WarehouseManagement.Service;

import java.util.Scanner;

public interface BaseService<C> {
    void add(C object);
    void update(Scanner sc, C updateObject);
    void delete(C object);
}
