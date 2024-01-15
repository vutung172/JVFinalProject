package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.CategoryService;
import WarehouseManagement.entity.FontConfig.PrintForm;
import WarehouseManagement.entity.Model.Category;
import WarehouseManagement.entity.Model.Product;

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

    public static CategoryServiceImpl getCategoryServiceInstance() {
        if (categoryServiceInstance == null) {
            categoryServiceInstance = new CategoryServiceImpl();
        }
        return categoryServiceInstance;
    }


    //Singleton cho danh sách category
    private static List<Category> categories = categoryIOServiceImpl.readFromFile(getCategoryServiceInstance().getPath());

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
            categoryIOServiceImpl.writeToFile(categories, path);
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
                PrintForm.printTableF("%s | %s | %s | %s \n",
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
                                PrintForm.success("Cập nhật thành công tên cho sản phẩm có ID là: "+updateCategory.getId());
                                break;
                            case 2:
                                updateCategory.inputStatus(sc);
                                PrintForm.success("Cập nhật thành công giá mua cho sản phẩm có ID là: "+updateCategory.getId());
                                break;
                            case 3:
                                updateCategory.inputCategoryDescription(sc);
                                PrintForm.success("Cập nhật thành công giá bán cho sản phẩm có ID là: "+updateCategory.getId());
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
                    categoryIOServiceImpl.writeToFile(categories, path);
                }
            } while (true);
        }
    }

    @Override
    public void delete(Category category) {
        List<Product> products = ProductServiceImpl.getProducts();
        long count = products.stream().filter(p -> p.getCategoryId() == category.getId()).count();
        if (searchCategoryById(category.getId()) == null) {
            PrintForm.warning("Danh mục "+category.getName()+" không tồn tại, không thể xóa");
        } else if (count != 0) {
            PrintForm.warning("Danh mục "+category.getName()+" đã có"+count+" sản phẩm, không thể xóa");
        } else {
            categories.remove(category);
            PrintForm.success("Xóa danh mục "+category.getName()+" thành công");
        }
        categoryIOServiceImpl.writeToFile(categories, path);
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
        return categories.stream().collect(Collectors.toMap(Category::getName, c -> productCountByCategory.getOrDefault(c.getId(), 0L)));
    }

    public Category searchCategoryById(int id) {
        return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}
