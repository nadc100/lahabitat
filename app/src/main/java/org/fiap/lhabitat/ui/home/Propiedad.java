package org.fiap.lhabitat.ui.home;

public class Propiedad {
    String price;
    String city;
    String estrato;
    String parking;
    String imagen;

    public Propiedad(){

    }

    public Propiedad(String price, String city, String estrato, String parking, String imagen){
        this.price = price;
        this.city = city;
        this.estrato = estrato;
        this.parking = parking;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


}
