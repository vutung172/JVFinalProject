package WarehouseManagement.Main;

import WarehouseManagement.Service.Impl.IOServiceImpl;
import WarehouseManagement.Service.Impl.ProductServiceImpl;
import WarehouseManagement.entity.Product;

import java.util.List;
import java.util.Scanner;

public class ProductManagement {
    private static ProductServiceImpl productService = ProductServiceImpl.getProductServiceInstance();
    private static IOServiceImpl<Product> productIOServiceImpl = IOServiceImpl.getIoServiceInstance();
    public static void displayMenu(Scanner sc){
        do {
            System.out.print("\u001B[1;35m");
            System.out.println("===== QUẢN LÝ SẢN PHẨM =====");
            System.out.println("1. Thêm mới sản phẩm");
            System.out.println("2. Cập nhật sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Hiển thị sản phẩm theo tên A-Z");
            System.out.println("5. Hiển thị sản phẩm theo lợi nhuận từ cao-thấp");
            System.out.println("6. Tìm kiếm sản phẩm");
            System.out.println("7. Quay lại");
            System.out.print("Mời nhập lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 7){
                    productIOServiceImpl.writeToFile(ProductServiceImpl.getProducts(),productService.getPath());
                    break;
                } else {
                    menuSelection(sc,choice);
                }
            } catch (NumberFormatException nfe){
                System.err.println("Lựa cho phải là số nguyên từ 1 đến 7");
            }
        } while (true);
    }
    public static void menuSelection(Scanner sc,int choice){
        System.out.print("\u001B[32m");
        switch (choice){
            case 1:
                String selection;
                do {
                    Product product = new Product();
                    product.inputData(sc);
                    productService.add(product);
                    System.out.print("Bạn có muốn tiếp tục thêm sản phẩm không (Y/N):");
                    selection = sc.nextLine();
                } while (selection.equalsIgnoreCase("Y"));
                break;
            case 2:
                do {
                    System.out.println("Nhập vào ID muốn cập nhật: ");
                    String idUpdate = sc.nextLine();
                    Product updateProduct = productService.searchProductById(idUpdate);
                    if(updateProduct == null){
                        System.err.printf("Sản phẩm có ID là %s không tồn tại.\n",idUpdate);
                    } else {
                        productService.update(sc,updateProduct);
                    }
                    System.out.print("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                    selection = sc.nextLine();
                } while (selection.equalsIgnoreCase("Y"));
                break;
            case 3:
                do {
                    System.out.println("Nhập vào ID cảu sản phẩm muốn xóa: ");
                    String idDelete = sc.next();
                    Product deleteProduct = productService.searchProductById(idDelete);
                    if (deleteProduct == null){
                        System.err.printf("Sản phẩm có ID là %s không tồn tại.\n",idDelete);
                    } else {
                        productService.delete(deleteProduct);
                        System.out.printf("Đã xóa thành công sản phẩm %s.\n",deleteProduct.getName());
                    }
                    System.out.print("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
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
                    System.out.print("Nhập vào từ khóa bạn muốn tìm kiếm: ");
                    String searchKey = sc.nextLine();
                    List<Product> searchedList = productService.searchAny(searchKey);
                    if (searchedList.isEmpty()){
                        System.out.printf("Không tìm thấy sản phẩm nào có chứa từ khóa: %s.\n",searchKey);
                    } else {
                        searchedList.stream().forEach(Product::displayData);
                    }
                    System.out.print("Bạn có muốn tiếp tục cập nhật sản phẩm khác không (Y/N):");
                    selection = sc.nextLine();
                } while (selection.equalsIgnoreCase("Y"));

                break;
            default:
                System.err.println("Lựa chọn không phù hơp");
        }
    }

}
