package com.example.gazete.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "haberler")
public class Haber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baslik;
    private String ozet;
    private String icerik;

    @Column(name = "yayin_tarihi")
    private LocalDateTime yayinTarihi;

    @Column(name = "gorsel_url")
    private String gorselUrl;

    @Column(name = "haber_linki")
    private String haberLinki;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kategori_id", nullable = false)
    private Category kategori;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kaynak_id", nullable = false)
    private NewsSource haberKaynagi;

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

    public String getGorselUrl() {
        return gorselUrl;
    }

    public void setGorselUrl(String gorselUrl) {
        this.gorselUrl = gorselUrl;
    }

    public String getHaberLinki() {
        return haberLinki;
    }

    public void setHaberLinki(String haberLinki) {
        this.haberLinki = haberLinki;
    }

    public Category getKategori() {
        return kategori;
    }

    public void setKategori(Category kategori) {
        this.kategori = kategori;
    }

    public NewsSource getHaberKaynagi() {
        return haberKaynagi;
    }

    public void setHaberKaynagi(NewsSource haberKaynagi) {
        this.haberKaynagi = haberKaynagi;
    }

    @Override
    public String toString() {
        return "Haber{"
                + "id=" + id
                + ", baslik='" + baslik + '\''
                + ", ozet='" + ozet + '\''
                + ", icerik='" + icerik + '\''
                + ", yayinTarihi=" + yayinTarihi
                + ", gorselUrl='" + gorselUrl + '\''
                + ", haberLinki='" + haberLinki + '\''
                + ", kategori=" + kategori
                + ", haberKaynagi=" + haberKaynagi
                + '}';
    }
}
