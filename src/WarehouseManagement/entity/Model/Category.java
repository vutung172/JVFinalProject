package WarehouseManagement.entity.Model;

import WarehouseManagement.Exception.CategoryException;
import WarehouseManagement.Service.Impl.CategoryServiceImpl;
import WarehouseManagement.entity.FontConfig.PrintForm;
import WarehouseManagement.entity.ICategory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Category implements ICategory, Serializable {
    @Serial
    private static final long serialVersionUID = 20230113202210L;
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

    public void setId(int id) throws CategoryException {
        Category category = CategoryServiceImpl.getCategories().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        if (id <= 0){
            throw new CategoryException("Mã phải lớn hơn 0");
        } else if (category != null) {
            throw new CategoryException("Mã đã tồn tại");
        } else {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws CategoryException{
        Category category = CategoryServiceImpl.getCategories().stream()
                                                            .filter(c -> c.getName().equals(name))
                                                            .findFirst()
                                                            .orElse(null);
        if (category != null){
            throw new CategoryException("Tên danh mục đã tồn tại");
        } else if (!isValid(name,".{6,30}")){
            throw new CategoryException("Tên danh mục phải từ 6 đến 30 kí tự");
        } else {
            this.name = name;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws CategoryException {
        if (description.trim().isEmpty()){
            throw new CategoryException("Yêu cầu phải nhập mô tả cho danh mục");
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
                PrintForm.categoryMenuln("Mời nhập vào mã của danh mục: ");
                int idCategory = Integer.parseInt(sc.nextLine());
                setId(idCategory);
                break;
            }catch (NumberFormatException nfe){
                PrintForm.warning("Mã phải là số nguyên");
            } catch (CategoryException ce){
                PrintForm.warning(ce.getMessage());
            }
        }while (true);
    }
    public void inputName(Scanner sc){
        do {
            try{
                PrintForm.categoryMenuln("Mời nhập vào tên danh mục: ");
                String nameCategory = sc.nextLine();
                setName(nameCategory);
                break;
            }catch (CategoryException ce){
                PrintForm.warning(ce.getMessage());
            }
        }while (true);

    }
    public void inputCategoryDescription(Scanner sc){
        do {
            try{
                PrintForm.categoryMenuln("Mời nhập vào mô tả: ");
                String descriptionCategory = sc.nextLine();
                setDescription(descriptionCategory);
                break;
            }catch (CategoryException ce){
                PrintForm.warning(ce.getMessage());
            }
        }while (true);
    }
    public void inputStatus(Scanner sc){
        do {
            try{
                PrintForm.categoryMenuln("Mời nhập vào trạng thái: ");
                PrintForm.categoryMenuln("1. Hoạt động");
                PrintForm.categoryMenuln("2. Tạm dừng");
                System.out.print("Mời chọn lựa trạng thái: ");
                int status = Integer.parseInt(sc.nextLine());
                if(status == 1){
                    setStatus(true);
                    break;
                } else if (status == 2) {
                    setStatus(false);
                    break;
                } else {
                    PrintForm.warning("Lựa chọn không phù hợp");
                }
            }catch (NumberFormatException nfe){
                PrintForm.warning(nfe.getMessage());
            }
        }while (true);
    }

    @Override
    public void displayData() {
        PrintForm.tableF("%5s | %-30s | %15s |%s |\n",
                getId(),
                getName(),
                isStatus()?"Hoạt động":"Tạm dừng",
                getDescription());
    }

    boolean isValid(String validString,String validFormat){
        Pattern p = Pattern.compile(validFormat);
        Matcher m = p.matcher(validString);
        return m.matches();
    }
}
