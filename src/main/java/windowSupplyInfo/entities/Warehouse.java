package windowSupplyInfo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Warehouse {
    @JsonProperty("name")
    private String warehouseName;
    @JsonProperty("ID")
    private double warehouseId;

    public String getWarehouseName() {
        return warehouseName;
    }

    public double getWarehouseId() {
        return warehouseId;
    }
}
