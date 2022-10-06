package se.experis.boxinator.models.dbo;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    @Column
    private String shorthand;
    @Column
    private Integer multiplier;

    //create entity relationship with account
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<Account> accounts;

    //map through shipments by their id
    @JsonGetter
    public List<String> shipments() {
        if (shipments != null) {
            return shipments.stream().map(shipment -> {
                return "/api/v0/shipments" + shipment.getId();
            }).collect(Collectors.toList());
        }

        return null;
    }

    //create entity relationship with
    @OneToMany(mappedBy = "destinationCountry", fetch = FetchType.LAZY)
    private List<Shipment> shipments;

    public Country(Long id, String name, String shorthand, Integer multiplier, List<Account> accounts) {
        this.id = id;
        this.name = name;
        this.shorthand = shorthand;
        this.multiplier = multiplier;
        this.accounts = accounts;
    }

    public Country() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShorthand() {
        return shorthand;
    }

    public void setShorthand(String shorthand) {
        this.shorthand = shorthand;
    }

    public Integer getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> people) {
        this.accounts = people;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }
}
