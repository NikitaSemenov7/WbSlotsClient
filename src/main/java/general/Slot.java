package general;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Slot {
    @JsonProperty("date")
    private Date date;
    @JsonProperty("coefficient")
    private int coefficient;
    @JsonProperty("warehouseName")
    private String warehouseName;
    @JsonProperty("warehouseID")
    private double warehouseId;

    public Date getDate() {
        return date;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public Double getWarehouseId() {
        return warehouseId;
    }
}
