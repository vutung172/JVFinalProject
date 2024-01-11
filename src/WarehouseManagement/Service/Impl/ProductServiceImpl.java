package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.BaseService;
import WarehouseManagement.Service.ProductService;
import WarehouseManagement.entity.Product;

import java.util.List;

public class ProductServiceImpl implements BaseService<Product>,ProductService {
    @Override
    public void add(Product object, List<Product> list) {

    }

    @Override
    public void update(Product oldObject, Product newObject) {

    }

    @Override
    public void delete(Product object, List<Product> list) {

    }

    @Override
    public void displaySortedDataAll(String sortType, List<Product> products) {

    }

    @Override
    public void displaySortedDataByProfit(String sortType, List<Product> products) {

    }

    @Override
    public List<Product> searchAny(String searchKey, List<Product> products) {
        return null;
    }
}
