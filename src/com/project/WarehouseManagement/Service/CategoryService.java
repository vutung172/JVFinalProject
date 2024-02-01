package com.project.WarehouseManagement.Service;

import com.project.WarehouseManagement.entity.Model.Category;
import com.project.WarehouseManagement.entity.Model.Product;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> searchCategoryByName(String name);
    Map<String,Long> synthesizeCategoryByProductQuantity(List<Product> products);
}
