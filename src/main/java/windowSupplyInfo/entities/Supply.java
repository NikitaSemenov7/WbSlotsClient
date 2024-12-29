package windowSupplyInfo.entities;

import java.time.LocalDate;
import java.util.ArrayList;

public class Supply {
    private int preOrderId;
    private int boxTypeId;
    private int maxCoefficient;
    private double warehouseId;
    private boolean isBooked;
    private ArrayList<LocalDate> dates;

    public Supply(int preOrderId, int boxTypeId, int maxCoefficient, double warehouseId, ArrayList<LocalDate> dates) {
        this.preOrderId = preOrderId;
        this.boxTypeId = boxTypeId;
        this.maxCoefficient = maxCoefficient;
        this.warehouseId = warehouseId;
        this.isBooked = false;
        this.dates = dates;
    }

    public int getPreOrderId() {
        return preOrderId;
    }

    public int getBoxTypeId() {
        return boxTypeId;
    }

    public int getMaxCoefficient() {
        return maxCoefficient;
    }

    public double getWarehouseId() {
        return warehouseId;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public ArrayList<LocalDate> getDates() {
        return dates;
    }
}
