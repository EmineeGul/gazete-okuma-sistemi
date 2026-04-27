package com.example.gazete.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "haberler")
public class Haber implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baslik;
    private String ozet;
    private String icerik;
    private LocalDateTime yayinTarihi;

    public Haber() {
    }

    public Haber(String baslik, String ozet, String icerik, LocalDateTime yayinTarihi) {
        this.baslik = baslik;
        this.ozet = ozet;
        this.icerik = icerik;
        this.yayinTarihi = yayinTarihi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getOzet() {
        return ozet;
    }

    public void setOzet(String ozet) {
        this.ozet = ozet;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public LocalDateTime getYayinTarihi() {
        return yayinTarihi;
    }

    public void setYayinTarihi(LocalDateTime yayinTarihi) {
        this.yayinTarihi = yayinTarihi;
    }
}
