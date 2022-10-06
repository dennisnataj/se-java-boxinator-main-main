package se.experis.boxinator.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.awt.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonCountry {

    public String name;
    public String iso3;
    public Double latitude;
    public Double longitude;

}
