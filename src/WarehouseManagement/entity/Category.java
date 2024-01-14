package WarehouseManagement.entity;

import WarehouseManagement.Service.Impl.CategoryServiceImpl;

import java.io.Serializable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Category implements ICategory, Serializable {
    private int id;
    private String name;
    private String description;
    private boolean status;

    public Category() {
    }

    public Category(int id, String name, String description, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws Exception{
        Category category = CategoryServiceImpl.getCategories().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        if (id <= 0){
            throw new Exception("ID phải lớn hơn 0");
        } else if (category != null) {
            throw new Exception("ID đã tồn tại");
        } else {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception{
        Category category = CategoryServiceImpl.getCategories().stream()
                                                            .filter(c -> c.getName().equals(name))
                                                            .findFirst()
                                                            .orElse(null);
        if (category != null){
            throw new Exception("Tên category đã tồn tại");
        } else if (!isValid(name,".{6,30}")){
            throw new Exception("Tên phải từ 6 đến 30 kí tự");
        } else {
            this.name = name;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws Exception {
        if (description.trim().isEmpty()){
            throw new Exception("Yêu cầu phải nhập mô tả cho danh mục");
        } else {
            this.description = description;
        }
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    //Phương thức của đối tượng Category
    @Override
    public void inputData(Scanner sc){
        inputId(sc);
        inputName(sc);
        inputCategoryDescription(sc);
        inputStatus(sc);
    }
    public void inputId(Scanner sc){
        do {
            try{
                System.out.println("Mời nhập vào ID của Category: ");
                int idCategory = Integer.parseInt(sc.nextLine());
                setId(idCategory);
                break;
            }catch (NumberFormatException nfe){
                System.err.println("ID phải là số nguyên");
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }while (true);
    }
    public void inputName(Scanner sc){
        do {
            try{
                System.out.println("Mời nhập vào tên Category: ");
                String nameCategory = sc.nextLine();
                setName(nameCategory);
                break;
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }while (true);

    }
    public void inputCategoryDescription(Scanner sc){
        do {
            try{
                System.out.println("Mời nhập vào mô tả: ");
                String descriptionCategory = sc.nextLine();
                setDescription(descriptionCategory);
                break;
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }while (true);
    }
    public void inputStatus(Scanner sc){
        do {
            try{
                System.out.println("Mời nhập vào trạng thái: ");
                System.out.println("1. Đang hoạt động");
                System.out.println("2. Tạm dừng");
                System.out.print("Mời chọnluwaja trạng thái: ");
                int status = Integer.parseInt(sc.nextLine());
                if(status == 1){
                    setStatus(true);
                    break;
                } else if (status == 2) {
                    setStatus(false);
                    break;
                } else {
                    System.err.println("Lựa chọn không phù hợp");
                }
            }catch (NumberFormatException nfe){
                System.err.println(nfe.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }while (true);
    }

    @Override
    public void displayData() {
        System.out.printf("%s | %s | %s |%s |\n",
                getId(),
                getName(),
                isStatus()?"Đang hoạt động":"Tạm dừng",
                getDescription());
    }

    boolean isValid(String validString,String validFormat){
        Pattern p = Pattern.compile(validFormat);
        Matcher m = p.matcher(validString);
        return m.matches();
    }
}
