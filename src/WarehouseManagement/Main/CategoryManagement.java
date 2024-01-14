package WarehouseManagement.Main;

import WarehouseManagement.Service.Impl.IOServiceImpl;
import WarehouseManagement.Service.Impl.CategoryServiceImpl;
import WarehouseManagement.Service.Impl.ProductServiceImpl;
import WarehouseManagement.entity.Category;
import WarehouseManagement.entity.Product;

import java.util.List;
import java.util.Scanner;

public class CategoryManagement {
    private static final CategoryServiceImpl categoryService = CategoryServiceImpl.getCategoryServiceInstance();
    private static final IOServiceImpl<Category> CATEGORY_IO_SERVICE_IMPL = IOServiceImpl.getIoServiceInstance();
    private static final List<Product> products = ProductServiceImpl.getProducts();

    public static void displayMenu(Scanner sc) {
        do {
            System.out.println("===== QUẢN LÝ DANH MỤC =====");
            System.out.println("1. Thêm mới danh mục");
            System.out.println("2. Cập nhật danh mục");
            System.out.println("3. Xóa danh mục");
            System.out.println("4. Tìm kiếm danh mục theo tên danh mục");
            System.out.println("5. Thống kê số lượng sp đang có trong danh mục");
            System.out.println("6. Quay lại");
            System.out.print("Mời nhập vào lựa chọn của bạn");
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 6) {
                    CATEGORY_IO_SERVICE_IMPL.writeToFile(CategoryServiceImpl.getCategories(), categoryService.getPath());
                    break;
                } else {
                    menuSelection(sc, choice);
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Lựa chọn phải là số nguyên từ 1 đến 6.");
            }

        } while (true);
    }

    public static void menuSelection(Scanner sc, int choice) {
        switch (choice) {
            case 1:
                String selection;
                do {
                    Category category = new Category();
                    category.inputData(sc);
                    categoryService.add(category);
                    System.out.print("Bạn có muốn tiếp tục thêm sản phẩm khác không (Y/N):");
                    selection = sc.nextLine();
                } while (selection.equalsIgnoreCase("Y"));
                break;
            case 2:
                do {
                    try{
                        System.out.println("Nhập vào ID của danh mục muốn cập nhật: ");
                        int idUpdate = Integer.parseInt(sc.nextLine());
                        Category updateCategory = categoryService.searchCategoryById(idUpdate);
                        if (updateCategory == null){
                            System.err.printf("Sản phẩm có ID là: %s không tồn tại.\n",idUpdate);
                        } else {
                            categoryService.update(sc,updateCategory);
                        }
                    } catch (NumberFormatException nfe){
                        System.err.println(nfe.getMessage());
                    }
                    System.out.print("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                    selection = sc.nextLine();
                } while (selection.equalsIgnoreCase("Y"));
                break;
            case 3:
                do {
                    try{
                        System.out.println("Nhập vào ID của danh mục muốn xóa: ");
                        int idUpdate = Integer.parseInt(sc.nextLine());
                        Category deleteCategory = categoryService.searchCategoryById(idUpdate);
                        if (deleteCategory == null){
                            System.err.printf("Sản phẩm có ID là: %s không tồn tại.\n",idUpdate);
                        } else {
                            categoryService.delete(deleteCategory);
                        }
                    } catch (NumberFormatException nfe){
                        System.err.println(nfe.getMessage());
                    }
                    System.out.print("Bạn có muốn tiếp tục xóa sản phẩm khác không (Y/N):");
                    selection = sc.nextLine();
                } while (selection.equalsIgnoreCase("Y"));
                break;
            case 4:
                do {
                    System.out.println("Nhập vào tên của danh mục muốn tìm kiếm: ");
                    String searchKey = sc.nextLine();
                    List<Category> searchedList = categoryService.searchCategoryByName(searchKey);
                    if (searchedList.isEmpty()){
                        System.err.printf("Không tìm thấy sản phẩm nào có từ khóa chứa: %s .\n",searchKey);
                    } else {
                        System.out.println();
                        searchedList.forEach(Category::displayData);
                    }
                    System.out.print("Bạn có muốn tiếp tục tìm kiếm sản phẩm khác không (Y/N):");
                    selection = sc.nextLine();
                } while (selection.equalsIgnoreCase("Y"));
                break;
            case 5:
                System.out.println();
                categoryService.synthesizeCategoryByProductQuantity(products).forEach((k,v) -> System.out.printf("%s | %s \n",k,v));
                break;
            default:
                System.err.println("Lựa chọn không phù hợp");
        }
    }
}
