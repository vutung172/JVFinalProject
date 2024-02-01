package com.project.WarehouseManagement.entity.Model;

import com.project.WarehouseManagement.Exception.ProductException;
import com.project.WarehouseManagement.Service.Impl.CategoryServiceImpl;
import com.project.WarehouseManagement.Service.Impl.ProductServiceImpl;
import com.project.WarehouseManagement.entity.FontConfig.PrintForm;
import com.project.WarehouseManagement.entity.IProduct;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product implements IProduct, Serializable {
    @Serial
    private static final long serialVersionUID = 20230113183502L;
    private String id;
    private String name;
    private double importPrice;
    private double exportPrice;
    private double profit;
    private String description;
    private boolean status;
    private int categoryId;
    private int quantity;

    public Product() {
    }

    public Product(String id, String name, double importPrice, double exportPrice, double profit, String description, boolean status, int categoryId) {
        this.id = id;
        this.name = name;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
        this.profit = profit;
        this.description = description;
        this.status = status;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws ProductException {
        if (!isValid(id, "P[\\S]{3}")) {
            throw new ProductException("Mã phải gồm 4 kí tự, bắt đầu là P, và không chứa khoảng trống");
        } else if (isExisted(product -> product.getId().equals(id))) {
            throw new ProductException("Mã đã tồn tại");
        } else {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ProductException {
        if (!isValid(name, ".{6,30}")) {
            throw new ProductException("Tên sản phẩm phải từ 6 đến 30 kí tự");
        } else if (name.trim().isEmpty()) {
            throw new ProductException("Tên sản phẩm không được bỏ trống");
        } else if (isExisted(p -> p.getName().equalsIgnoreCase(name))) {
            throw new ProductException("Tên sản phẩm đã tồn tại");
        } else {
            this.name = name;
        }
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) throws ProductException {
        if (importPrice <= 0) {
            throw new ProductException("Giá nhập phải lớn hơn 0");
        } else if (this.exportPrice != 0) {
            if (importPrice > (this.exportPrice / (1 + MIN_INTEREST_RATE))) {
                throw new ProductException("Giá nhập phải nhỏ hơn: " + (this.exportPrice / (1 + MIN_INTEREST_RATE)) + " USD.");
            } else {
                this.importPrice = importPrice;
                calProfit();
            }
        } else {
            this.importPrice = importPrice;
        }
    }

    public double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(double exportPrice) throws ProductException {
        if (exportPrice <= 0) {
            throw new ProductException("Giá bán phải lớn hơn không");
        } else if (exportPrice <= (this.importPrice * (1 + MIN_INTEREST_RATE))) {
            throw new ProductException("Giá bán phải lớn hơn: " + (this.importPrice * (1 + MIN_INTEREST_RATE)) + " USD.");
        } else {
            this.exportPrice = exportPrice;
            calProfit();
        }
    }

    public double getProfit() {
        return profit;
    }

    private void setProfit(double profit) {
        this.profit = profit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws ProductException {
        if (description.trim().isEmpty()) {
            throw new ProductException("Yêu cầu phải nhập mô tả cho sản phẩm");
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) throws ProductException{
        List<Category> categories = CategoryServiceImpl.getCategories();
        Category category = categories.stream().filter(c -> c.getId() == categoryId)
                                                    .findFirst()
                                                    .orElse(null);
        if (category == null){
            throw  new ProductException("Danh mục không tồn tại");
        } else if (!category.isStatus()) {
            throw new ProductException("Danh mục hiện tạm dừng, không thể thêm");
        } else {
            this.categoryId = categoryId;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws ProductException{
        if(quantity <= 0) {
            throw new ProductException("Số lượng phải lớn hơn 0");
        }else {this.quantity = quantity;}
    }

    // Phương thức của đối tượng Product
    @Override
    public void inputData(Scanner sc) {
        IProduct.super.inputData(sc);
        inputProductId(sc);
        inputProductName(sc);
        inputQuantity(sc);
        inputImportPrice(sc);
        inputExportPrice(sc);
        inputProductDescription(sc);
        inputStatus(sc);
        inputCategoryId(sc);
    }

    public void inputProductId(Scanner sc) {
        do {
            try {
                PrintForm.productMenuln("Mời nhập vào ID của sản phẩm: ");
                String idProduct = sc.nextLine();
                setId(idProduct);
                break;
            } catch (ProductException pe) {
                PrintForm.warning(pe.getMessage());
            }
        } while (true);

    }

    public void inputProductName(Scanner sc) {
        do {
            try {
                PrintForm.productMenuln("Mời nhập vào tên sản phẩm: ");
                String productNameInput = sc.nextLine();
                setName(productNameInput);
                break;
            } catch (ProductException pe) {
                PrintForm.warning(pe.getMessage());
            }
        } while (true);
    }

    public void inputImportPrice(Scanner sc) {
        do {
            try {
                PrintForm.productMenuln("Mời nhập vào giá nhập sản phẩm: ");
                double importPriceInput = Double.parseDouble(sc.nextLine());
                setImportPrice(importPriceInput);
                break;
            } catch (NumberFormatException nfe) {
                PrintForm.warning(nfe.getMessage());
            } catch (ProductException pe) {
                PrintForm.warning(pe.getMessage());
            }
        } while (true);
    }

    public void inputExportPrice(Scanner sc) {
        do {
            try {
                PrintForm.productMenuln("Mời nhập vào giá bán sản phẩm: ");
                double exportPriceInput = Double.parseDouble(sc.nextLine());
                setExportPrice(exportPriceInput);
                break;
            } catch (NumberFormatException nfe) {
                PrintForm.warning(nfe.getMessage());
            } catch (ProductException pe) {
                PrintForm.warning(pe.getMessage());
            }
        } while (true);
    }

    public void inputProductDescription(Scanner sc) {
        do {
            try {
                PrintForm.productMenuln("Nhập vào mô tả cho sản phầm: ");
                String productDescription = sc.nextLine();
                setDescription(productDescription);
                break;
            } catch (ProductException pe) {
                PrintForm.warning(pe.getMessage());
            }
        } while (true);
    }

    public void inputStatus(Scanner sc) {
        do {
            try {
                PrintForm.productMenuln("Mời nhập vào trạng thái của sản phẩm: ");
                PrintForm.productMenuln("1. Đang bán");
                PrintForm.productMenuln("2. Dừng bán");
                PrintForm.productMenu("Mời chọn lựa chọn trạng thái: ");
                float statusProduct = Float.parseFloat(sc.nextLine());
                if (statusProduct == 1F) {
                    setStatus(true);
                    break;
                } else if (statusProduct == 2F) {
                    setStatus(false);
                    break;
                } else {
                    PrintForm.warning("Lựa chọn không phù hợp");
                }
            } catch (NumberFormatException nfe) {
                PrintForm.warning(nfe.getMessage());
            }
        } while (true);
    }

    public void inputCategoryId(Scanner sc) {
        List<Category> categories = CategoryServiceImpl.getCategories();
        do {
            try {
                if (categories.isEmpty()) {
                    PrintForm.warning("Hiện chưa có danh mục nào trong danh sách, không thể chọn");
                } else {
                    PrintForm.tableF("%5s | %-30s\n", "Mã", "Tên danh mục");
                    categories.stream().sorted(Comparator.comparing(Category::getId))
                                        .filter(Category::isStatus)
                                        .forEach(category -> PrintForm.tableF("%5s | %-30s\n", category.getId(), category.getName()));
                    /*PrintForm.tableF("%5s | %-30s\n", "0", "Chưa có danh mục");*/
                    PrintForm.productMenu("Chọn mã của danh mục muốn thêm: ");
                    int idCategory = Integer.parseInt(sc.nextLine());
                    setCategoryId(idCategory);
                }
                break;
            } catch (NumberFormatException nfe) {
                PrintForm.warning("Chỉ được lựa chọn các mã có trong danh sách");
            } catch (ProductException pe) {
                PrintForm.warning(pe.getMessage());
            }
        } while (true);
    }
    public void inputQuantity(Scanner sc){
        do {
            try{
                PrintForm.productMenuln("Nhập vào số lượng sản phẩm: ");
                int productQuantity = Integer.parseInt(sc.nextLine());
                setQuantity(productQuantity);
                break;
            }catch (NumberFormatException nfe){
                PrintForm.warning(nfe.getMessage());
            } catch (ProductException pe){
                PrintForm.warning(pe.getMessage());
            }
        }while (true);
    }

    @Override
    public void displayData() {
        IProduct.super.displayData();
        List<Category> categories = CategoryServiceImpl.getCategories();
        Category category = categories.stream().filter(c -> c.getId() == getCategoryId()).findFirst().orElse(null);
        PrintForm.tableF("%5s | %-30s | %15.2f | %15.2f | %15.2f | %-30s | %17s | %10d | %s \n",
                getId(),
                getName(),
                getImportPrice(),
                getExportPrice(),
                getProfit(),
                category != null ? category.getName() : "Chưa có danh mục",
                isStatus() ? "Còn hàng" : "Ngừng kinh doanh",
                getQuantity(),
                getDescription());
    }

    @Override
    public void calProfit() {
        setProfit(this.exportPrice - this.importPrice);
    }

    boolean isValid(String validString, String validFormat) {
        Pattern p = Pattern.compile(validFormat);
        Matcher m = p.matcher(validString);
        return m.matches();
    }

    boolean isExisted(Predicate<Product> predicate) {
        List<Product> products = ProductServiceImpl.getProducts();
        Product product = products.stream().filter(predicate)
                .findFirst()
                .orElse(null);
        return product != null;
    }

}
