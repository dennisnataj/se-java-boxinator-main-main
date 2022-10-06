package se.experis.boxinator.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import se.experis.boxinator.models.dbo.*;

import java.util.Date;

//dto for shipment, showing only relevant data for endpoint
public class ShipmentDTO {

    private Long id;
    private String receiverName;
    private Integer weightOption;
    private String boxColor;

    @JsonGetter
    public String destinationCountry() {
        if(destinationCountry != null) {
            return destinationCountry.getName();
        }
        return null;
    }

    private Country destinationCountry;
    private Integer cost;
    private String status;
    private Date changedTime;

    public ShipmentDTO(Shipment shipment) {
        this.id = shipment.getId();
        this.receiverName = shipment.getReceiverName();
        this.weightOption = shipment.getWeightOption();
        this.boxColor = shipment.getBoxColor();
        this.destinationCountry = shipment.getDestinationCountry();
        ShipmentStatus latestStatus =  shipment.getShipmentStatuses().get(shipment.getShipmentStatuses().size() - 1);
        this.status = latestStatus.getStatus().getStatus();
        this.changedTime = latestStatus.getChangedTime();
        this.cost = shipment.getCost();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getWeightOption() {
        return weightOption;
    }

    public void setWeightOption(Integer weightOption) {
        this.weightOption = weightOption;
    }

    public String getBoxColor() {
        return boxColor;
    }

    public void setBoxColor(String boxColor) {
        this.boxColor = boxColor;
    }

    public Country getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(Country destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(Date changedTime) {
        this.changedTime = changedTime;
    }
    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

}
