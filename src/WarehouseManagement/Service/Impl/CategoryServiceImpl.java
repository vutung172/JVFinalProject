package WarehouseManagement.Service.Impl;

import WarehouseManagement.Database.MySQL.MySQLConnect;
import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.CategoryService;
import WarehouseManagement.Service.DbService;
import WarehouseManagement.Service.IOService;
import WarehouseManagement.entity.FontConfig.PrintForm;
import WarehouseManagement.entity.Model.Category;
import WarehouseManagement.entity.Model.Product;

import java.sql.*;
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
    private static List<Category> categories ;

    private CategoryServiceImpl() {
        categories = downloadFromDb();
    }

    public static CategoryServiceImpl getCategoryServiceInstance() {
        if (categoryServiceInstance == null) {
            categoryServiceInstance = new CategoryServiceImpl();
        }
        return categoryServiceInstance;
    }

    //Singleton cho danh sách category
    public static List<Category> getCategories() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        return categories;
    }

    @Override
    public void add(Category category) {
        if (searchCategoryById(category.getId()) != null) {
            PrintForm.warning("Danh mục " + category.getName() + " đã tồn tại, không thể thêm");
        } else {
            Connection connection = null;
            try {
                // B1. Tạo kết nối
                connection = MySQLConnect.open();
                // B2. Tạo đối tượng thực thi câu truy vấn
                String query = "INSERT INTO categories(name,description,status) VALUES(?,?,?)";
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                // B2.1: Truyền tham số nếu có
                ps.setString(1, category.getName());
                ps.setString(2, category.getDescription());
                ps.setBoolean(3, category.isStatus());
                // B3. Thực thi câu truy vấn
                int result = ps.executeUpdate();
                if (result > 0) {
                    try (ResultSet generateKeys = ps.getGeneratedKeys()) {
                        while (generateKeys.next()) {
                            category.setId(generateKeys.getInt(1));
                        }
                        categories.add(category);
                        PrintForm.success("Đã thêm danh mục " + category.getName() + " thành công");
                    }
                    PrintForm.success("Upload danh mục " + category.getName() + " thành công lên database");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                MySQLConnect.close(connection);
            }
        }
    }

    @Override
    public void update(Scanner sc, Category updateCategory){
        Connection connection = MySQLConnect.open();
        try {
            if (searchCategoryById(updateCategory.getId()) == null) {
                PrintForm.warning("Danh mục " + updateCategory.getName() + " không tồn tại, mời nhập lại");
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
                        int result;
                        PreparedStatement ps;
                        if (choice == 4) {
                            break;
                        } else {
                            switch (choice) {
                                case 1:
                                    updateCategory.inputName(sc);
                                    String updateName = "UPDATE categories SET name = ? WHERE id = ?";
                                    ps = connection.prepareStatement(updateName);
                                    ps.setInt(2, updateCategory.getId());
                                    ps.setString(1, updateCategory.getName());
                                    result = ps.executeUpdate();
                                    if (result > 0)
                                        PrintForm.success("Cập nhật thành công tên cho danh mục có mã là: " + updateCategory.getId());
                                    break;
                                case 2:
                                    updateCategory.inputStatus(sc);
                                    String updateStatus= "UPDATE categories SET status = ? WHERE id = ?";
                                    ps = connection.prepareStatement(updateStatus);
                                    ps.setInt(2, updateCategory.getId());
                                    ps.setBoolean(1, updateCategory.isStatus());
                                    result = ps.executeUpdate();
                                    if (result > 0)
                                        PrintForm.success("Cập nhật thành công trạng thái cho danh mục có mã là: " + updateCategory.getId());
                                    break;
                                case 3:
                                    updateCategory.inputCategoryDescription(sc);
                                    String updateDescription = "UPDATE categories SET description = ? WHERE id = ?";
                                    ps = connection.prepareStatement(updateDescription);
                                    ps.setInt(2, updateCategory.getId());
                                    ps.setString(1, updateCategory.getDescription());
                                    result = ps.executeUpdate();
                                    if (result > 0)
                                        PrintForm.success("Cập nhật thành công tên cho danh mục có mã là: " + updateCategory.getId());
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
                        connection.close();
                    }
                } while (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Category category) {
        long count = ProductServiceImpl.getProducts().stream().filter(p -> p.getCategoryId() == category.getId()).count();
        if (searchCategoryById(category.getId()) == null) {
            PrintForm.warning("Danh mục " + category.getName() + " không tồn tại, không thể xóa");
        } else if (count != 0) {
            PrintForm.warning("Danh mục " + category.getName() + " đang có " + count + " sản phẩm tham chiếu, không thể xóa");
        } else {
            Connection connection = null;
            try {
                connection = MySQLConnect.open();
                String deleteQuery = "DELETE FROM categories WHERE id = ?";
                PreparedStatement ps = connection.prepareStatement(deleteQuery);
                ps.setInt(1,category.getId());
                int resultSet = ps.executeUpdate();
                if (resultSet >0) {
                    categories.remove(category);
                    PrintForm.success("Xóa danh mục " + category.getName() + " thành công");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
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
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Category searchCategoryById(int id) {
        return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Category> downloadFromDb() {
        List<Category> dbCategories = new ArrayList<>();
        Connection connection = MySQLConnect.open();
        if (connection != null) {
            try {
                String dbQuery = "SELECT * FROM categories";
                PreparedStatement ps = connection.prepareStatement(dbQuery);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    Category category = new Category();
                    category.setId(resultSet.getInt("id"));
                    category.setName(resultSet.getNString("name"));
                    category.setDescription(resultSet.getNString("description"));
                    category.setStatus(resultSet.getBoolean("status"));
                    dbCategories.add(category);
                }
                PrintForm.success("download db success");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                MySQLConnect.close(connection);
            }
        }
        return dbCategories;
    }

    @Override
    public void uploadAllToDb() {
        Connection connection = MySQLConnect.open();
        try {
            String uploadQuery = "INSERT INTO categories (name,description,status) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(uploadQuery);
            categories.stream().forEach(c -> {
                try {
                    ps.setString(1, c.getName());
                    ps.setString(2, c.getDescription());
                    ps.setBoolean(3, c.isStatus());
                } catch (SQLException se) {
                    se.getStackTrace();
                }
            });
            int result = ps.executeUpdate();
            if (result > 0) {
                PrintForm.success("Upload " + result + " records to database success");
                categoryIOService.writeToFile(categories, getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLConnect.close(connection);
        }
    }
}
