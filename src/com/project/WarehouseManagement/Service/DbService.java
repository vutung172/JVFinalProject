package com.project.WarehouseManagement.Service;

import java.util.List;

public interface DbService<C> {
    List<C> downloadFromDb();
    void uploadToDb();
}
