package bean;

import entity.Haber;
import entity.Kullanici;
import entity.Yorum;
import enums.RoleEnum;
import facadeLocal.HaberFacadeLocal;
import facadeLocal.YorumFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Named("yorumBean")
@ViewScoped
public class YorumBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private YorumFacadeLocal yorumFacade;

    @EJB
    private HaberFacadeLocal haberFacade;

    private Long haberId;
    private Haber haber;
    private String yorumIcerik;
    private List<Yorum> yorumlar;
    private List<Yorum> tumYorumlar;

    @PostConstruct
    public void init() {
        yorumlar = Collections.emptyList();
        tumYorumlar = Collections.emptyList();
    }

    public void yorumlariYukle() {
        yorumlar = Collections.emptyList();
        haber = null;

        if (haberId != null) {
            haber = haberFacade.idIleBul(haberId);
            if (haber != null) {
                yorumlar = yorumFacade.habereGoreYorumlariGetir(haber);
            }
        }
    }

    public void tumYorumlariYukle() {
        tumYorumlar = Collections.emptyList();

        if (!adminMi()) {
            return;
        }

        tumYorumlar = yorumFacade.tumYorumlariGetir();
    }

    public String yorumEkle() {
        Kullanici kullanici = getGirisYapanKullanici();

        if (kullanici == null) {
            mesajEkle(FacesMessage.SEVERITY_WARN, "Uyari", "Yorum yapmak icin giris yapmalisiniz.");
            return "/giris.xhtml?faces-redirect=true";
        }

        if (adminMi()) {
            mesajEkle(FacesMessage.SEVERITY_WARN, "Uyari", "Yoneticiler yorum yapamaz.");
            return null;
        }

        if (haber == null || haber.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum yapilacak haber bulunamadi.");
            return null;
        }

        if (yorumIcerik == null || yorumIcerik.trim().isEmpty()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum bos olamaz.");
            return null;
        }

        yorumIcerik = yorumIcerik.trim();

        if (yorumIcerik.length() > 300) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum en fazla 300 karakter olabilir.");
            return null;
        }

        if (yasakliIcerikVarMi(yorumIcerik)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum uygun olmayan icerik iceriyor.");
            return null;
        }

        Yorum yorum = new Yorum();
        yorum.setHaber(haber);
        yorum.setKullanici(kullanici);
        yorum.setIcerik(yorumIcerik);
        yorum.setYorumTarihi(LocalDateTime.now());

        yorumFacade.ekle(yorum);

        yorumIcerik = null;
        yorumlariYukle();

        return null;
    }

    public String yorumSil(Yorum yorum) {
        if (!adminMi()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu islem sadece yoneticiler icindir.");
            return null;
        }

        if (yorum == null || yorum.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek yorum bulunamadi.");
            return null;
        }

        yorumFacade.sil(yorum);
        tumYorumlariYukle();
        yorumlariYukle();

        return null;
    }

    private boolean yasakliIcerikVarMi(String metin) {
        String kucuk = metin.toLowerCase(new Locale("tr", "TR"));

        String[] yasakliKelimeler = {
            "kufur1", "kufur2", "hakaret", "siyasi", "propaganda"
        };

        for (String kelime : yasakliKelimeler) {
            if (kucuk.contains(kelime)) {
                return true;
            }
        }

        return false;
    }

    private Kullanici getGirisYapanKullanici() {
        Object userObj = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        if (userObj instanceof Kullanici) {
            return (Kullanici) userObj;
        }

        return null;
    }

    private boolean adminMi() {
        Kullanici kullanici = getGirisYapanKullanici();
        return kullanici != null
                && kullanici.getRole() != null
                && kullanici.getRole() == RoleEnum.ADMIN;
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    public Long getHaberId() {
        return haberId;
    }

    public void setHaberId(Long haberId) {
        this.haberId = haberId;
    }

    public Haber getHaber() {
        return haber;
    }

    public void setHaber(Haber haber) {
        this.haber = haber;
    }

    public String getYorumIcerik() {
        return yorumIcerik;
    }

    public void setYorumIcerik(String yorumIcerik) {
        this.yorumIcerik = yorumIcerik;
    }

    public List<Yorum> getYorumlar() {
        return yorumlar;
    }

    public void setYorumlar(List<Yorum> yorumlar) {
        this.yorumlar = yorumlar;
    }

    public List<Yorum> getTumYorumlar() {
        return tumYorumlar;
    }

    public void setTumYorumlar(List<Yorum> tumYorumlar) {
        this.tumYorumlar = tumYorumlar;
    }
}
