package com.project.WarehouseManagement.Service;

import com.project.WarehouseManagement.entity.Model.Product;

import java.util.List;

public interface ProductService {
    void displaySortedDataByName(String sortType);
    void displaySortedDataByProfit(String sortType);
    List<Product> searchAny(String searchKey);
}
