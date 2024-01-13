package WarehouseManagement.Service;

import WarehouseManagement.entity.Product;

import java.util.List;

public interface ProductService {
    void displaySortedDataByName(String sortType);
    void displaySortedDataByProfit(String sortType);
    List<Product> searchAny(String searchKey);

}
