package com.example.gazete.controller;

import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.facade.HaberFacade;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("haberBean")
@ViewScoped
public class HaberBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HaberFacade haberFacade;

    private List<Haber> haberler;

    @PostConstruct
    public void init() {
        haberleriYukle();
    }

    public void haberleriYukle() {
        haberler = haberFacade.sonHaberleriGetir(10);
    }

    public List<Haber> getHaberler() {
        return haberler;
    }

    public void setHaberler(List<Haber> haberler) {
        this.haberler = haberler;
    }
}
