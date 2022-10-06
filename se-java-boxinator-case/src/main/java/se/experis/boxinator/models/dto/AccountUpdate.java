package se.experis.boxinator.models.dto;

import se.experis.boxinator.models.dbo.Account;

import java.util.Date;

public class AccountUpdate {

    private Date dob;
    private String postalCode;
    private String contactNumber;
    private String country;

    public AccountUpdate() {
    }

    public AccountUpdate(Account account) {
        this.dob = account.getDob();
        this.postalCode = account.getPostalCode();
        this.contactNumber = account.getContactNumber();
        this.country = account.getCountry().getName();
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
