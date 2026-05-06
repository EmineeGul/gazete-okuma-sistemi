package bean;

import entity.Kullanici;
import entity.Yorum;
import enums.RoleEnum;
import facadeLocal.YorumBegeniFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("yorumBegeniBean")
@ViewScoped
public class YorumBegeniBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private YorumBegeniFacadeLocal yorumBegeniFacade;

    public String yorumBegeniDegistir(Yorum yorum) {
        Kullanici kullanici = girisYapanKullaniciGetir();

        if (kullanici == null) {
            return "/giris.xhtml?faces-redirect=true";
        }

        if (adminMi(kullanici) || yorum == null || yorum.getId() == null) {
            return null;
        }

        if (yorumBegeniFacade.yorumBegeniVarMi(kullanici, yorum)) {
            yorumBegeniFacade.yorumBegeniKaldir(kullanici, yorum);
        } else {
            yorumBegeniFacade.yorumuBegen(kullanici, yorum);
        }

        return null;
    }

    public boolean yorumBegenildiMi(Yorum yorum) {
        Kullanici kullanici = girisYapanKullaniciGetir();

        if (kullanici == null || adminMi(kullanici)) {
            return false;
        }

        return yorumBegeniFacade.yorumBegeniVarMi(kullanici, yorum);
    }

    public long yorumBegeniSayisi(Yorum yorum) {
        return yorumBegeniFacade.yorumBegeniSayisi(yorum);
    }

    private Kullanici girisYapanKullaniciGetir() {
        Object kullaniciObj = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        return kullaniciObj instanceof Kullanici ? (Kullanici) kullaniciObj : null;
    }

    private boolean adminMi(Kullanici kullanici) {
        return kullanici != null
                && kullanici.getRole() != null
                && kullanici.getRole() == RoleEnum.ADMIN;
    }
}
