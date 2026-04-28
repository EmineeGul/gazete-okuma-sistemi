package com.example.gazete.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "news_sources")
public class NewsSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad", nullable = false, length = 100)
    private String ad;

    @Column(name = "web_sitesi", length = 255)
    private String webSitesi;

    @Column(name = "aciklama", length = 255)
    private String aciklama;

    public NewsSource() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getWebSitesi() {
        return webSitesi;
    }

    public void setWebSitesi(String webSitesi) {
        this.webSitesi = webSitesi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    @Override
    public String toString() {
        return "NewsSource{"
                + "id=" + id
                + ", ad='" + ad + '\''
                + ", webSitesi='" + webSitesi + '\''
                + ", aciklama='" + aciklama + '\''
                + '}';
    }
}
