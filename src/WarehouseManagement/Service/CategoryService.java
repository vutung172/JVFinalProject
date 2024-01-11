package WarehouseManagement.Service;

import WarehouseManagement.entity.Category;
import WarehouseManagement.entity.Product;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> searchCategoryByName(String name, List<Category> categories);
    Map<String,Integer> synthesizeCategoryByProductQuantity(List<Category> categories, List<Product> products);
}
