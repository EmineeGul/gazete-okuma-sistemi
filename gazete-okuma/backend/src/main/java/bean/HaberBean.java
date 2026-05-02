package bean;

import entity.Haber;
import facadeLocal.FavoriHaberFacadeLocal;
import facadeLocal.HaberFacadeLocal;
import facadeLocal.YorumFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("haberBean")
@ViewScoped
public class HaberBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private HaberFacadeLocal haberFacade;

    @EJB
    private FavoriHaberFacadeLocal favoriHaberFacade;

    @EJB
    private YorumFacadeLocal yorumFacade;

    private Long haberId;
    private List<Haber> haberler;
    private Haber seciliHaber;

    @PostConstruct
    public void init() {
        haberleriYukle();
    }

    public void haberleriYukle() {
        try {
            haberler = haberFacade.tumHaberleriGetir();
        } catch (Exception e) {
            haberler = Collections.emptyList();
        }
    }

    public void seciliHaberiYukle() {
        seciliHaber = null;

        if (haberId == null) {
            return;
        }

        try {
            seciliHaber = haberFacade.idIleBul(haberId);
        } catch (Exception e) {
            seciliHaber = null;
        }
    }

    public String haberSil(Haber haber) {
        if (haber == null || haber.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek haber bulunamadi.");
            return null;
        }

        try {
            Haber silinecekHaber = haberFacade.idIleBul(haber.getId());

            if (silinecekHaber == null) {
                mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Haber veritabaninda bulunamadi.");
                return null;
            }

            yorumFacade.habereAitYorumlariSil(silinecekHaber);
            favoriHaberFacade.habereAitFavorileriSil(silinecekHaber);
            haberFacade.sil(silinecekHaber);
            haberleriYukle();
            seciliHaber = null;

            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Haber basariyla silindi.");
            return "/panel/admin-haber-liste.xhtml?faces-redirect=true";
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Haber silinemedi.");
            return null;
        }
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    public List<Haber> getHaberler() {
        if (haberler == null) {
            haberleriYukle();
        }
        return haberler;
    }

    public void setHaberler(List<Haber> haberler) {
        this.haberler = haberler;
    }

    public Long getHaberId() {
        return haberId;
    }

    public void setHaberId(Long haberId) {
        this.haberId = haberId;
    }

    public Haber getSeciliHaber() {
        return seciliHaber;
    }

    public void setSeciliHaber(Haber seciliHaber) {
        this.seciliHaber = seciliHaber;
    }

}
