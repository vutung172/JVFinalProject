package WarehouseManagement.entity;

import java.util.Scanner;

public interface IProduct extends ICategory{
    float MIN_INTEREST_RATE = 0.2F;
    @Override
    default void inputData(Scanner sc) {
    }

    @Override
    default void displayData() {
    }

    void calProfit();

}
