package com.muratcivek.kampsosyalmedya.models;

import android.net.Uri;

public class akisYorumModel {

    public String detay, kullaniciAdi;

    public akisYorumModel(String detay, String kullaniciAdi) {
        this.detay = detay;
        this.kullaniciAdi = kullaniciAdi;

    }
    public String getDetay() {
        return detay;
    }
    public void setDetay(String detay) {
        this.detay = detay;
    }
    public String getKullaniciAdi() {
        return kullaniciAdi;
    }
    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

}

