package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.CategoryService;
import WarehouseManagement.entity.Category;
import WarehouseManagement.entity.Product;

import java.util.*;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements BaseService<Category>, CategoryService {
    private static IOServiceImpl<Category> categoryIOServiceImpl = IOServiceImpl.getIoServiceInstance();
    private final String path = "categories.txt";

    public String getPath() {
        return path;
    }

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
    private static List<Category> categories = categoryIOServiceImpl.readFromFile(getCategoryServiceInstance().getPath()) ;
    public static List<Category> getCategories(){
        if (categories == null){
            categories = new ArrayList<>();
        }
        return categories;
    }

    @Override
    public void add(Category category) {
        if (searchCategoryById(category.getId()) != null){
            System.out.printf("Danh mục %s đã tồn tại, không thể thêm",category.getName());
        } else {
            categories.add(category);
            categoryIOServiceImpl.writeToFile(categories,path);
        }
    }

    @Override
    public void update(Scanner sc, Category updateCategory) {
        if (searchCategoryById(updateCategory.getId()) == null){
            System.out.printf("Danh mục %s không tồn tại, mời nhập lại",updateCategory.getName());
        } else {
            do {
                System.out.println("===== CẬP NHẬT DANH MỤC =====");
                System.out.printf("%s | %s | %s | %s \n",
                        "Mã",
                        "Tên danh mục",
                        "Trạng thái",
                        "Mô tả");
                updateCategory.displayData();
                System.out.println("1. Cập nhật tên danh mục");
                System.out.println("2. Cập nhật trạng thái danh mục");
                System.out.println("3. Cập nhật mô tả danh mục");
                System.out.println("4. Quay lại");
                System.out.print("Mời bạn lựa chọn: ");
                try {
                    int choice = Integer.parseInt(sc.nextLine());
                    if (choice == 4) {
                        break;
                    } else {
                        switch (choice) {
                            case 1:
                                updateCategory.inputName(sc);
                                System.out.printf("Cập nhật thành công tên cho sản phẩm có ID là %s.\n", updateCategory.getId());
                                break;
                            case 2:
                                updateCategory.inputStatus(sc);
                                System.out.printf("Cập nhật thành công giá mua cho sản phẩm có ID là %s.\n", updateCategory.getId());
                                break;
                            case 3:
                                updateCategory.inputCategoryDescription(sc);
                                System.out.printf("Cập nhật thành công giá bán cho sản phẩm có ID là %s.\n", updateCategory.getId());
                                break;
                            default:
                                System.err.println("Lựa chọn không phù hợp");
                                break;
                        }
                    }

                } catch (NumberFormatException nfe) {
                    System.err.println("Lựa chọn phải là số nguyên từ 1 đến 7");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    categoryIOServiceImpl.writeToFile(categories,path);
                }
            } while (true);
        }
    }

    @Override
    public void delete(Category category) {
        if (searchCategoryById(category.getId()) == null){
            System.out.printf("Danh mục %s không tồn tại, không thể xóa",category.getName());
        } else {
            categories.remove(category);
            System.out.printf("Xóa danh mục %s thành công",category.getName());
        }
        categoryIOServiceImpl.writeToFile(categories,path);
    }

    @Override
    public List<Category> searchCategoryByName(String name) {
        return categories.stream().filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                                    .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> synthesizeCategoryByProductQuantity(List<Product> products) {
        Map<Integer, Long> productCountByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategoryId, Collectors.counting()));
        return categories.stream().collect(Collectors.toMap(Category::getName,c -> productCountByCategory.getOrDefault(c.getId(),0L)));
    }

    public Category searchCategoryById(int id){
        return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}
