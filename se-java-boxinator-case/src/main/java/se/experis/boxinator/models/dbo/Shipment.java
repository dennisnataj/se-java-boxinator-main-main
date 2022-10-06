package se.experis.boxinator.models.dbo;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String receiverName;

    @Column
    private Integer weightOption;

    @Column
    private String boxColor;

    @Column
    private Integer cost;

    //return destination country by id
    @JsonGetter("destinationCountry")
    public String destinationCountry() {

        if(destinationCountry != null) {
            return "api/v0/country/" + destinationCountry.getId();
        }

        return null;
    }

    //create entity relationship with country
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country destinationCountry;

    //return the sender by id
    @JsonGetter("sender")
    public String sender() {
        if(sender != null) {
            return "api/v0/account/" + sender.getId();
        }

        return null;
    }
    //create entity relationship with account
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account sender;

    @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
    private List<ShipmentStatus> shipmentStatuses = new ArrayList<>();


    //map through shipment statuses
    @JsonGetter("shipmentStatuses")
    public List<String> shipmentStatuses() {
        if(shipmentStatuses != null) {
            return shipmentStatuses.stream().map(shipmentStatus -> {
                return shipmentStatus.getStatus().getStatus();
            }).collect(Collectors.toList());
        }

        return null;
    }

    public Shipment() {
    }

    public Long getId() {
        return id;
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

    public Account getSender() {
        return sender;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public List<ShipmentStatus> getShipmentStatuses() {
        return shipmentStatuses;
    }

    public void setShipmentStatuses(List<ShipmentStatus> shipmentStatuses) {
        this.shipmentStatuses = shipmentStatuses;
    }
}
