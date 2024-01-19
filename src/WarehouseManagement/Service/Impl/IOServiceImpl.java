package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.IOService;
import WarehouseManagement.entity.FontConfig.PrintForm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class IOServiceImpl<C> implements IOService<C> {
    private static IOService ioServiceInstance;
    private IOServiceImpl() {
    }
    public static IOService getIoServiceInstance(){
        if (ioServiceInstance == null){
            ioServiceInstance = new IOServiceImpl<>();
        }
        return ioServiceInstance;
    }

    @Override
    public List<C> readFromFile(String path) {
        List<C> list = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\TungV\\IdeaProjects\\FinalProject\\src\\WarehouseManagement\\Database\\" + path);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (List<C>) ois.readObject();
            ois.close();
            fis.close();
            PrintForm.success("Đọc dữ liệu từ file "+path+" thành công.");
        } catch (FileNotFoundException fnf) {
            PrintForm.warning(fnf.getMessage());
        } catch (IOException ioe) {
            PrintForm.warning(ioe.getMessage());
        } catch (ClassNotFoundException cnf) {
            PrintForm.warning(cnf.getMessage());
        }
        return list;
    }

    @Override
    public void writeToFile(List<C> list, String path) {
        try {
            File file = new File("C:\\Users\\TungV\\IdeaProjects\\FinalProject\\src\\WarehouseManagement\\Database\\" + path);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
            fos.close();
            PrintForm.success("Ghi dữ liệu ra file "+path+" thành công.");
        } catch (FileNotFoundException fnf) {
            PrintForm.warning(fnf.getMessage());
        } catch (IOException ioe) {
            PrintForm.warning(ioe.getMessage());
        }
    }

    List<String> string = new ArrayList<>();
}
