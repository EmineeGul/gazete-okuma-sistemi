package entity;

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
@Table(name = "haber")
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

    @Column(name = "olusturulma_tarihi")
    private LocalDateTime olusturulmaTarihi;

    @Column(name = "guncellenme_tarihi")
    private LocalDateTime guncellenmeTarihi;

    @Column(name = "son_guncelleyen_admin", length = 100)
    private String sonGuncelleyenAdmin;

    @Column(name = "goruntulenme_sayisi")
    private Long goruntulenmeSayisi = 0L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori kategori;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gazete_kaynagi_id", nullable = false)
    private GazeteKaynagi haberKaynagi;

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

    public boolean gorselUrlHariciBaglantiMi() {
        return gorselUrl != null
                && (gorselUrl.startsWith("http://") || gorselUrl.startsWith("https://"));
    }

    public String getGorselKaynagi() {
        if (gorselUrl == null || gorselUrl.isBlank()) {
            return "/resources/images/default-news.jpg";
        }

        if (gorselUrlHariciBaglantiMi()) {
            return gorselUrl;
        }

        return "/resources/images/" + gorselUrl;
    }

    public String getHaberLinki() {
        return haberLinki;
    }

    public void setHaberLinki(String haberLinki) {
        this.haberLinki = haberLinki;
    }

    public LocalDateTime getOlusturulmaTarihi() {
        return olusturulmaTarihi;
    }

    public void setOlusturulmaTarihi(LocalDateTime olusturulmaTarihi) {
        this.olusturulmaTarihi = olusturulmaTarihi;
    }

    public LocalDateTime getGuncellenmeTarihi() {
        return guncellenmeTarihi;
    }

    public void setGuncellenmeTarihi(LocalDateTime guncellenmeTarihi) {
        this.guncellenmeTarihi = guncellenmeTarihi;
    }

    public String getSonGuncelleyenAdmin() {
        return sonGuncelleyenAdmin;
    }

    public void setSonGuncelleyenAdmin(String sonGuncelleyenAdmin) {
        this.sonGuncelleyenAdmin = sonGuncelleyenAdmin;
    }

    public Long getGoruntulenmeSayisi() {
        return goruntulenmeSayisi == null ? 0L : goruntulenmeSayisi;
    }

    public void setGoruntulenmeSayisi(Long goruntulenmeSayisi) {
        this.goruntulenmeSayisi = goruntulenmeSayisi;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public GazeteKaynagi getHaberKaynagi() {
        return haberKaynagi;
    }

    public void setHaberKaynagi(GazeteKaynagi haberKaynagi) {
        this.haberKaynagi = haberKaynagi;
    }

    public GazeteKaynagi getGazeteKaynagi() {
        return haberKaynagi;
    }

    public void setGazeteKaynagi(GazeteKaynagi gazeteKaynagi) {
        this.haberKaynagi = gazeteKaynagi;
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
                + ", olusturulmaTarihi=" + olusturulmaTarihi
                + ", guncellenmeTarihi=" + guncellenmeTarihi
                + ", sonGuncelleyenAdmin='" + sonGuncelleyenAdmin + '\''
                + ", goruntulenmeSayisi=" + goruntulenmeSayisi
                + ", kategori=" + kategori
                + ", haberKaynagi=" + haberKaynagi
                + '}';
    }
}
