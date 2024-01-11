package WarehouseManagement.Service;

import WarehouseManagement.entity.Category;

import java.util.List;

public interface BaseService<C> {
    void add(C object, List<C> list);
    void update(C oldObject, C newObject);
    void delete(C object,List<C> list);
}
