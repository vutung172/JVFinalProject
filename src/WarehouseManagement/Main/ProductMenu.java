package WarehouseManagement.Main;

import WarehouseManagement.Exception.ProductException;
import WarehouseManagement.Service.IOService;
import WarehouseManagement.Service.Impl.CategoryServiceImpl;
import WarehouseManagement.Service.Impl.IOServiceImpl;
import WarehouseManagement.Service.Impl.ProductServiceImpl;
import WarehouseManagement.entity.FontConfig.ColorFont;
import WarehouseManagement.entity.FontConfig.PrintForm;
import WarehouseManagement.entity.Model.Category;
import WarehouseManagement.entity.Model.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProductMenu {
    private static final ProductServiceImpl productService = ProductServiceImpl.getProductServiceInstance();
    private static final CategoryServiceImpl categoryService = CategoryServiceImpl.getCategoryServiceInstance();
    private static final IOService<Product> productIOService = IOServiceImpl.getIoServiceInstance();
    private static final IOService<Category> categoryIOService = IOServiceImpl.getIoServiceInstance();
    public static void displayMenu(Scanner sc){
        do {
            PrintForm.productMenu(ColorFont.BLUE);
            PrintForm.productMenuln("===== QUẢN LÝ SẢN PHẨM =====");
            PrintForm.productMenuln("1. Thêm mới sản phẩm");
            PrintForm.productMenuln("2. Cập nhật sản phẩm");
            PrintForm.productMenuln("3. Xóa sản phẩm");
            PrintForm.productMenuln("4. Hiển thị sản phẩm theo tên A-Z");
            PrintForm.productMenuln("5. Hiển thị sản phẩm theo lợi nhuận từ cao-thấp");
            PrintForm.productMenuln("6. Tìm kiếm sản phẩm");
            PrintForm.productMenuln("7. Nhập kho");
            PrintForm.productMenuln("8. Quay lại");
            PrintForm.productMenu("Mời nhập lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 8){
                    productIOService.writeToFile(ProductServiceImpl.getProducts(),productService.getPath());
                    break;
                } else {
                    switch (choice){
                        case 1:
                            String selection1;
                            do {
                                Product product = new Product();
                                product.inputData(sc);
                                productService.add(product);
                                PrintForm.productMenu("Bạn có muốn tiếp tục thêm sản phẩm không (Y/N):");
                                selection1 = sc.nextLine();
                            } while (selection1.equalsIgnoreCase("Y"));
                            break;
                        case 2:
                            String selection2;
                            do {
                                PrintForm.productMenuln("Nhập vào ID muốn cập nhật: ");
                                String idUpdate = sc.nextLine();
                                Product updateProduct = productService.searchProductById(idUpdate);
                                if(updateProduct == null){
                                    PrintForm.warning("Sản phẩm có ID là "+idUpdate+" Không tồn tại");
                                } else {
                                    productService.update(sc,updateProduct);
                                }
                                PrintForm.productMenu("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                                selection2 = sc.nextLine();
                            } while (selection2.equalsIgnoreCase("Y"));
                            break;
                        case 3:
                            String selection3;
                            do {
                                PrintForm.productMenuln("Nhập vào ID của sản phẩm muốn xóa: ");
                                String idDelete = sc.nextLine();
                                Product deleteProduct = productService.searchProductById(idDelete);
                                if (deleteProduct == null){
                                    PrintForm.warning("Sản phẩm có ID là "+idDelete+" không tồn tại");
                                } else {
                                    productService.delete(deleteProduct);
                                    PrintForm.success("Đã xóa thành công sản phẩm "+deleteProduct.getName());
                                }
                                PrintForm.productMenu("Bạn có muốn tiếp tục xóa sản phẩm khác không (Y/N): ");
                                selection3 = sc.nextLine();
                            } while (selection3.equalsIgnoreCase("Y"));
                            break;
                        case 4:
                            productService.displaySortedDataByName("ASC");
                            break;
                        case 5:
                            productService.displaySortedDataByProfit("DESC");
                            break;
                        case 6:
                            String selection6;
                            do {
                                PrintForm.productMenu("Nhập vào từ khóa bạn muốn tìm kiếm: ");
                                String searchKey = sc.nextLine();
                                List<Product> searchedList = productService.searchAny(searchKey);
                                if (searchedList.isEmpty()){
                                    PrintForm.attention("Không tìm thấy sản phẩm nào có chứa từ khóa: "+searchKey);
                                } else {
                                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %17s | %s \n",
                                            "Mã sp",
                                            "Tên sản phẩm",
                                            "Giá mua (USD)",
                                            "Giá bán (USD)",
                                            "Lợi nhuận (USD)",
                                            "Danh mục sản phẩm",
                                            "Trạng thái",
                                            "Mô tả");
                                    searchedList.stream().sorted(Comparator.comparing(Product::getId)).forEach(Product::displayData);
                                }
                                PrintForm.productMenu("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                                selection6 = sc.nextLine();
                            } while (selection6.equalsIgnoreCase("Y"));
                            break;
                        case 7:
                            String selection7;
                            do {
                                PrintForm.productMenuln("Mời nhập vào tên sản phẩm: ");
                                String productName = sc.nextLine();
                                PrintForm.productMenuln("Mời nhập vào tên danh mục: ");
                                String categoryName = sc.nextLine();
                                PrintForm.productMenuln("Mời nhập vào số lượng sản phẩm: ");
                                int quantity = Integer.parseInt(sc.nextLine());
                                List<Product> foundProduct = productService.searchAny(productName);
                                List<Category> foundCategory = categoryService.searchCategoryByName(categoryName);
                                if (!foundProduct.isEmpty() && !foundCategory.isEmpty()){
                                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %17s | %s \n",
                                            "Mã sp",
                                            "Tên sản phẩm",
                                            "Giá mua (USD)",
                                            "Giá bán (USD)",
                                            "Lợi nhuận (USD)",
                                            "Danh mục sản phẩm",
                                            "Trạng thái",
                                            "Mô tả");
                                    foundProduct.stream().filter(p -> foundCategory.stream().anyMatch(c -> c.getId() == p.getCategoryId())).forEach(Product::displayData);
                                    PrintForm.productMenuln("Chọn mã sản phẩm bạn muốn nhập kho: ");
                                    String idImport = sc.nextLine();
                                    Product product = productService.searchProductById(idImport);
                                    productService.importWarehouse(sc,idImport,product.getCategoryId(),quantity);
                                } else {
                                    if (foundProduct.isEmpty() && foundCategory.isEmpty()){
                                        PrintForm.warning("Sản phẩm và danh mục chưa tồn tại, yêu cầu nhập mới.");
                                        Category category = new Category();
                                        Product product = new Product();

                                        category.inputData(sc);
                                        CategoryServiceImpl.getCategories().add(category);
                                        product.inputData(sc);
                                        ProductServiceImpl.getProducts().add(product);

                                        productService.importWarehouse(sc,product.getId(),product.getCategoryId(),0);
                                    } else {
                                        if (!foundCategory.isEmpty()) {
                                            PrintForm.attention("Sản phẩm chưa tồn tại, yêu cầu thêm mới: ");
                                            Product product = new Product();
                                            product.inputData(sc);
                                            ProductServiceImpl.getProducts().add(product);
                                            productService.importWarehouse(sc,product.getId(), product.getCategoryId(),0);
                                        }
                                        if (!foundProduct.isEmpty()) {
                                            try {
                                                PrintForm.attention("Danh mục chưa tồn tại, yêu cầu thêm mới: ");
                                                Category category = new Category();
                                                category.inputData(sc);
                                                categoryService.add(category);

                                                PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %17s | %s \n",
                                                        "Mã sp",
                                                        "Tên sản phẩm",
                                                        "Giá mua (USD)",
                                                        "Giá bán (USD)",
                                                        "Lợi nhuận (USD)",
                                                        "Danh mục sản phẩm",
                                                        "Trạng thái",
                                                        "Mô tả");
                                                foundProduct.stream().forEach(Product::displayData);
                                                PrintForm.productMenuln("Chọn mã sản phẩm bạn muốn nhập kho: ");
                                                String idImport = sc.nextLine();

                                                Product product = productService.searchProductById(idImport);
                                                product.setCategoryId(category.getId());
                                                productService.importWarehouse(sc,product.getId(), category.getId(), quantity);
                                            } catch (ProductException pe) {
                                                PrintForm.warning(pe.getMessage());
                                            }
                                        }
                                    }
                                }
                                categoryIOService.writeToFile(CategoryServiceImpl.getCategories(),categoryService.getPath());

                                PrintForm.productMenu("Bạn có muốn tiếp tục nhập kho sản phẩm khác không (Y/N): ");
                                selection7 = sc.nextLine();
                            } while (selection7.equalsIgnoreCase("Y"));
                            break;
                        default:
                            PrintForm.warning("Lựa cho không phù hợp");
                    }
                }
            } catch (NumberFormatException nfe){
                PrintForm.warning("Lựa cho phải là số nguyên từ 1 đến 7");
            }
        } while (true);
    }
}
