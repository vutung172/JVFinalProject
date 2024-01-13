package WarehouseManagement.Service;

import WarehouseManagement.entity.Category;
import WarehouseManagement.entity.Product;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> searchCategoryByName(String name);
    Map<String,Integer> synthesizeCategoryByProductQuantity(List<Product> products);
}
