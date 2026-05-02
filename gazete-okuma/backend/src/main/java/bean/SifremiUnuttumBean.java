package bean;

import entity.Kullanici;
import facadeLocal.KullaniciFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.regex.Pattern;

@Named("sifremiUnuttumBean")
@ViewScoped
public class SifremiUnuttumBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Pattern SIFRE_DESENI =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,}$");

    @EJB
    private KullaniciFacadeLocal kullaniciFacade;

    private String eposta;
    private String yeniSifre;
    private String yeniSifreTekrar;
    private boolean epostaDogrulandi;
    private Kullanici bulunanKullanici;

    public String epostaDogrula() {
        if (eposta == null || eposta.trim().isEmpty()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "E-posta alani bos birakilamaz.");
            return null;
        }

        bulunanKullanici = kullaniciFacade.epostaIleBul(eposta.trim());
        if (bulunanKullanici == null) {
            epostaDogrulandi = false;
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu e-posta ile kayitli kullanici bulunamadi.");
            return null;
        }

        epostaDogrulandi = true;
        mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "E-posta dogrulandi. Simdi yeni sifrenizi belirleyin.");
        return null;
    }

    public String sifreyiYenile() {
        if (!epostaDogrulandi || bulunanKullanici == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Once e-posta dogrulamasi yapilmalidir.");
            return null;
        }

        if (yeniSifre == null || yeniSifre.trim().isEmpty() || yeniSifreTekrar == null || yeniSifreTekrar.trim().isEmpty()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yeni sifre alanlari bos birakilamaz.");
            return null;
        }

        if (!yeniSifre.equals(yeniSifreTekrar)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yeni sifre ile tekrar ayni olmalidir.");
            return null;
        }

        if (bulunanKullanici.getSifre() != null && bulunanKullanici.getSifre().equals(yeniSifre)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yeni sifre eski sifreyle ayni olamaz.");
            return null;
        }

        if (!sifreKurallariGecerliMi(yeniSifre)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata",
                    "Yeni sifre en az 8 karakter olmali, buyuk harf, kucuk harf ve ozel karakter icermelidir.");
            return null;
        }

        try {
            bulunanKullanici.setSifre(yeniSifre);
            kullaniciFacade.guncelle(bulunanKullanici);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Sifre basariyla guncellendi.");
            alanlariTemizle();
            return "/giris.xhtml?faces-redirect=true";
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Sifre guncellenemedi.");
            return null;
        }
    }

    public boolean sifreKurallariGecerliMi(String sifre) {
        return sifre != null && SIFRE_DESENI.matcher(sifre).matches();
    }

    private void alanlariTemizle() {
        yeniSifre = null;
        yeniSifreTekrar = null;
        epostaDogrulandi = false;
        bulunanKullanici = null;
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getYeniSifre() {
        return yeniSifre;
    }

    public void setYeniSifre(String yeniSifre) {
        this.yeniSifre = yeniSifre;
    }

    public String getYeniSifreTekrar() {
        return yeniSifreTekrar;
    }

    public void setYeniSifreTekrar(String yeniSifreTekrar) {
        this.yeniSifreTekrar = yeniSifreTekrar;
    }

    public boolean isEpostaDogrulandi() {
        return epostaDogrulandi;
    }
}
