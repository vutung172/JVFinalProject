package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.ProductService;
import WarehouseManagement.Service.IOService;
import WarehouseManagement.entity.FontConfig.PrintForm;
import WarehouseManagement.entity.Model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ProductServiceImpl implements BaseService<Product>, ProductService {
    private static final IOService<Product> productIOService = IOServiceImpl.getIoServiceInstance();
    private final String fileName = "products.txt";

    public String getPath() {
        return fileName;
    }

    //Singleton cho class CategoryService
    private static ProductServiceImpl productServiceInstance;

    private ProductServiceImpl() {
    }

    public static ProductServiceImpl getProductServiceInstance() {
        if (productServiceInstance == null) {
            productServiceInstance = new ProductServiceImpl();
        }
        return productServiceInstance;
    }

    //Singleton cho danh sách category
    private static List<Product> products = productIOService.readFromFile(getProductServiceInstance().getPath());

    public static List<Product> getProducts() {
        if (products == null) {
            products = new ArrayList<>();
        }
        return products;
    }

    //Service methods
    @Override
    public void add(Product product) {
        if (searchProductById(product.getId()) != null) {
            PrintForm.warning("Sản phẩm"+product.getName()+"đã tồn tại, không thể thêm");
        } else {
            products.add(product);
            PrintForm.success("Thêm sản phẩm thành công");
            productIOService.writeToFile(products,this.fileName);
        }
    }

    @Override
    public void update(Scanner sc, Product updateProduct) {
        if (searchProductById(updateProduct.getId()) == null) {
            PrintForm.warning("Sản phẩm "+updateProduct.getName()+" không tồn tại, không thể cập nhật");
        } else {
            do {
                PrintForm.productMenuln("===== CẬP NHẬT SẢN PHẨM =====");
                PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %10s | %s \n",
                        "Mã sp",
                        "Tên sản phẩm",
                        "Giá mua (USD)",
                        "Giá bán (USD)",
                        "Lợi nhuận (USD)",
                        "Danh mục sản phẩm",
                        "Trạng thái",
                        "Mô tả");
                updateProduct.displayData();
                PrintForm.productMenuln("1. Cập nhật tên sản phẩm");
                PrintForm.productMenuln("2. Cập nhật giá mua");
                PrintForm.productMenuln("3. Cập nhật giá bán");
                PrintForm.productMenuln("4. Cập nhật mã danh mục của sản phẩm");
                PrintForm.productMenuln("5. Cập nhật trạng thái sản phẩm");
                PrintForm.productMenuln("6. Cập nhật mô tả sản phẩm");
                PrintForm.productMenuln("7. Quay lại");
                PrintForm.productMenu("Mời bạn lựa chọn : ");
                try {
                    int choice = Integer.parseInt(sc.nextLine());
                    if (choice == 7) {
                        break;
                    } else {
                        switch (choice) {
                            case 1:
                                updateProduct.inputProductName(sc);
                                PrintForm.success("Cập nhật thành công tên cho sản phẩm có ID là: "+updateProduct.getId());
                                break;
                            case 2:
                                updateProduct.inputImportPrice(sc);
                                PrintForm.success("Cập nhật thành công giá mua cho sản phẩm có ID là: "+updateProduct.getId());
                                break;
                            case 3:
                                updateProduct.inputExportPrice(sc);
                                PrintForm.success("Cập nhật thành công giá bán cho sản phẩm có ID là: "+updateProduct.getId());
                                break;
                            case 4:
                                updateProduct.inputCategoryId(sc);
                                PrintForm.success("Cập nhật thành công danh mục cho sản phẩm có ID là: "+updateProduct.getId());
                                break;
                            case 5:
                                updateProduct.inputStatus(sc);
                                PrintForm.success("Cập nhật thành công trạng thái cho sản phẩm có ID là: "+updateProduct.getId());
                                break;
                            case 6:
                                updateProduct.inputProductDescription(sc);
                                PrintForm.success("Cập nhật thành công mô tả cho sản phẩm có ID là: "+updateProduct.getId());
                                break;
                            default:
                                PrintForm.warning("Lựa chọn không phù hợp");
                        }

                    }
                } catch (NumberFormatException nfe) {
                    PrintForm.warning("Lựa chọn phải là số nguyên từ 1 đến 7");
                } catch (Exception e) {
                    PrintForm.warning(e.getMessage());
                }
                productIOService.writeToFile(products,this.fileName);
            } while (true);
        }
    }

    @Override
    public void delete(Product product) {
        if (searchProductById(product.getId()) == null) {
            PrintForm.warning("Sản phẩm "+product.getName()+" không tồn tại");
        } else {
            products.remove(product);
            PrintForm.success("Xóa sản phẩm "+product.getName()+" thành công.");
            productIOService.writeToFile(products,this.fileName);
        }
    }

    @Override
    public void displaySortedDataByName(String sortType) {
        if (products == null) {
            PrintForm.warning("Hiện chưa có sản phẩm trong danh sách");
        } else {
            switch (sortType) {
                case "ASC":
                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %17s | %s \n",
                            "Mã sp",
                            "Tên sản phẩm",
                            "Giá mua (USD)",
                            "Giá bán (USD)",
                            "Lợi nhuận (USD)",
                            "Danh mục sản phẩm",
                            "Trạng thái",
                            "Mô tả");
                    products.stream().sorted(Comparator.comparing(Product::getName)).forEach(Product::displayData);
                    break;
                case "DESC":
                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %17s | %s \n",
                            "Mã sp",
                            "Tên sản phẩm",
                            "Giá mua (USD)",
                            "Giá bán (USD)",
                            "Lợi nhuận (USD)",
                            "Danh mục sản phẩm",
                            "Trạng thái",
                            "Mô tả");
                    products.stream().sorted(Comparator.comparing(Product::getName).reversed()).forEach(Product::displayData);
                    break;
                default:
                    System.err.println("Lỗi lựa chon");
            }
        }
    }

    @Override
    public void displaySortedDataByProfit(String sortType) {
        if (products == null) {
            PrintForm.warning("Hiện chưa có sản phẩm trong danh sách");
        } else {
            switch (sortType) {
                case "ASC":
                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %17s | %s \n",
                            "Mã sp",
                            "Tên sản phẩm",
                            "Giá mua (USD)",
                            "Giá bán (USD)",
                            "Lợi nhuận (USD)",
                            "Danh mục sản phẩm",
                            "Trạng thái",
                            "Mô tả");
                    products.stream().sorted(Comparator.comparing(Product::getProfit)).forEach(Product::displayData);
                    break;
                case "DESC":
                    PrintForm.tableHeaderF("%5s | %-30s | %15s | %15s | %15s | %-30s | %17s | %s \n",
                            "Mã sp",
                            "Tên sản phẩm",
                            "Giá mua (USD)",
                            "Giá bán (USD)",
                            "Lợi nhuận (USD)",
                            "Danh mục sản phẩm",
                            "Trạng thái",
                            "Mô tả");
                    products.stream().sorted(Comparator.comparing(Product::getProfit).reversed()).forEach(Product::displayData);
                    break;
                default:
                    PrintForm.warning("Lựa chọn không phù hợp");
            }
        }
    }

    @Override
    public List<Product> searchAny(String searchKey) {
        String checkType = checkDataType(searchKey);
        String key = searchKey.toLowerCase();

        List<Product> searchAnyList = new ArrayList<>();

        //Tìm kiếm trong danh mục
        CategoryServiceImpl categoryService = CategoryServiceImpl.getCategoryServiceInstance();
        Map<Integer,String> currentCategoryMap = new HashMap<>();
        if ("chưa có danh mục".contains(key)) {
            currentCategoryMap.put(0, "Chưa có danh mục");
        }
        categoryService.searchCategoryByName(key).stream().forEach(category -> currentCategoryMap.put(category.getId(), category.getName()));

        switch (checkType) {
            case "INTEGER":
                Set<String> idFoundList = new HashSet<>();
                products.stream().filter(p1 -> p1.getName().toLowerCase().contains(key))
                        .forEach(p1 -> idFoundList.add(p1.getId()));
                products.stream().filter(p2 -> p2.getId().toLowerCase().contains(key))
                        .forEach(p2 -> idFoundList.add(p2.getId()));
                products.stream().filter(p3 -> String.valueOf(p3.getImportPrice()).contains(key))
                        .forEach(p3 -> idFoundList.add(p3.getId()));
                products.stream().filter(p4 -> String.valueOf(p4.getExportPrice()).contains(key))
                        .forEach(p4 -> idFoundList.add(p4.getId()));
                products.stream().filter(p5 -> String.valueOf(p5.getProfit()).contains(key))
                        .forEach(p5 -> idFoundList.add(p5.getId()));
                products.stream().filter(p6 -> p6.getDescription().toLowerCase().contains(key))
                        .forEach(p6 -> idFoundList.add(p6.getId()));
                //tìm theo danh mục
                products.stream().filter(p7 -> currentCategoryMap.containsKey(p7.getCategoryId()))
                        .forEach(p7 -> idFoundList.add(p7.getId()));

                for (String id:idFoundList) {
                    searchAnyList.add(products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null));
                }
                return searchAnyList;
            case "DOUBLE":
                Set<String> idFoundList2 = new HashSet<>();
                products.stream().filter(p1 -> p1.getName().toLowerCase().contains(key))
                        .forEach(p1 -> idFoundList2.add(p1.getId()));
                products.stream().filter(p3 -> String.valueOf(p3.getImportPrice()).contains(key))
                        .forEach(p3 -> idFoundList2.add(p3.getId()));
                products.stream().filter(p4 -> String.valueOf(p4.getExportPrice()).contains(key))
                        .forEach(p4 -> idFoundList2.add(p4.getId()));
                products.stream().filter(p5 -> String.valueOf(p5.getProfit()).contains(key))
                        .forEach(p5 -> idFoundList2.add(p5.getId()));
                products.stream().filter(p6 -> p6.getDescription().toLowerCase().contains(key))
                        .forEach(p6 -> idFoundList2.add(p6.getId()));
                //tìm theo danh mục
                products.stream().filter(p7 -> currentCategoryMap.containsKey(p7.getCategoryId()))
                        .forEach(p7 -> idFoundList2.add(p7.getId()));

                for (String id:idFoundList2) {
                    searchAnyList.add(products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null));
                }
                return searchAnyList;
            case "STRING":
                Set<String> idFoundList3 = new HashSet<>();
                products.stream().filter(p1 -> p1.getName().toLowerCase().contains(key))
                        .forEach(p1 -> idFoundList3.add(p1.getId()));
                products.stream().filter(p2 -> p2.getId().toLowerCase().contains(key))
                        .forEach(p2 -> idFoundList3.add(p2.getId()));
                products.stream().filter(p6 -> p6.getDescription().toLowerCase().contains(key))
                        .forEach(p3 -> idFoundList3.add(p3.getId()));
                //tìm theo danh mục
                products.stream().filter(p4 -> currentCategoryMap.containsKey(p4.getCategoryId()))
                        .forEach(p4 -> idFoundList3.add(p4.getId()));
                //tìm theo trạng thái
                if("còn hàng".contains(key)){
                    products.stream().filter(Product::isStatus).forEach(p5 -> idFoundList3.add(p5.getId()));
                }
                if ("ngừng kinh doanh".contains(key)) {
                    products.stream().filter(p6 -> !p6.isStatus()).forEach(p6 -> idFoundList3.add(p6.getId()));
                }

                for (String id:idFoundList3) {
                    searchAnyList.add(products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null));
                }
                return searchAnyList;
            default:
                PrintForm.warning("Lỗi dữ liệu");
                return null;
        }
    }

    public Product searchProductById(String id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);

    }

    public String checkDataType(String searchKey) {
        try {
            Integer.parseInt(searchKey);
            return "INTEGER";
        } catch (NumberFormatException nfe1) {
            try {
                Double.parseDouble(searchKey);
                return "DOUBLE";
            } catch (NumberFormatException nfe2) {
                return "STRING";
            }
        }
    }

}
