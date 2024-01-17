package WarehouseManagement.Main;

import WarehouseManagement.Service.Impl.IOServiceImpl;
import WarehouseManagement.Service.Impl.ProductServiceImpl;
import WarehouseManagement.entity.FontConfig.ColorFont;
import WarehouseManagement.entity.FontConfig.PrintForm;
import WarehouseManagement.entity.Model.Product;

import java.util.List;
import java.util.Scanner;

public class ProductMenu {
    private static final ProductServiceImpl productService = ProductServiceImpl.getProductServiceInstance();
    private static final IOServiceImpl<Product> productIOServiceImpl = IOServiceImpl.getIoServiceInstance();
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
            PrintForm.productMenuln("7. Quay lại");
            PrintForm.productMenu("Mời nhập lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 7){
                    productIOServiceImpl.writeToFile(ProductServiceImpl.getProducts(),productService.getPath());
                    break;
                } else {
                    switch (choice){
                        case 1:
                            String selection;
                            do {
                                Product product = new Product();
                                product.inputData(sc);
                                productService.add(product);
                                PrintForm.productMenu("Bạn có muốn tiếp tục thêm sản phẩm không (Y/N):");
                                selection = sc.nextLine();
                            } while (selection.equalsIgnoreCase("Y"));
                            break;
                        case 2:
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
                                selection = sc.nextLine();
                            } while (selection.equalsIgnoreCase("Y"));
                            break;
                        case 3:
                            do {
                                PrintForm.productMenuln("Nhập vào ID của sản phẩm muốn xóa: ");
                                String idDelete = sc.next();
                                Product deleteProduct = productService.searchProductById(idDelete);
                                if (deleteProduct == null){
                                    PrintForm.warning("Sản phẩm có ID là "+idDelete+" Không tồn tại");
                                } else {
                                    productService.delete(deleteProduct);
                                    PrintForm.success("Đã xóa thành công sản phẩm "+deleteProduct.getName());
                                }
                                PrintForm.productMenu("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                                selection = sc.nextLine();
                            } while (selection.equalsIgnoreCase("Y"));
                            break;
                        case 4:
                            productService.displaySortedDataByName("ASC");
                            break;
                        case 5:
                            productService.displaySortedDataByProfit("DESC");
                            break;
                        case 6:
                            do {
                                PrintForm.productMenu("Nhập vào từ khóa bạn muốn tìm kiếm: ");
                                String searchKey = sc.nextLine();
                                List<Product> searchedList = productService.searchAny(searchKey);
                                if (searchedList.isEmpty()){
                                    PrintForm.attention("Không tìm thấy sản phẩm nào có chứa từ khóa: "+searchKey);
                                } else {
                                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %10s | %s \n",
                                            "Mã sp",
                                            "Tên sản phẩm",
                                            "Giá mua (USD)",
                                            "Giá bán (USD)",
                                            "Lợi nhuận (USD)",
                                            "Danh mục sản phẩm",
                                            "Trạng thái",
                                            "Mô tả");
                                    searchedList.stream().forEach(Product::displayData);
                                }
                                PrintForm.productMenu("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                                selection = sc.nextLine();
                            } while (selection.equalsIgnoreCase("Y"));
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
