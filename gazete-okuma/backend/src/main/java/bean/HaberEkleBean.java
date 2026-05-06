package bean;

import entity.GazeteKaynagi;
import entity.Haber;
import entity.Kategori;
import entity.Kullanici;
import facadeLocal.GazeteKaynagiFacadeLocal;
import facadeLocal.HaberFacadeLocal;
import facadeLocal.KategoriFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Named("haberEkleBean")
@ViewScoped
public class HaberEkleBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORM_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @EJB
    private HaberFacadeLocal haberFacade;

    @EJB
    private KategoriFacadeLocal kategoriFacade;

    @EJB
    private GazeteKaynagiFacadeLocal gazeteKaynagiFacade;

    private Haber haber;
    private String yayinTarihiText;
    private Long kategoriId;
    private Long kaynakId;
    private List<Kategori> kategoriler;
    private List<GazeteKaynagi> kaynaklar;

    @PostConstruct
    public void init() {
        haber = new Haber();
        kategoriler = kategoriFacade.tumunuGetir();
        kaynaklar = gazeteKaynagiFacade.tumunuGetir();
        if (kategoriler == null) {
            kategoriler = Collections.emptyList();
        }
        if (kaynaklar == null) {
            kaynaklar = Collections.emptyList();
        }
        System.out.println("Kategori sayisi: " + kategoriler.size());
        System.out.println("Kaynak sayisi: " + kaynaklar.size());

        if (kategoriler.isEmpty() || kaynaklar.isEmpty()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Once kategori ve gazete kaynagi verisi eklenmelidir.");
        }
    }

    public String haberEkle() {
        if (haber == null) {
            haber = new Haber();
        }

        if (kategoriler == null || kategoriler.isEmpty() || kaynaklar == null || kaynaklar.isEmpty()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Once kategori ve gazete kaynagi verisi eklenmelidir.");
            return null;
        }

        if (kategoriId == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Kategori secilmelidir.");
            return null;
        }

        if (kaynakId == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Gazete kaynagi secilmelidir.");
            return null;
        }

        if (yayinTarihiText == null || yayinTarihiText.isBlank()) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yayin tarihi zorunludur.");
            return null;
        }

        try {
            Kategori kategori = kategoriFacade.idIleBul(kategoriId);
            GazeteKaynagi kaynak = gazeteKaynagiFacade.idIleBul(kaynakId);

            if (kategori == null) {
                mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Secilen kategori bulunamadi.");
                return null;
            }

            if (kaynak == null) {
                mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Secilen gazete kaynagi bulunamadi.");
                return null;
            }

            try {
                haber.setYayinTarihi(LocalDateTime.parse(yayinTarihiText, FORM_FORMATTER));
            } catch (DateTimeParseException e) {
                mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yayin tarihi formati hatalidir.");
                return null;
            }

            haber.setKategori(kategori);
            haber.setGazeteKaynagi(kaynak);

            LocalDateTime simdi = LocalDateTime.now();
            haber.setOlusturulmaTarihi(simdi);
            haber.setGuncellenmeTarihi(simdi);
            haber.setSonGuncelleyenAdmin(girisYapanAdminAdiniGetir());
            haber.setGoruntulenmeSayisi(0L);

            haberFacade.ekle(haber);

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);
            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Haber basariyla eklendi.");

            alanlariTemizle();
            return "/panel/admin-haber-liste.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Haber eklenirken hata olustu: " + e.getMessage());
            return null;
        }
    }

    public String haberKaydet() {
        return haberEkle();
    }

    private void alanlariTemizle() {
        haber = new Haber();
        yayinTarihiText = null;
        kategoriId = null;
        kaynakId = null;
    }

    private void mesajEkle(FacesMessage.Severity severity, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, baslik, detay));
    }

    private String girisYapanAdminAdiniGetir() {
        Object kullaniciObj = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        if (kullaniciObj instanceof Kullanici) {
            Kullanici kullanici = (Kullanici) kullaniciObj;

            if (kullanici.getKullaniciAdi() != null && !kullanici.getKullaniciAdi().isBlank()) {
                return kullanici.getKullaniciAdi();
            }

            if (kullanici.getEposta() != null && !kullanici.getEposta().isBlank()) {
                return kullanici.getEposta();
            }
        }

        return "Bilinmeyen Admin";
    }

    public Haber getHaber() {
        if (haber == null) {
            haber = new Haber();
        }
        return haber;
    }

    public void setHaber(Haber haber) {
        this.haber = haber;
    }

    public String getYayinTarihiText() {
        return yayinTarihiText;
    }

    public void setYayinTarihiText(String yayinTarihiText) {
        this.yayinTarihiText = yayinTarihiText;
    }

    public Long getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(Long kategoriId) {
        this.kategoriId = kategoriId;
    }

    public Long getKaynakId() {
        return kaynakId;
    }

    public void setKaynakId(Long kaynakId) {
        this.kaynakId = kaynakId;
    }

    public List<Kategori> getKategoriler() {
        return kategoriler;
    }

    public void setKategoriler(List<Kategori> kategoriler) {
        this.kategoriler = kategoriler;
    }

    public List<GazeteKaynagi> getKaynaklar() {
        return kaynaklar;
    }

    public void setKaynaklar(List<GazeteKaynagi> kaynaklar) {
        this.kaynaklar = kaynaklar;
    }

}
