package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.ProductService;
import WarehouseManagement.entity.Product;

import java.util.*;

public class ProductServiceImpl implements BaseService<Product>, ProductService {
    private static IOServiceImpl<Product> productIOServiceImpl = IOServiceImpl.getIoServiceInstance();
    private final String path = "products.txt";

    public String getPath() {
        return path;
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
    private static List<Product> products = productIOServiceImpl.readFromFile(getProductServiceInstance().getPath());

    public static List<Product> getProducts() {
        if (products == null) {
            products = new ArrayList<>();
        }
        return products;
    }

    @Override
    public void add(Product product) {
        if (searchProductById(product.getId()) != null) {
            System.err.printf("Sản phẩm %s đã tồn tại, không thể thêm", product.getName());
        } else {
            products.add(product);
            System.out.println("Thêm sản pẩm thành công");
            productIOServiceImpl.writeToFile(products,this.path);
        }
    }

    @Override
    public void update(Scanner sc, Product updateProduct) {
        if (searchProductById(updateProduct.getId()) == null) {
            System.err.printf("Sản phẩm %s không tồn tại, không thể update", updateProduct.getName());
        } else {
            do {
                System.out.println("===== CẬP NHẬT SẢN PHẨM =====");
                System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s |\n",
                        "Mã sp",
                        "Tên sản phẩm",
                        "Giá mua",
                        "Giá bán",
                        "Lợi nhuận",
                        "Mã danh mục",
                        "Trạng thái",
                        "Mô tả");
                updateProduct.displayData();
                System.out.println("1. Cập nhật tên sản phẩm");
                System.out.println("2. Cập nhật giá bán");
                System.out.println("3. Cập nhật giá mua");
                System.out.println("4. Cập nhật mã danh muc của sản phẩm");
                System.out.println("5. Cập nhật Trạng thái sản phẩm");
                System.out.println("6. Cập nhật mô tả sản phẩm");
                System.out.println("7. Quay lại");
                System.out.print("Mời bạn lựa chọn : ");
                try {
                    int choice = Integer.parseInt(sc.nextLine());
                    if (choice == 7) {
                        break;
                    } else {
                        switch (choice) {
                            case 1:
                                updateProduct.inputProductName(sc);
                                System.out.printf("Cập nhật thành công tên cho sản phẩm có ID là %s.\n", updateProduct.getId());
                                break;
                            case 2:
                                updateProduct.inputImportPrice(sc);
                                System.out.printf("Cập nhật thành công giá mua cho sản phẩm có ID là %s.\n", updateProduct.getId());
                                break;
                            case 3:
                                updateProduct.inputExportPrice(sc);
                                System.out.printf("Cập nhật thành công giá bán cho sản phẩm có ID là %s.\n", updateProduct.getId());
                                break;
                            case 4:
                                updateProduct.inputProductDescription(sc);
                                System.out.printf("Cập nhật thành công mô tả cho sản phẩm có ID là %s.\n", updateProduct.getId());
                                break;
                            case 5:
                                updateProduct.inputStatus(sc);
                                System.out.printf("Cập nhật thành công trạng thái cho sản phẩm có ID là %s.\n", updateProduct.getId());
                                break;
                            case 6:
                                updateProduct.inputCategoryId(sc);
                                System.out.printf("Cập nhật thành công mã danh mục cho sản phẩm có ID là %s.\n", updateProduct.getId());
                                break;
                            default:
                                System.err.println("Lựa chọn không phù hợp");
                                break;
                        }

                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("Lựa chọn phải là số nguyên từ 1 đến 7");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    productIOServiceImpl.writeToFile(products,this.path);
                }
            } while (true);
        }
    }

    @Override
    public void delete(Product product) {
        if (searchProductById(product.getId()) == null) {
            System.err.printf("Sản phẩm %s không tồn tại.\n", product.getName());
        } else {
            products.remove(product);
            System.out.printf("Xóa sản phẩm %s thành công.\n", product.getName());
            productIOServiceImpl.writeToFile(products,this.path);
        }
    }

    @Override
    public void displaySortedDataByName(String sortType) {
        if (products == null) {
            System.err.println("Chưa có danh sách sản phẩm");
        } else {
            switch (sortType) {
                case "ASC":
                    products.stream().sorted(Comparator.comparing(Product::getName)).forEach(Product::displayData);
                    break;
                case "DESC":
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
            System.err.println("Chưa có danh sách sản phẩm");
        } else {
            switch (sortType) {
                case "ASC":
                    products.stream().sorted(Comparator.comparing(Product::getProfit)).forEach(Product::displayData);
                    break;
                case "DESC":
                    products.stream().sorted(Comparator.comparing(Product::getProfit).reversed()).forEach(Product::displayData);
                    break;
                default:
                    System.err.println("Lỗi lựa chọn");
            }
        }
    }

    @Override
    public List<Product> searchAny(String searchKey) {
        List<Product> searchAnyList = new ArrayList<>();
        String checkType = checkDataType(searchKey);
        switch (checkType) {
            case "INTEGER":
                Set<String> idFoundList = new HashSet<>();
                products.stream().filter(p1 -> p1.getName().contains(searchKey))
                        .forEach(p1 -> idFoundList.add(p1.getId()));
                products.stream().filter(p2 -> p2.getId().contains(searchKey))
                        .forEach(p2 -> idFoundList.add(p2.getId()));
                products.stream().filter(p3 -> String.valueOf(p3.getImportPrice()).contains(searchKey))
                        .forEach(p3 -> idFoundList.add(p3.getId()));
                products.stream().filter(p4 -> String.valueOf(p4.getExportPrice()).contains(searchKey))
                        .forEach(p4 -> idFoundList.add(p4.getId()));
                products.stream().filter(p5 -> String.valueOf(p5.getProfit()).contains(searchKey))
                        .forEach(p5 -> idFoundList.add(p5.getId()));
                products.stream().filter(p6 -> p6.getDescription().contains(searchKey))
                        .forEach(p6 -> idFoundList.add(p6.getId()));
                for (String id:idFoundList) {
                    searchAnyList.add(products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null));
                }
                return searchAnyList;
            case "DOUBLE":
                Set<String> idFoundList2 = new HashSet<>();
                products.stream().filter(p1 -> p1.getName().contains(searchKey))
                        .forEach(p1 -> idFoundList2.add(p1.getId()));
                products.stream().filter(p3 -> String.valueOf(p3.getImportPrice()).contains(searchKey))
                        .forEach(p3 -> idFoundList2.add(p3.getId()));
                products.stream().filter(p4 -> String.valueOf(p4.getExportPrice()).contains(searchKey))
                        .forEach(p4 -> idFoundList2.add(p4.getId()));
                products.stream().filter(p5 -> String.valueOf(p5.getProfit()).contains(searchKey))
                        .forEach(p5 -> idFoundList2.add(p5.getId()));
                products.stream().filter(p6 -> p6.getDescription().contains(searchKey))
                        .forEach(p6 -> idFoundList2.add(p6.getId()));
                for (String id:idFoundList2) {
                    searchAnyList.add(products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null));
                }
                return searchAnyList;
            case "STRING":
                Set<String> idFoundList3 = new HashSet<>();
                products.stream().filter(p1 -> p1.getName().contains(searchKey))
                        .forEach(p1 -> idFoundList3.add(p1.getId()));
                products.stream().filter(p2 -> p2.getId().contains(searchKey))
                        .forEach(p2 -> idFoundList3.add(p2.getId()));
                products.stream().filter(p6 -> p6.getDescription().contains(searchKey))
                        .forEach(p6 -> idFoundList3.add(p6.getId()));
                for (String id:idFoundList3) {
                    searchAnyList.add(products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null));
                }
                return searchAnyList;
            default:
                System.err.println("Lỗi dữ liệu");
        }
        return null;
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
