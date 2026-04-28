package com.example.gazete.controller;

import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.entity.Kategori;
import com.example.gazete.model.facade.HaberFacade;
import com.example.gazete.model.facade.KategoriFacade;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("kategoriFiltreBean")
@ViewScoped
public class KategoriFiltreBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HaberFacade haberFacade;

    @Inject
    private KategoriFacade kategoriFacade;

    private List<Haber> haberler;
    private List<Kategori> kategoriler;
    private Kategori seciliKategori;

    @PostConstruct
    public void init() {
        haberler = new ArrayList<>();
        kategorileriYukle();
    }

    public void kategorileriYukle() {
        kategoriler = kategoriFacade.findAll();
    }

    public void kategoriyeGoreFiltrele() {
        if (seciliKategori != null) {
            haberler = haberFacade.kategoriyeGoreHaberleriBul(seciliKategori);
        } else {
            haberler = new ArrayList<>();
        }
    }

    public void filtreyiTemizle() {
        seciliKategori = null;
        haberler = new ArrayList<>();
    }

    public List<Haber> getHaberler() {
        return haberler;
    }

    public void setHaberler(List<Haber> haberler) {
        this.haberler = haberler;
    }

    public List<Kategori> getKategoriler() {
        return kategoriler;
    }

    public void setKategoriler(List<Kategori> kategoriler) {
        this.kategoriler = kategoriler;
    }

    public Kategori getSeciliKategori() {
        return seciliKategori;
    }

    public void setSeciliKategori(Kategori seciliKategori) {
        this.seciliKategori = seciliKategori;
    }
}
