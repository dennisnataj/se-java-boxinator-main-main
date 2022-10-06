package se.experis.boxinator.models.dto;

public class ShipmentInput {

    private String receiverName;
    private Integer weightOption;
    private String boxColor;
    private String destinationCountry;

    public ShipmentInput() {
    }

    public ShipmentInput(String receiverName, Integer weightOption, String boxColor, String destinationCountry) {
        this.receiverName = receiverName;
        this.weightOption = weightOption;
        this.boxColor = boxColor;
        this.destinationCountry = destinationCountry;
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

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }
}
