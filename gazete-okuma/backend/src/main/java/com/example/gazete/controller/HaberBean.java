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
}
