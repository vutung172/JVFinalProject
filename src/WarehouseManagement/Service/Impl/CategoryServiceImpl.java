package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.CategoryService;
import WarehouseManagement.entity.Category;
import WarehouseManagement.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryServiceImpl implements BaseService<Category>, CategoryService {
    //Singleton cho class CategoryService
    private static CategoryService categoryServiceInstance;

    private CategoryServiceImpl() {
    }

    public static CategoryService getCategoryServiceInstance(){
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
    public void add(Category object, List<Category> list) {

    }

    @Override
    public void update(Category oldObject, Category newObject) {

    }

    @Override
    public void delete(Category object, List<Category> list) {

    }

    @Override
    public List<Category> searchCategoryByName(String name, List<Category> categories) {
        return null;
    }

    @Override
    public Map<String, Integer> synthesizeCategoryByProductQuantity(List<Category> categories, List<Product> products) {
        return null;
    }
}
