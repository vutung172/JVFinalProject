package WarehouseManagement.Service.Impl;

import WarehouseManagement.Service.IOService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOServiceImpl<C> implements IOService<C> {
    private static IOServiceImpl ioServiceInstance;
    private IOServiceImpl() {
    }
    public static IOServiceImpl getIoServiceInstance(){
        if (ioServiceInstance == null){
            ioServiceInstance = new IOServiceImpl();
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
            System.out.printf("Đọc dữ liệu từ file %s thành công.\n",path);
        } catch (FileNotFoundException fnf) {
            System.err.println(fnf.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (ClassNotFoundException cnf) {
            System.err.println(cnf.getMessage());
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
            System.out.printf("Ghi dữ liệu ra file %s thành công.\n",path);
        } catch (FileNotFoundException fnf) {
            System.err.println(fnf.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }
}
