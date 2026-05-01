package bean;

import entity.GazeteKaynagi;
import entity.Haber;
import entity.Kategori;
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
        kategoriler = kategoriFacade.tumunuGetir();
        kaynaklar = gazeteKaynagiFacade.tumunuGetir();
        if (haber == null) {
            haber = new Haber();
        }
    }

    public String haberEkle() {
        if (kategoriId == null || kaynakId == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Kategori ve kaynak secilmelidir.");
            return null;
        }

        Kategori kategori = kategoriFacade.idIleBul(kategoriId);
        GazeteKaynagi kaynak = gazeteKaynagiFacade.idIleBul(kaynakId);

        if (kategori == null || kaynak == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Kategori veya kaynak bulunamadi.");
            return null;
        }

        if (yayinTarihiText != null && !yayinTarihiText.isBlank()) {
            haber.setYayinTarihi(LocalDateTime.parse(yayinTarihiText, FORM_FORMATTER));
        }

        haber.setKategori(kategori);
        haber.setHaberKaynagi(kaynak);
        haberFacade.ekle(haber);

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getFlash()
                .setKeepMessages(true);
        mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Haber basariyla eklendi.");

        alanlariTemizle();
        return "/panel/admin-haber-liste.xhtml?faces-redirect=true";
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
