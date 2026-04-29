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
import java.util.Collections;
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
    private Long secilenKategoriId;

    @PostConstruct
    public void init() {
        haberler = new ArrayList<>();
        kategorileriYukle();
    }

    public void kategorileriYukle() {
        kategoriler = kategoriFacade.findAll();
    }

    public void kategoriyeGoreFiltrele() {
        if (secilenKategoriId == null) {
            haberler = Collections.emptyList();
            return;
        }

        Kategori kategori = kategoriFacade.find(secilenKategoriId);
        haberler = kategori == null
                ? Collections.emptyList()
                : haberFacade.kategoriyeGoreHaberleriBul(kategori);
    }

    public void filtreyiTemizle() {
        secilenKategoriId = null;
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

    public Long getSecilenKategoriId() {
        return secilenKategoriId;
    }

    public void setSecilenKategoriId(Long secilenKategoriId) {
        this.secilenKategoriId = secilenKategoriId;
    }
}
