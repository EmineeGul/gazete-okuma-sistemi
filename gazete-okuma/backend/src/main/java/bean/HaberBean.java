package bean;

import entity.Haber;
import facadeLocal.HaberFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.annotation.PostConstruct;
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

    public void haberSil(Haber haber) {
        if (haber == null || haber.getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek haber bulunamadı."));
            return;
        }

        try {
            Haber silinecekHaber = haberFacade.idIleBul(haber.getId());

            if (silinecekHaber == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Haber veritabanında bulunamadı."));
                return;
            }

            haberFacade.sil(silinecekHaber);
            haberleriYukle();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Haber başarıyla silindi."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Haber silinemedi."));
        }
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
