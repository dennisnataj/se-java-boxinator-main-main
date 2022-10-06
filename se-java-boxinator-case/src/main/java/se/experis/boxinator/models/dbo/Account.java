package se.experis.boxinator.models.dbo;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String kcSubjectId;
    @Column(unique = true)
    private String email;
    @Column(name = "date_of_birth")
    private Date dob;
    @Column
    private String postalCode;
    @Column
    private String contactNumber;
    @Column
    private Date createdAt = new Date();


    @JsonGetter(value = "country")
    public String country() {
        if (country != null) {
            return country.getName();
        }
        return null;
    }

    //create entity relationship with country
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    //map through shipments by their id
    @JsonGetter(value = "shipments")
    public List<String> shipments() {
        if(shipments != null) {
            return shipments.stream().map(shipment -> {
                return "/api/v0/shipments/" + shipment.getId();
            }).collect(Collectors.toList());
        }
        return null;
    }

    //create entity relationship with shipments
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Shipment> shipments;


    public Account(String email) {
        this.email = email;
    }

    public Account(Long id, String kcSubjectId, String email, Date dob, String postalCode, String contactNumber, Date createdAt, Country country, List<Shipment> shipments) {
        this.id = id;
        this.kcSubjectId = kcSubjectId;
        this.email = email;
        this.dob = dob;
        this.postalCode = postalCode;
        this.contactNumber = contactNumber;
        this.createdAt = createdAt;
        this.country = country;
        this.shipments = shipments;
    }

    public Account() {

    }

    public Long getId() {
        return id;
    }

    public String getKcSubjectId() {
        return kcSubjectId;
    }

    public void setKcSubjectId(String kcSubjectId) {
        this.kcSubjectId = kcSubjectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

}
