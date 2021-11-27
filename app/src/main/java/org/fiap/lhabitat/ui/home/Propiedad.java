package org.fiap.lhabitat.ui.home;

public class Propiedad {
    String price;
    String city;
    String estrato;
    String parking;
    String imagenURL;

    public Propiedad(){

    }

    public Propiedad(String price, String city, String estrato, String parking, String url){
        this.price = price;
        this.city = city;
        this.estrato = estrato;
        this.parking = parking;
        this.imagenURL = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEstrato() {
        return estrato;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }


}
