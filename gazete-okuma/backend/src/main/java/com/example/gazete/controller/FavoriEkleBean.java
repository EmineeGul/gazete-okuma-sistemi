package com.example.gazete.controller;

import com.example.gazete.model.entity.FavoriHaber;
import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.entity.Kullanici;
import com.example.gazete.model.facade.FavoriHaberFacade;
import com.example.gazete.model.facade.KullaniciFacade;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("favoriEkleBean")
@RequestScoped
public class FavoriEkleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FavoriHaberFacade favoriHaberFacade;

    @Inject
    private KullaniciFacade kullaniciFacade;

    private List<FavoriHaber> favoriler;

    @PostConstruct
    public void init() {
        Kullanici kullanici = kullaniciFacade.find(1L);
        if (kullanici != null) {
            favoriler = favoriHaberFacade.kullaniciyaGoreFavorileriGetir(kullanici);
        }
    }

    public void favoriyeEkle(Haber haber) {
        if (haber == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Favorilere eklenecek haber bulunamadı."));
            return;
        }

        Kullanici kullanici = kullaniciFacade.find(1L);
        if (kullanici == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı bulunamadı."));
            return;
        }

        if (favoriHaberFacade.favoriVarMi(kullanici, haber)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyarı", "Bu haber zaten favorilerde."));
            return;
        }

        FavoriHaber favoriHaber = new FavoriHaber();
        favoriHaber.setKullanici(kullanici);
        favoriHaber.setHaber(haber);

        favoriHaberFacade.create(favoriHaber);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Haber favorilere eklendi."));
    }

    public List<FavoriHaber> getFavoriler() {
        return favoriler;
    }
}
