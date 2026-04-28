package com.example.gazete.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(
        name = "favori_haberler",
        uniqueConstraints = @UniqueConstraint(columnNames = {"kullanici_id", "haber_id"})
)
public class FavoriHaber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @ManyToOne(optional = false)
    @JoinColumn(name = "haber_id", nullable = false)
    private Haber haber;

    public FavoriHaber() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Haber getHaber() {
        return haber;
    }

    public void setHaber(Haber haber) {
        this.haber = haber;
    }

    @Override
    public String toString() {
        return "FavoriHaber{"
                + "id=" + id
                + ", kullanici=" + kullanici
                + ", haber=" + haber
                + '}';
    }
}
