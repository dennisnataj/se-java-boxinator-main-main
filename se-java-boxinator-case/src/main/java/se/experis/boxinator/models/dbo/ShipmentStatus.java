package se.experis.boxinator.models.dbo;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(ShipmentStatusId.class)
public class ShipmentStatus implements Serializable {

    //create entity relationship with status
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Id
    private Date changedTime = new Date();
    //create entity relationship with shipment
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @JsonGetter("shipment")
    public String shipment(){
        if(shipment != null){
            return "/api/v0/shipments/" + shipment.getId();
        }
        return null;
    }

    public ShipmentStatus() {
    }

    public ShipmentStatus(Shipment shipment, Status status) {
        this.shipment = shipment;
        this.status = status;
    }

    public ShipmentStatus(Status status, Shipment shipment) {
        this.status = status;
        this.shipment = shipment;
    }

    public Status getStatus() {
        return status;
    }

    public Date getChangedTime() {
        return changedTime;
    }

    public Shipment getShipment() {
        return shipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipmentStatus that = (ShipmentStatus) o;
        return getStatus() == that.getStatus() && Objects.equals(getChangedTime(), that.getChangedTime()) && Objects.equals(getShipment(), that.getShipment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getChangedTime(), getShipment());
    }
}
