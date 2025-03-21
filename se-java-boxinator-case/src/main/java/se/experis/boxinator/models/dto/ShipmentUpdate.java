package se.experis.boxinator.models.dto;

import se.experis.boxinator.models.dbo.Country;
import se.experis.boxinator.models.dbo.Status;

public class ShipmentUpdate {
    private Long id;
    private String status;

    public ShipmentUpdate() {
    }

    public ShipmentUpdate(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
