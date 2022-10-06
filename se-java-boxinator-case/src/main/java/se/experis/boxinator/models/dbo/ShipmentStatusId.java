package se.experis.boxinator.models.dbo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ShipmentStatusId implements Serializable {

    private Long shipment;
    private Long status;
    private Date changedTime = new Date();

    public ShipmentStatusId() {
    }

    public Long getShipment() {
        return shipment;
    }

    public void setShipment(Long shipment) {
        this.shipment = shipment;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long statusEnum) {
        this.status = statusEnum;
    }

    public Date getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(Date changedTime) {
        this.changedTime = changedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipmentStatusId that = (ShipmentStatusId) o;
        return Objects.equals(getShipment(), that.getShipment()) && getStatus() == that.getStatus() && Objects.equals(getChangedTime(), that.getChangedTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShipment(), getStatus(), getChangedTime());
    }
}
