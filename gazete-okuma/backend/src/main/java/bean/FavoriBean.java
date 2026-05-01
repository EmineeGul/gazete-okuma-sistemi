package bean;

import entity.FavoriHaber;
import entity.Haber;
import entity.Kullanici;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import facadeLocal.FavoriHaberFacadeLocal;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named("favoriBean")
@ViewScoped
public class FavoriBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String GIRIS_UYARI_MESAJI = "Favorilere eklemek için giriş yapmalısınız.";

    @EJB
    private FavoriHaberFacadeLocal favoriHaberFacade;

    private List<FavoriHaber> favoriler;
    private Set<Long> favoriHaberIdleri;

    @PostConstruct
    public void init() {
        favorileriYukle();
    }

    public void favorileriYukle() {
        Kullanici kullanici = sessionKullanicisiniGetir();
        if (kullanici == null) {
            favoriler = new ArrayList<>();
            favoriHaberIdleri = new HashSet<>();
            return;
        }

        favoriler = favoriHaberFacade.kullanicininFavorileriniGetir(kullanici);
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

        Kullanici kullanici = girisZorunluYonlendir();
        if (kullanici == null) {
            return "/giris.xhtml?faces-redirect=true";
        }

        FavoriHaber favoriHaber = favoriHaberFacade.kullanicininFavorisiniBul(kullanici, haber);
        if (favoriHaber != null) {
            favoriHaberFacade.sil(favoriHaber);
        } else {
            FavoriHaber yeniFavori = new FavoriHaber();
            yeniFavori.setKullanici(kullanici);
            yeniFavori.setHaber(haber);
            favoriHaberFacade.ekle(yeniFavori);
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

    public String favoridenCikar(FavoriHaber favoriHaber) {
        Kullanici kullanici = girisZorunluYonlendir();
        if (kullanici == null) {
            return "/giris.xhtml?faces-redirect=true";
        }

        if (favoriHaber == null || favoriHaber.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Cikarilacak favori kaydi bulunamadi.");
            return null;
        }

        if (favoriHaber.getKullanici() == null
                || kullanici.getId() == null
                || !kullanici.getId().equals(favoriHaber.getKullanici().getId())) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu favori kaydi size ait degil.");
            favorileriYukle();
            return null;
        }

        favoriHaberFacade.sil(favoriHaber);
        favorileriYukle();
        return null;
    }

    public void favorilerSayfasiErisimKontrolu() throws IOException {
        if (sessionKullanicisiniGetir() != null) {
            favorileriYukle();
            return;
        }

        mesajEkle(FacesMessage.SEVERITY_WARN, "Uyari", GIRIS_UYARI_MESAJI);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.getFlash().setKeepMessages(true);
        externalContext.redirect(externalContext.getRequestContextPath() + "/giris.xhtml");
        facesContext.responseComplete();
    }

    private Kullanici girisZorunluYonlendir() {
        Kullanici kullanici = sessionKullanicisiniGetir();
        if (kullanici != null) {
            return kullanici;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        mesajEkle(FacesMessage.SEVERITY_WARN, "Uyari", GIRIS_UYARI_MESAJI);
        favoriler = new ArrayList<>();
        favoriHaberIdleri = new HashSet<>();
        return null;
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    private Kullanici sessionKullanicisiniGetir() {
        Object kullanici = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");
        return kullanici instanceof Kullanici ? (Kullanici) kullanici : null;
    }

    public List<FavoriHaber> getFavoriler() {
        return favoriler;
    }

    public void setFavoriler(List<FavoriHaber> favoriler) {
        this.favoriler = favoriler;
    }
}
