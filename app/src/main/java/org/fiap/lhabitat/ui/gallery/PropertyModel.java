package org.fiap.lhabitat.ui.gallery;

public class PropertyModel {
    private String status;
    private String city;
    private String neighborhood;
    private String price;
    private String estrato;
    private String parking;
    public String imageURL;
    public PropertyModel(){}

    public PropertyModel(String status, String city, String neighborhood, String price, String estrato, String parking, String url) {
        this.status = status;
        this.city = city;
        this.neighborhood= neighborhood;
        this.price = price;
        this.estrato = estrato;
        this.parking = parking;
        this.imageURL = url;
    }

    public String getStatus() {
        return status;
    }
    public String getCity() {
        return city;
    }
    public String getNeighborhood() {
        return neighborhood;
    }
    public String getPrice() {
        return price;
    }
    public String getEstrato() {
        return estrato;
    }
    public String getParking() {
        return parking;
    }
    public String getImageURL() {
        return imageURL;
    }

}
