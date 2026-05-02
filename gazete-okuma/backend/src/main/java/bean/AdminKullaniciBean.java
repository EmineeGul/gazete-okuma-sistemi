package bean;

import entity.Kullanici;
import enums.RoleEnum;
import facadeLocal.FavoriHaberFacadeLocal;
import facadeLocal.KullaniciFacadeLocal;
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
import java.util.stream.Collectors;

@Named("adminKullaniciBean")
@ViewScoped
public class AdminKullaniciBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private KullaniciFacadeLocal kullaniciFacade;

    @EJB
    private YorumFacadeLocal yorumFacade;

    @EJB
    private FavoriHaberFacadeLocal favoriHaberFacade;

    private List<Kullanici> kullanicilar;

    @PostConstruct
    public void init() {
        kullanicilariYukle();
    }

    public void kullanicilariYukle() {
        try {
            kullanicilar = kullaniciFacade.tumKullanicilariGetir()
                    .stream()
                    .filter(k -> k.getRole() == RoleEnum.USER)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            kullanicilar = Collections.emptyList();
        }
    }

    public long yorumSayisiGetir(Kullanici kullanici) {
        if (kullanici == null || kullanici.getId() == null) {
            return 0L;
        }

        return yorumFacade.kullaniciYorumSayisi(kullanici);
    }

    public long favoriSayisiGetir(Kullanici kullanici) {
        if (kullanici == null || kullanici.getId() == null) {
            return 0L;
        }

        return favoriHaberFacade.kullaniciFavoriSayisi(kullanici);
    }

    public String kullaniciSil(Kullanici kullanici) {
        if (kullanici == null || kullanici.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek kullanıcı bulunamadı.");
            return null;
        }

        if (kullanici.getRole() != RoleEnum.USER) {
            mesajEkle(FacesMessage.SEVERITY_WARN, "Uyarı", "Yönetici hesapları bu sayfadan silinemez.");
            kullanicilariYukle();
            return null;
        }

        yorumFacade.kullaniciYorumlariniSil(kullanici);
        favoriHaberFacade.kullaniciFavorileriniSil(kullanici);
        kullaniciFacade.sil(kullanici);

        kullanicilariYukle();
        mesajEkle(FacesMessage.SEVERITY_INFO, "Başarılı", "Kullanıcı başarıyla silindi.");
        return null;
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    public List<Kullanici> getKullanicilar() {
        return kullanicilar;
    }

    public void setKullanicilar(List<Kullanici> kullanicilar) {
        this.kullanicilar = kullanicilar;
    }
}