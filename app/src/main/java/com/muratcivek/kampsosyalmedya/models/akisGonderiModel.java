package com.muratcivek.kampsosyalmedya.models;

public class akisGonderiModel {
    public String detay, kullaniciAdi;
    public String  olumluSayi, yorumSayi;

    public akisGonderiModel(String detay, String kullaniciAdi, String olumluSayi, String yorumSayi) {
        this.detay = detay;
        this.kullaniciAdi = kullaniciAdi;
        this.olumluSayi = olumluSayi;
        this.yorumSayi = yorumSayi;
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
    public String getOlumluSayi() {
        return olumluSayi;
    }
    public void setOlumluSayi(String olumluSayi) {
        this.olumluSayi = olumluSayi;
    }
    public String getYorumSayi() {
        return yorumSayi;
    }
    public void setYorumSayi(String yorumSayi) {
        this.yorumSayi = yorumSayi;
    }
}
