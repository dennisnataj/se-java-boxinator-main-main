package se.experis.boxinator.models.dto;

import se.experis.boxinator.models.dbo.Country;

//dto for country, showing only relevant data for endpoint
public class CountryDTO {
    private Long id;
    private String name;
    private String shorthand;
    private Integer multiplier;

    public CountryDTO() {
    }

    public CountryDTO(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.shorthand = country.getShorthand();
        this.multiplier = country.getMultiplier();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
