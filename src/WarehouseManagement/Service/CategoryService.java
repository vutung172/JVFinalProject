package WarehouseManagement.Service;

import WarehouseManagement.entity.Model.Category;
import WarehouseManagement.entity.Model.Product;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> searchCategoryByName(String name);
    Map<String,Long> synthesizeCategoryByProductQuantity(List<Product> products);
}
