package org.fiap.lhabitat.ui.gallery;

import java.io.Serializable;

public class PropertyModel implements Serializable {
    private String status;
    private String city;
    private String estrato;
    private String neighborhood;
    private String price;
    private String room;
    private String parking;
    private String description;
    public String imageURL;
    public PropertyModel(){}

    public PropertyModel(String status, String city,  String estrato, String neighborhood, String price, String room, String parking, String description, String url) {
        this.status = status;
        this.city = city;
        this.estrato = estrato;
        this.neighborhood= neighborhood;
        this.price = price;
        this.room = room;
        this.parking = parking;
        this.description = description;
        this.imageURL = url;
    }

    public String getStatus() {
        return status;
    }
    public String getCity() {
        return city;
    }
    public String getEstrato() {
        return estrato;
    }
    public String getNeighborhood() {
        return neighborhood;
    }
    public String getPrice() {
        return price;
    }
    public String getRoom() {
        return room;
    }
    public String getParking() {
        return parking;
    }
    public String getDescription() {
        return description;
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
