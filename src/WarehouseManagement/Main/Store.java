package WarehouseManagement.Main;

import WarehouseManagement.Service.Impl.CategoryServiceImpl;
import WarehouseManagement.Service.Impl.ProductServiceImpl;
import WarehouseManagement.entity.Category;
import WarehouseManagement.entity.Product;

import java.util.Scanner;

public class Store{
    public static void main(String[] args)throws Exception{
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("===== QUẢN LÝ KHO =====");
            System.out.println("1. Quản lý danh mục");
            System.out.println("2. Quản lý sản phẩm");
            System.out.println("3. Thoát");
            System.out.print("Mời bạn lựa chọn: ");
            try{
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice){
                    case 1:

                        break;
                    case 2:
                        ProductManagement.displayMenu(sc);
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.err.println("Lựa chọn không phù hợp");
                }
            } catch (NumberFormatException nfe){
                System.err.println(nfe.getMessage());
            }
        }while (true);
    }
}
