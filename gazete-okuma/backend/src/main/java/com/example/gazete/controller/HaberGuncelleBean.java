package com.example.gazete.controller;

import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.facade.HaberFacade;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("haberGuncelleBean")
@ViewScoped
public class HaberGuncelleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HaberFacade haberFacade;

    private Long haberId;
    private Haber haber;

    public Long getHaberId() {
        return haberId;
    }

    public void setHaberId(Long haberId) {
        this.haberId = haberId;

        if (haberId != null) {
            haber = haberFacade.find(haberId);

            if (haber == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Güncellenecek haber bulunamadı."));
            }
        }
    }

    public Haber getHaber() {
        return haber;
    }

    public void setHaber(Haber haber) {
        this.haber = haber;
    }

    public String guncelle() {
        if (haber == null || haber.getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Güncellenecek haber bulunamadı."));
            return null;
        }

        try {
            haberFacade.edit(haber);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Haber başarıyla güncellendi."));

            return "admin-haber-liste.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Haber güncellenemedi."));
            return null;
        }
    }
}
