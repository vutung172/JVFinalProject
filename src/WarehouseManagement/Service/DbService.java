package WarehouseManagement.Service;

import java.util.List;

public interface DbService<C> {
    List<C> downloadFromDb();
    void uploadAllToDb();
}
