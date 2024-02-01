package com.project.WarehouseManagement.Service;

import java.util.List;

public interface IOService<C> {
    List<C> readFromFile(String path);
    void writeToFile(List<C> list,String path);
}
