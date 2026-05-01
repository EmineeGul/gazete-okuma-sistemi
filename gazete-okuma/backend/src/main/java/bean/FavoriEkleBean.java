package bean;

import entity.FavoriHaber;
import entity.Haber;
import entity.Kullanici;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import facadeLocal.FavoriHaberFacadeLocal;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("favoriEkleBean")
@ViewScoped
public class FavoriEkleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FavoriHaberFacadeLocal favoriHaberFacade;

    private List<FavoriHaber> favoriler;

    @PostConstruct
    public void init() {
        Kullanici kullanici = sessionKullanicisiniGetir();
        if (kullanici == null) {
            favoriler = Collections.emptyList();
            return;
        }

        favoriler = favoriHaberFacade.kullanicininFavorileriniGetir(kullanici);
    }

    public String favoriyeEkle(Haber haber) {
        if (haber == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Favorilere eklenecek haber bulunamadi."));
            return null;
        }

        Kullanici kullanici = sessionKullanicisiniGetir();
        if (kullanici == null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyari", "Favorilere eklemek için giriş yapmalısınız."));
            return "/giris.xhtml?faces-redirect=true";
        }

        if (favoriHaberFacade.favoriVarMi(kullanici, haber)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Uyari", "Bu haber zaten favorilerde."));
            return null;
        }

        FavoriHaber favoriHaber = new FavoriHaber();
        favoriHaber.setKullanici(kullanici);
        favoriHaber.setHaber(haber);
        favoriHaberFacade.ekle(favoriHaber);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Basarili", "Haber favorilere eklendi."));
        return null;
    }

    public List<FavoriHaber> getFavoriler() {
        return favoriler;
    }

    private Kullanici sessionKullanicisiniGetir() {
        Object kullanici = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");
        return kullanici instanceof Kullanici ? (Kullanici) kullanici : null;
    }
}
