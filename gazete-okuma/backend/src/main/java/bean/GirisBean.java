package bean;

import entity.Kullanici;
import enums.RoleEnum;
import facadeLocal.KullaniciFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named("girisBean")
@ViewScoped
public class GirisBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private KullaniciFacadeLocal kullaniciFacade;

    private String eposta;
    private String sifre;
    private Kullanici aktifKullanici;

    public String girisYap() {
        Kullanici kullanici = kullaniciFacade.epostaIleBul(eposta);

        if (kullanici == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanici bulunamadi", null));
            return null;
        }

        if (sifre != null && sifre.equals(kullanici.getSifre())) {
            aktifKullanici = kullanici;
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", kullanici);
            return "/index.xhtml?faces-redirect=true";
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sifre hatali", null));
        return null;
    }

    public String adminGirisYap() {
        Kullanici kullanici = kullaniciFacade.epostaIleBul(eposta);

        if (kullanici == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanici bulunamadi", null));
            return null;
        }

        if (sifre == null || !sifre.equals(kullanici.getSifre())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sifre hatali", null));
            return null;
        }

        if (kullanici.getRole() == null || kullanici.getRole() != RoleEnum.ADMIN) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Bu alan sadece yoneticiler icindir."));
            return null;
        }

        aktifKullanici = kullanici;
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("user", kullanici);
        return "/panel/index.xhtml?faces-redirect=true";
    }

    public String cikisYap() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/giris.xhtml?faces-redirect=true";
    }

    public boolean girisYapilmisMi() {
        return sessionKullanicisiniGetir() != null;
    }

    public boolean adminMi() {
        Object userObj = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        if (userObj instanceof Kullanici) {
            Kullanici kullanici = (Kullanici) userObj;
            return kullanici.getRole() != null
                    && kullanici.getRole() == RoleEnum.ADMIN;
        }

        return false;
    }

    public void adminKontrolu() throws IOException {
        if (!adminMi()) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(
                    facesContext.getExternalContext().getRequestContextPath() + "/index.xhtml"
            );
            facesContext.responseComplete();
        }
    }

    public String getHesapGosterim() {
        Kullanici kullanici = sessionKullanicisiniGetir();
        if (kullanici == null) {
            return "Misafir";
        }

        if (kullanici.getKullaniciAdi() != null && !kullanici.getKullaniciAdi().isBlank()) {
            return kullanici.getKullaniciAdi();
        }

        return kullanici.getEposta();
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
        return sessionKullanicisiniGetir();
    }

    public void setAktifKullanici(Kullanici aktifKullanici) {
        this.aktifKullanici = aktifKullanici;
    }

    public Kullanici getGirisYapanKullanici() {
        return sessionKullanicisiniGetir();
    }

    private Kullanici sessionKullanicisiniGetir() {
        Object kullanici = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");
        return kullanici instanceof Kullanici ? (Kullanici) kullanici : aktifKullanici;
    }
}
