package com.example.gazete.controller;

import com.example.gazete.model.entity.Kullanici;
import com.example.gazete.model.facade.KullaniciFacade;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Named("kayitBean")
@ViewScoped
public class KayitBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Pattern SIFRE_DESENI =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,}$");

    @Inject
    private KullaniciFacade kullaniciFacade;

    private String kullaniciAdi;
    private String ad;
    private String soyad;
    private String eposta;
    private String sifre;
    private String sifreTekrar;

    public String kayitOl() {
        if (bosAlanVarMi()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Tum alanlarin doldurulmasi zorunludur.");
            return null;
        }

        if (!sifre.equals(sifreTekrar)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Sifre ve sifre tekrari ayni olmalidir.");
            return null;
        }

        if (!SIFRE_DESENI.matcher(sifre).matches()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata",
                    "Sifre en az 8 karakter olmali, buyuk harf, kucuk harf ve ozel karakter icermelidir.");
            return null;
        }

        if (kullaniciFacade.epostayaGoreBul(eposta) != null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu e-posta adresi ile kayitli kullanici bulunuyor.");
            return null;
        }

        try {
            Kullanici kullanici = new Kullanici();
            kullanici.setKullaniciAdi(kullaniciAdi);
            kullanici.setAd(ad);
            kullanici.setSoyad(soyad);
            kullanici.setEposta(eposta);
            kullanici.setSifre(sifre);
            kullanici.setKayitTarihi(LocalDateTime.now());

            kullaniciFacade.create(kullanici);
            return "giris.xhtml?faces-redirect=true";
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Kayit islemi sirasinda bir hata olustu.");
            return null;
        }
    }

    private boolean bosAlanVarMi() {
        return bosMu(kullaniciAdi)
                || bosMu(ad)
                || bosMu(soyad)
                || bosMu(eposta)
                || bosMu(sifre)
                || bosMu(sifreTekrar);
    }

    private boolean bosMu(String deger) {
        return deger == null || deger.trim().isEmpty();
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
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

    public String getSifreTekrar() {
        return sifreTekrar;
    }

    public void setSifreTekrar(String sifreTekrar) {
        this.sifreTekrar = sifreTekrar;
    }
}
