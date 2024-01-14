package WarehouseManagement.entity;

import WarehouseManagement.Service.Impl.CategoryServiceImpl;
import WarehouseManagement.Service.Impl.ProductServiceImpl;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product implements IProduct, Serializable {
    private String id;
    private String name;
    private double importPrice;
    private double exportPrice;
    private double profit;
    private String description;
    private boolean status;
    private int categoryId;
    private List<Category> categories = CategoryServiceImpl.getCategories();

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

    public void setId(String id) throws Exception {
        if (!isValid(id, "P.{3}")) {
            throw new Exception("ID phải gồm 4 kí tự, và bắt đầu là P");
        } else if (isExisted(product -> product.getId().equals(id))) {
            throw new Exception("ID đã tô tại");
        } else {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (!isValid(name, ".{6,30}")) {
            throw new Exception("Tên sản phẩm phải từ 6 đến 30 kí tự");
        } else if (isExisted(p -> p.getName().equals(name))) {
            throw new Exception("Tên sản phẩm đã tồn tại");
        } else {
            this.name = name;
        }
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) throws Exception {
        if (importPrice <= 0) {
            throw new Exception("Giá nhập phải lớn hơn 0");
        } else if (this.exportPrice != 0){
            if (importPrice > (this.exportPrice/(1 + MIN_INTEREST_RATE))) {
                throw new Exception("Giá nhập phải nhỏ hơn: " + (this.exportPrice / (1 + MIN_INTEREST_RATE)) + " USD.");
            } else {
                this.importPrice = importPrice;
                calProfit();
            }
        }
        else {
            this.importPrice = importPrice;
            calProfit();
        }
    }

    public double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(double exportPrice) throws Exception {
        if (exportPrice <= 0) {
            throw new Exception("Giá bán phải lớn hơn không");
        } else if (exportPrice <= (this.importPrice * (1 + MIN_INTEREST_RATE))) {
            throw new Exception("Giá bán phải lớn hơn: " + (this.importPrice * (1 + MIN_INTEREST_RATE)) + " USD.");
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

    public void setDescription(String description) throws Exception{
        if (description.trim().isEmpty()){
            throw new Exception("Yêu cầu phải nhập mô tả cho sản phẩm");
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

    public void setCategoryId(int categoryId) throws Exception {
        Category category = categories.stream().filter(c -> c.getId() == categoryId)
                .findFirst()
                .orElse(null);
        if (category == null) {
            throw new Exception("Category không tồn tại");
        }else {
            this.categoryId = categoryId;
        }
    }

    // Phương thức của đối tượng Product
    @Override
    public void inputData(Scanner sc) {
        IProduct.super.inputData(sc);
        inputIdProduct(sc);
        inputProductName(sc);
        inputImportPrice(sc);
        inputExportPrice(sc);
        inputProductDescription(sc);
        inputStatus(sc);
        inputCategoryId(sc);
    }

    public void inputIdProduct(Scanner sc) {
        do {
            try {
                System.out.println("Mời nhập vào ID của sản phẩm: ");
                String idProduct = sc.nextLine();
                setId(idProduct);
                break;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } while (true);

    }

    public void inputProductName(Scanner sc) {
        do {
            try {
                System.out.println("Mời nhập vào tên sản phẩm: ");
                String productNameInput = sc.nextLine();
                setName(productNameInput);
                break;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } while (true);
    }

    public void inputImportPrice(Scanner sc) {
        do {
            try {
                System.out.println("Mời nhập vào giá nhập sản phẩm: ");
                double importPriceInput = Double.parseDouble(sc.nextLine());
                setImportPrice(importPriceInput);
                break;
            }catch (NumberFormatException nfe){
                System.err.println(nfe.getMessage());
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }while (true);
    }

    public void inputExportPrice(Scanner sc) {
        do {
            try {
                System.out.println("Mời nhập vào giá bán sản phẩm: ");
                double exportPriceInput = Double.parseDouble(sc.nextLine());
                setExportPrice(exportPriceInput);
                break;
            } catch (NumberFormatException nfe){
                System.err.println(nfe.getMessage());
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        } while (true);
    }

    public void inputProductDescription(Scanner sc) {
        do {
            try {
                System.out.println("Nhập vào mô tả cho sản phầm: ");
                String productDescription = sc.nextLine();
                setDescription(productDescription);
                break;
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }while (true);
    }

    public void inputStatus(Scanner sc) {
        do {
            try{
                System.out.println("Mời nhập vào trạng thái của sản phẩm: ");
                System.out.println("1. Đang bán");
                System.out.println("2. Đã ngừng bán");
                System.out.print("Mời chọn lựa chọn trạng thái: ");
                int statusProduct = Integer.parseInt(sc.nextLine());
                if(statusProduct == 1){
                    setStatus(true);
                    break;
                } else if (statusProduct == 2) {
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

    public void inputCategoryId(Scanner sc){
        do {
            try {
                if (categories.isEmpty()) {
                    System.err.println("Danh sách danh mục hiện đang trống, không thể chọn");
                } else {
                    System.out.printf("%s | %s\n","Mã","Tên danh mục");
                    categories.stream().sorted(Comparator.comparing(Category::getId)).forEach(category -> System.out.printf("%s | %s\n", category.getId(), category.getName()));
                    System.out.print("Nhập vào mã của danh mục muốn thêm: ");
                    int idCategory = Integer.parseInt(sc.nextLine());
                    setCategoryId(idCategory);
                }
                break;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } while (true);
    }

    @Override
    public void displayData() {
        IProduct.super.displayData();
        Category category = categories.stream().filter(c -> c.getId() == getCategoryId()).findFirst().orElse(null);
        System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s |\n",
                getId(),
                getName(),
                getImportPrice(),
                getExportPrice(),
                getProfit(),
                category != null ? category.getName() : "Chưa có danh mục",
                isStatus()?"Đang bán":"Đã ngừng bán",
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
