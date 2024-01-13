package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.CategoryService;
import WarehouseManagement.entity.Category;
import WarehouseManagement.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CategoryServiceImpl implements BaseService<Category>, CategoryService {
    //Singleton cho class CategoryService
    private static CategoryServiceImpl categoryServiceInstance;

    private CategoryServiceImpl() {
    }

    public static CategoryServiceImpl getCategoryServiceInstance(){
        if (categoryServiceInstance == null){
            categoryServiceInstance = new CategoryServiceImpl();
        }
        return categoryServiceInstance;
    }

    //Singleton cho danh sách category
    private static List<Category> categories ;
    public static List<Category> getCategories(){
        if (categories == null){
            categories = new ArrayList<>();
        }
        return categories;
    }

    @Override
    public void add(Category object) {

    }

    @Override
    public void update(Scanner sc, Category updateCategory) {

    }

    @Override
    public void delete(Category object) {

    }

    @Override
    public List<Category> searchCategoryByName(String name) {
        return null;
    }

    @Override
    public Map<String, Integer> synthesizeCategoryByProductQuantity(List<Product> products) {
        return null;
    }
}
