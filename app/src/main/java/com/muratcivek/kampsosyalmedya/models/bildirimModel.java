package com.muratcivek.kampsosyalmedya.models;

public class bildirimModel {
    String mail,detay,bildirim;

    public bildirimModel(String mail, String detay, String bildirim) {
        this.mail = mail;
        this.detay = detay;
        this.bildirim = bildirim;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDetay() {
        return detay;
    }

    public void setDetay(String detay) {
        this.detay = detay;
    }

    public String getBildirim() {
        return bildirim;
    }

    public void setBildirim(String bildirim) {
        this.bildirim = bildirim;
    }
}
