package com.project.WarehouseManagement.entity.FontConfig;

public class PrintForm {

    public static void warning(String string){
        System.out.println(ColorFont.WARNING_RED+string+ColorFont.RESET);
    }
    public static void attention(String string){
        System.out.println(ColorFont.ATTENTION_YELOW+string+ColorFont.RESET);
    }
    public static void success(String string){
        System.out.println(ColorFont.SUCCESS_GREEN+string+ColorFont.RESET);
    }
    public static void mainMenuln(String string){
        System.out.println(ColorFont.CYAN+string+ColorFont.RESET);
    }
    public static void mainMenu(String string){
        System.out.print(ColorFont.CYAN+string+ColorFont.RESET);
    }

    public static void productMenuln(String string){
        System.out.println(ColorFont.BLUE+string+ColorFont.RESET);
    }
    public static void productMenu(String string){
        System.out.print(ColorFont.BLUE+string+ColorFont.RESET);
    }
    public static void categoryMenuln(String string){
        System.out.println(ColorFont.WHITE+string+ColorFont.RESET);
    }
    public static void categoryMenu(String string){
        System.out.print(ColorFont.WHITE+string+ColorFont.RESET);
    }
    public static void tableHeaderF(String string, Object... args){
        System.out.print(ColorFont.BOLD+ColorFont.PURPLE);
        System.out.printf(string,args);
        System.out.print(ColorFont.RESET);
    }
    public static void tableF(String string, Object... args){
        System.out.print(ColorFont.PURPLE);
        System.out.printf(string,args);
        System.out.print(ColorFont.RESET);
    }
}
