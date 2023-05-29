package com.muratcivek.kampsosyalmedya.models;

public class mesajlarModel {
    String email,mesajDetay;

    public mesajlarModel(String email, String mesajDetay) {
        this.email = email;
        this.mesajDetay = mesajDetay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMesajDetay() {
        return mesajDetay;
    }

    public void setMesajDetay(String mesajDetay) {
        this.mesajDetay = mesajDetay;
    }
}
