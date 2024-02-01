package WarehouseManagement.Database.MySQL;

import WarehouseManagement.entity.FontConfig.PrintForm;

import java.sql.Connection;
import java.sql.*;

public class MySQLConnect {
    public static Connection open(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); // xác nhận driver dùng cho MySQL
            String url = "jdbc:mysql://localhost:3306/warehouse";
            String user = "root";
            String password = "Vtt$88842648864";
            Connection connection = DriverManager.getConnection(url,user,password);//connect tới database sử dụng
            PrintForm.success("Kết nối thành công");
            if (connection == null)
                System.out.println("connection null");
            return connection;
        } catch (Exception ex){
            ex.printStackTrace();
            PrintForm.warning("kết nối thất bại");
            return null;
        }
    }

    public static void close(Connection connection){
        try{
            if (connection != null)
                connection.close();
        } catch (Exception ex){
            ex.getStackTrace();
        }
    }
}
