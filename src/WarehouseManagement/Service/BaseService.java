package WarehouseManagement.Service;

import WarehouseManagement.entity.Category;

import java.util.List;
import java.util.Scanner;

public interface BaseService<C> {
    void add(C object);
    void update(Scanner sc, C updateObject);
    void delete(C object);
}
