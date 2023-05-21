package com.muratcivek.kampsosyalmedya.models;

import android.net.Uri;

public class ProfilGonderiModel {
   public String detay,kullaniciAdi,olumlu,yorum;

    public ProfilGonderiModel(String detay, String kullaniciAdi, String olumlu, String yorum) {
        this.detay = detay;
        this.kullaniciAdi = kullaniciAdi;
        this.olumlu = olumlu;
        this.yorum = yorum;
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

    public String getOlumlu() {
        return olumlu;
    }

    public void setOlumlu(String olumlu) {
        this.olumlu = olumlu;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }
}
