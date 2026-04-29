package com.example.gazete.controller;

import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.facade.HaberFacade;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("haberBean")
@ViewScoped
public class HaberBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HaberFacade haberFacade;

    private Long haberId;
    private List<Haber> haberler;
    private Haber seciliHaber;

    @PostConstruct
    public void init() {
        System.out.println("HaberBean init calisti.");
        haberleriYukle();
    }

    public void haberleriYukle() {
        try {
            haberler = haberFacade.tumHaberleriGetir();
            System.out.println("Veritabanindan gelen haber sayisi: " + (haberler != null ? haberler.size() : 0));
        } catch (Exception e) {
            haberler = Collections.emptyList();
            System.out.println("Haberler yuklenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void seciliHaberiYukle() {
        seciliHaber = null;

        if (haberId == null) {
            return;
        }

        try {
            seciliHaber = haberFacade.find(haberId);
            System.out.println("Detay sayfasi icin yuklenen haber id: " + haberId
                    + ", bulundu mu: " + (seciliHaber != null));
        } catch (Exception e) {
            seciliHaber = null;
            System.out.println("Secili haber yuklenirken hata olustu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void haberSil(Haber haber) {
        if (haber == null || haber.getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek haber bulunamadı."));
            return;
        }

        try {
            Haber silinecekHaber = haberFacade.find(haber.getId());

            if (silinecekHaber == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Haber veritabanında bulunamadı."));
                return;
            }

            haberFacade.remove(silinecekHaber);
            haberleriYukle();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Haber başarıyla silindi."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Haber silinemedi."));
        }
    }

    public List<Haber> getHaberler() {
        return haberler;
    }

    public void setHaberler(List<Haber> haberler) {
        this.haberler = haberler;
    }

    public Long getHaberId() {
        return haberId;
    }

    public void setHaberId(Long haberId) {
        this.haberId = haberId;
    }

    public Haber getSeciliHaber() {
        return seciliHaber;
    }

    public void setSeciliHaber(Haber seciliHaber) {
        this.seciliHaber = seciliHaber;
    }
}
