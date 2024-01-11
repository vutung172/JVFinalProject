package WarehouseManagement.Service;

import WarehouseManagement.entity.Product;

import java.util.List;

public interface ProductService {
    void displaySortedDataAll(String sortType,List<Product> products);
    void displaySortedDataByProfit(String sortType, List<Product> products);
    List<Product> searchAny(String searchKey,List<Product> products);

}
