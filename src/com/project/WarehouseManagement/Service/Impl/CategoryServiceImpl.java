package com.project.WarehouseManagement.Service.Impl;

import com.project.WarehouseManagement.Database.MySQL.MySQLConnect;
import com.project.WarehouseManagement.Service.BaseService;
import com.project.WarehouseManagement.Service.CategoryService;
import com.project.WarehouseManagement.Service.DbService;
import com.project.WarehouseManagement.Service.IOService;
import com.project.WarehouseManagement.entity.FontConfig.PrintForm;
import com.project.WarehouseManagement.entity.Model.Category;
import com.project.WarehouseManagement.entity.Model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements BaseService<Category>, CategoryService, DbService<Category> {
    private static IOService<Category> categoryIOService = IOServiceImpl.getIoServiceInstance();
    private final String fileName = "categories.txt";

    public String getPath() {
        return fileName;
    }

    //Singleton cho class CategoryService
    private static CategoryServiceImpl categoryServiceInstance;

    private CategoryServiceImpl() {
    }

    public static CategoryServiceImpl getCategoryServiceInstance() {
        if (categoryServiceInstance == null) {
            categoryServiceInstance = new CategoryServiceImpl();
        }
        return categoryServiceInstance;
    }


    //Singleton cho danh sách category
    private static List<Category> categories = categoryIOService.readFromFile(getCategoryServiceInstance().getPath());

    public static List<Category> getCategories() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        return categories;
    }

    @Override
    public void add(Category category) {
        if (searchCategoryById(category.getId()) != null) {
            PrintForm.warning("Danh mục "+category.getName()+" đã tồn tại, không thể thêm");
        } else {
            categories.add(category);
            categoryIOService.writeToFile(categories, fileName);
            PrintForm.success("Đã thêm danh mục "+category.getName()+" thành công");
        }
    }

    @Override
    public void update(Scanner sc, Category updateCategory) {
        if (searchCategoryById(updateCategory.getId()) == null) {
            PrintForm.warning("Danh mục "+updateCategory.getName()+" không tồn tại, mời nhập lại");
        } else {
            do {
                PrintForm.categoryMenuln("===== CẬP NHẬT DANH MỤC =====");
                PrintForm.tableHeaderF("%5s | %-30s | %15s | %s \n",
                        "Mã",
                        "Tên danh mục",
                        "Trạng thái",
                        "Mô tả");
                updateCategory.displayData();
                PrintForm.categoryMenuln("1. Cập nhật tên danh mục");
                PrintForm.categoryMenuln("2. Cập nhật trạng thái danh mục");
                PrintForm.categoryMenuln("3. Cập nhật mô tả danh mục");
                PrintForm.categoryMenuln("4. Quay lại");
                PrintForm.categoryMenu("Mời bạn lựa chọn: ");
                try {
                    int choice = Integer.parseInt(sc.nextLine());
                    if (choice == 4) {
                        break;
                    } else {
                        switch (choice) {
                            case 1:
                                updateCategory.inputName(sc);
                                PrintForm.success("Cập nhật thành công tên cho sản phẩm có mã là: "+updateCategory.getId());
                                break;
                            case 2:
                                updateCategory.inputStatus(sc);
                                PrintForm.success("Cập nhật thành công giá mua cho sản phẩm có mã là: "+updateCategory.getId());
                                break;
                            case 3:
                                updateCategory.inputCategoryDescription(sc);
                                PrintForm.success("Cập nhật thành công giá bán cho sản phẩm có mã là: "+updateCategory.getId());
                                break;
                            default:
                                PrintForm.warning("Lựa chọn không phù hợp");
                                break;
                        }
                    }

                } catch (NumberFormatException nfe) {
                    PrintForm.warning("Lựa chọn phải là số nguyên từ 1 đến 7");
                } catch (Exception e) {
                    PrintForm.warning(e.getMessage());
                } finally {
                    categoryIOService.writeToFile(categories, fileName);
                }
            } while (true);
        }
    }

    @Override
    public void delete(Category category) {
        long count = ProductServiceImpl.getProducts().stream().filter(p -> p.getCategoryId() == category.getId()).count();
        if (searchCategoryById(category.getId()) == null) {
            PrintForm.warning("Danh mục "+category.getName()+" không tồn tại, không thể xóa");
        } else if (count != 0) {
            PrintForm.warning("Danh mục "+category.getName()+" đang có "+count+" sản phẩm tham chiếu, không thể xóa");
        } else {
            categories.remove(category);
            PrintForm.success("Xóa danh mục "+category.getName()+" thành công");
        }
        categoryIOService.writeToFile(categories, fileName);
    }

    @Override
    public List<Category> searchCategoryByName(String name) {
        return categories.stream().filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(Category::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> synthesizeCategoryByProductQuantity(List<Product> products) {
        Map<Integer, Long> productCountByCategory = products.stream().collect(Collectors.groupingBy(Product::getCategoryId, Collectors.counting()));
        return categories.stream().collect(Collectors.toMap(Category::getName, c -> productCountByCategory.getOrDefault(c.getId(), 0L)))
                                    .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2) -> e1,LinkedHashMap::new));
    }

    public Category searchCategoryById(int id) {
        return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Category> downloadFromDb() {
        List<Category> dbCategories = new ArrayList<>();
        Connection connection = MySQLConnect.open();
        try{
            String dbQuery = "SELECT * FROM categories";
            PreparedStatement ps = connection.prepareStatement(dbQuery);
            ResultSet resultSet = ps.getResultSet();
            while (resultSet.next()){
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getNString("name"));
                category.setDescription(resultSet.getNString("description"));
                category.setStatus(resultSet.getBoolean("status"));
                dbCategories.add(category);
            }
            if (dbCategories.isEmpty()){
                dbCategories = getCategories();
            } else {
                categoryIOService.writeToFile(dbCategories,fileName);
            }
        } catch (Exception e){
            e.getStackTrace();
        } finally {
            MySQLConnect.close(connection);
        }
        return dbCategories;
    }

    @Override
    public void uploadToDb() {
        Connection connection = MySQLConnect.open();
        try{
            String uploadQuery = "INSERT INTO categories (name,description,status) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(uploadQuery);
            categories.stream().forEach(c ->{
                                        try {
                                            ps.setString(1,c.getName());
                                            ps.setString(2,c.getDescription());
                                            ps.setBoolean(3,c.isStatus());
                                        } catch (SQLException se) {
                                            se.getStackTrace();
                                        }
                                    });
            int result = ps.executeUpdate();
            if (result > 0){
                PrintForm.success("Upload to database success");
            }
        } catch (Exception e){
            e.getStackTrace();
        }
    }
}
