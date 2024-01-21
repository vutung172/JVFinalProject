package WarehouseManagement.Main;

import WarehouseManagement.Service.Impl.CategoryServiceImpl;
import WarehouseManagement.Service.Impl.IOServiceImpl;
import WarehouseManagement.Service.IOService;
import WarehouseManagement.Service.Impl.ProductServiceImpl;
import WarehouseManagement.entity.FontConfig.PrintForm;
import WarehouseManagement.entity.Model.Category;
import WarehouseManagement.entity.Model.Product;

import java.util.List;
import java.util.Scanner;

public class CategoryMenu {
    private static final CategoryServiceImpl categoryService = CategoryServiceImpl.getCategoryServiceInstance();
    private static final IOService<Category> categoryIOService = IOServiceImpl.getIoServiceInstance();
    private static final List<Product> products = ProductServiceImpl.getProducts();

    public static void displayMenu(Scanner sc) {
        do {
            PrintForm.categoryMenuln("===== QUẢN LÝ DANH MỤC =====");
            PrintForm.categoryMenuln("1. Thêm mới danh mục");
            PrintForm.categoryMenuln("2. Cập nhật danh mục");
            PrintForm.categoryMenuln("3. Xóa danh mục");
            PrintForm.categoryMenuln("4. Tìm kiếm danh mục theo tên");
            PrintForm.categoryMenuln("5. Thống kê số lượng sản phẩm đang có trong danh mục");
            PrintForm.categoryMenuln("6. Quay lại");
            PrintForm.categoryMenu("Mời nhập vào lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 6) {
                    categoryIOService.writeToFile(CategoryServiceImpl.getCategories(), categoryService.getPath());
                    break;
                } else {
                    switch (choice) {
                        case 1:
                            String selection11;
                            do {
                                Category category = new Category();
                                category.inputData(sc);
                                categoryService.add(category);
                                PrintForm.categoryMenu("Bạn có muốn tiếp tục thêm sản phẩm khác không (Y/N):");
                                selection11 = sc.nextLine();
                            } while (selection11.equalsIgnoreCase("Y"));
                            break;
                        case 2:
                            String selection12;
                            do {
                                try{
                                    PrintForm.categoryMenuln("Nhập vào mã của danh mục muốn cập nhật: ");
                                    int idUpdate = Integer.parseInt(sc.nextLine());
                                    Category updateCategory = categoryService.searchCategoryById(idUpdate);
                                    if (updateCategory == null){
                                        PrintForm.warning("Sản phẩm có mã là: "+idUpdate+" không tồn tại");
                                    } else {
                                        categoryService.update(sc,updateCategory);
                                    }
                                } catch (NumberFormatException nfe){
                                    PrintForm.warning(nfe.getMessage());
                                }
                                PrintForm.categoryMenu("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                                selection12 = sc.nextLine();
                            } while (selection12.equalsIgnoreCase("Y"));
                            break;
                        case 3:
                            String selection13;
                            do {
                                try{
                                    PrintForm.categoryMenuln("Nhập vào mã của danh mục muốn xóa: ");
                                    int idUpdate = Integer.parseInt(sc.nextLine());
                                    Category deleteCategory = categoryService.searchCategoryById(idUpdate);
                                    if (deleteCategory == null){
                                        PrintForm.warning("Sản phẩm có mã là "+idUpdate+" không tồn tại");
                                    } else {
                                        categoryService.delete(deleteCategory);
                                    }
                                } catch (NumberFormatException nfe){
                                    PrintForm.warning(nfe.getMessage());
                                }
                                PrintForm.categoryMenu("Bạn có muốn tiếp tục xóa sản phẩm khác không (Y/N):");
                                selection13 = sc.nextLine();
                            } while (selection13.equalsIgnoreCase("Y"));
                            break;
                        case 4:
                            String selection14;
                            do {
                                PrintForm.categoryMenuln("Nhập vào tên của danh mục muốn tìm kiếm: ");
                                String searchKey = sc.nextLine();
                                List<Category> searchedList = categoryService.searchCategoryByName(searchKey);
                                if (searchedList.isEmpty()){
                                    PrintForm.attention("Không tìm thấy sản phẩm nào có từ khóa chứa: "+ searchKey);
                                } else {
                                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %s \n","Mã","Tên danh mục","Trạng thái","Mô tả");
                                    searchedList.forEach(Category::displayData);
                                }
                                PrintForm.categoryMenu("Bạn có muốn tiếp tục tìm kiếm sản phẩm khác không (Y/N):");
                                selection14 = sc.nextLine();
                            } while (selection14.equalsIgnoreCase("Y"));
                            break;
                        case 5:
                            PrintForm.tableHeaderF("%30s | %-15s \n","Tên danh mục","Số sản phẩm");
                            categoryService.synthesizeCategoryByProductQuantity(products).forEach((k,v) -> PrintForm.tableF("%30s | %-15s \n",k,v));
                            break;
                        default:
                            PrintForm.warning("Lựa chọn không phù hợp");
                    }
                }
            } catch (NumberFormatException nfe) {
                PrintForm.warning("Lựa chọn phải là số nguyên từ 1 đến 6.");
            }
        } while (true);
    }
}
