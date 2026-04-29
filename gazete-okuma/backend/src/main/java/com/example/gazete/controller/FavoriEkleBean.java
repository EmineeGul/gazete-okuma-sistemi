package com.example.gazete.controller;

import com.example.gazete.model.entity.FavoriHaber;
import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.entity.Kullanici;
import com.example.gazete.model.facade.FavoriHaberFacade;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("favoriEkleBean")
@RequestScoped
public class FavoriEkleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FavoriHaberFacade favoriHaberFacade;

    @Inject
    private GirisBean girisBean;

    private List<FavoriHaber> favoriler;

    @PostConstruct
    public void init() {
        Kullanici kullanici = girisBean.getGirisYapanKullanici();
        if (kullanici == null) {
            favoriler = Collections.emptyList();
            return;
        }

        favoriler = favoriHaberFacade.kullaniciyaGoreFavorileriGetir(kullanici);
    }

    public String favoriyeEkle(Haber haber) {
        if (haber == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Favorilere eklenecek haber bulunamadi."));
            return null;
        }

        Kullanici kullanici = girisBean.getGirisYapanKullanici();
        if (kullanici == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyari", "Favorilere eklemek için giriş yapmalısınız."));
            return "giris.xhtml?faces-redirect=true";
        }

        if (favoriHaberFacade.favoriVarMi(kullanici, haber)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyari", "Bu haber zaten favorilerde."));
            return null;
        }

        FavoriHaber favoriHaber = new FavoriHaber();
        favoriHaber.setKullanici(kullanici);
        favoriHaber.setHaber(haber);
        favoriHaberFacade.create(favoriHaber);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Basarili", "Haber favorilere eklendi."));
        return null;
    }

    public List<FavoriHaber> getFavoriler() {
        return favoriler;
    }
}
