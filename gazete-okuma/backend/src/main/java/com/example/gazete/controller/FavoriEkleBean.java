package com.example.gazete.controller;

import com.example.gazete.model.entity.FavoriHaber;
import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.entity.Kullanici;
import com.example.gazete.model.facade.FavoriHaberFacade;
import com.example.gazete.model.facade.KullaniciFacade;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
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
            return;
        }

        Kullanici kullanici = kullaniciFacade.find(1L);
        if (kullanici == null) {
            return;
        }

        if (favoriHaberFacade.favoriVarMi(kullanici, haber)) {
            return;
        }

        FavoriHaber favoriHaber = new FavoriHaber();
        favoriHaber.setKullanici(kullanici);
        favoriHaber.setHaber(haber);

        favoriHaberFacade.create(favoriHaber);
    }

    public List<FavoriHaber> getFavoriler() {
        return favoriler;
    }
}
