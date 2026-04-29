package com.example.gazete.controller;

import com.example.gazete.model.entity.Kullanici;
import com.example.gazete.model.facade.KullaniciFacade;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("girisBean")
@SessionScoped
public class GirisBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private KullaniciFacade kullaniciFacade;

    private String eposta;
    private String sifre;
    private Kullanici aktifKullanici;

    public String girisYap() {
        Kullanici kullanici = kullaniciFacade.epostayaGoreBul(eposta);

        if (kullanici == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanici bulunamadi", null));
            return null;
        }

        if (sifre != null && sifre.equals(kullanici.getSifre())) {
            aktifKullanici = kullanici;
            return "index.xhtml?faces-redirect=true";
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sifre hatali", null));
        return null;
    }

    public String cikisYap() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "giris.xhtml?faces-redirect=true";
    }

    public boolean girisYapilmisMi() {
        return aktifKullanici != null;
    }

    public String getHesapGosterim() {
        if (aktifKullanici == null) {
            return "Misafir";
        }

        if (aktifKullanici.getKullaniciAdi() != null && !aktifKullanici.getKullaniciAdi().isBlank()) {
            return aktifKullanici.getKullaniciAdi();
        }

        return aktifKullanici.getEposta();
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public Kullanici getAktifKullanici() {
        return aktifKullanici;
    }

    public void setAktifKullanici(Kullanici aktifKullanici) {
        this.aktifKullanici = aktifKullanici;
    }

    public Kullanici getGirisYapanKullanici() {
        return aktifKullanici;
    }
}
