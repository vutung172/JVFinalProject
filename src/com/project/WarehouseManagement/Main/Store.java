package com.project.WarehouseManagement.Main;


import com.project.WarehouseManagement.entity.FontConfig.PrintForm;

import java.util.Scanner;

public class Store{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        do {
            PrintForm.mainMenuln("===== QUẢN LÝ KHO =====");
            PrintForm.mainMenuln("1. Quản lý danh mục");
            PrintForm.mainMenuln("2. Quản lý sản phẩm");
            PrintForm.mainMenuln("3. Thoát");
            PrintForm.mainMenu("Mời bạn lựa chọn: ");
            try{
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice){
                    case 1:
                        CategoryMenu.displayMenu(sc);
                        break;
                    case 2:
                        ProductMenu.displayMenu(sc);
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        PrintForm.warning("Lựa chọn không phù hợp");
                }
            } catch (NumberFormatException nfe){
                PrintForm.warning(nfe.getMessage());
            }
        }while (true);
    }
}
