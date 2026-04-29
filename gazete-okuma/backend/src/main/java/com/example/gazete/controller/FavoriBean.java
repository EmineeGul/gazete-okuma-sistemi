package com.example.gazete.controller;

import com.example.gazete.model.entity.FavoriHaber;
import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.entity.Kullanici;
import com.example.gazete.model.facade.FavoriHaberFacade;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named("favoriBean")
@ViewScoped
public class FavoriBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FavoriHaberFacade favoriHaberFacade;

    @Inject
    private GirisBean girisBean;

    private List<FavoriHaber> favoriler;
    private Set<Long> favoriHaberIdleri;

    @PostConstruct
    public void init() {
        favorileriYukle();
    }

    public void favorileriYukle() {
        Kullanici kullanici = girisBean.getGirisYapanKullanici();
        if (kullanici == null) {
            favoriler = new ArrayList<>();
            favoriHaberIdleri = new HashSet<>();
            return;
        }

        favoriler = favoriHaberFacade.kullaniciyaGoreFavorileriGetir(kullanici);
        favoriHaberIdleri = new HashSet<>();

        for (FavoriHaber favori : favoriler) {
            if (favori.getHaber() != null && favori.getHaber().getId() != null) {
                favoriHaberIdleri.add(favori.getHaber().getId());
            }
        }
    }

    public String favoriDegistir(Haber haber) {
        if (haber == null || haber.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Favori islemi yapilacak haber bulunamadi.");
            return null;
        }

        Kullanici kullanici = girisBean.getGirisYapanKullanici();
        if (kullanici == null) {
            return "giris.xhtml?faces-redirect=true";
        }

        FavoriHaber favoriHaber = favoriHaberFacade.favoriyiBul(kullanici, haber);
        if (favoriHaber != null) {
            favoriHaberFacade.remove(favoriHaber);
            mesajEkle(FacesMessage.SEVERITY_INFO, "Bilgi", "Haber favorilerden cikarildi.");
        } else {
            FavoriHaber yeniFavori = new FavoriHaber();
            yeniFavori.setKullanici(kullanici);
            yeniFavori.setHaber(haber);
            favoriHaberFacade.create(yeniFavori);
            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Haber favorilere eklendi.");
        }

        favorileriYukle();
        return null;
    }

    public boolean favorideMi(Haber haber) {
        return haber != null
                && haber.getId() != null
                && favoriHaberIdleri != null
                && favoriHaberIdleri.contains(haber.getId());
    }

    public void favoridenCikar(FavoriHaber favoriHaber) {
        if (favoriHaber == null || favoriHaber.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Cikarilacak favori kaydi bulunamadi.");
            return;
        }

        favoriHaberFacade.remove(favoriHaber);
        mesajEkle(FacesMessage.SEVERITY_INFO, "Bilgi", "Haber favorilerden cikarildi.");
        favorileriYukle();
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    public List<FavoriHaber> getFavoriler() {
        return favoriler;
    }

    public void setFavoriler(List<FavoriHaber> favoriler) {
        this.favoriler = favoriler;
    }
}
