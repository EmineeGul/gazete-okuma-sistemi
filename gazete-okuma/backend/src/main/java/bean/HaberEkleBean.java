package bean;

import entity.GazeteKaynagi;
import entity.Haber;
import entity.Kategori;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import facadeLocal.GazeteKaynagiFacadeLocal;
import facadeLocal.HaberFacadeLocal;
import facadeLocal.KategoriFacadeLocal;

@Named("haberEkleBean")
@ViewScoped
public class HaberEkleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private HaberFacadeLocal haberFacade;

    @EJB
    private KategoriFacadeLocal kategoriFacade;

    @EJB
    private GazeteKaynagiFacadeLocal gazeteKaynagiFacade;

    private String baslik;
    private String ozet;
    private String icerik;
    private String gorselUrl;
    private String haberLinki;
    private Long secilenKategoriId;
    private Long secilenKaynakId;
    private List<Kategori> kategoriler;
    private List<GazeteKaynagi> gazeteKaynaklari;

    @PostConstruct
    public void init() {
        kategoriler = kategoriFacade.tumunuGetir();
        gazeteKaynaklari = gazeteKaynagiFacade.tumunuGetir();
    }

    public void haberKaydet() {
        Kategori kategori = kategoriFacade.idIleBul(secilenKategoriId);
        GazeteKaynagi gazeteKaynagi = gazeteKaynagiFacade.idIleBul(secilenKaynakId);

        if (kategori == null || gazeteKaynagi == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kategori veya gazete kaynağı bulunamadı."));
            return;
        }

        Haber haber = new Haber();
        haber.setBaslik(baslik);
        haber.setOzet(ozet);
        haber.setIcerik(icerik);
        haber.setGorselUrl(gorselUrl);
        haber.setHaberLinki(haberLinki);
        haber.setKategori(kategori);
        haber.setHaberKaynagi(gazeteKaynagi);
        haber.setYayinTarihi(LocalDateTime.now());

        haberFacade.ekle(haber);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Haber başarıyla eklendi."));

        alanlariTemizle();
    }

    private void alanlariTemizle() {
        baslik = null;
        ozet = null;
        icerik = null;
        gorselUrl = null;
        haberLinki = null;
        secilenKategoriId = null;
        secilenKaynakId = null;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getOzet() {
        return ozet;
    }

    public void setOzet(String ozet) {
        this.ozet = ozet;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getGorselUrl() {
        return gorselUrl;
    }

    public void setGorselUrl(String gorselUrl) {
        this.gorselUrl = gorselUrl;
    }

    public String getHaberLinki() {
        return haberLinki;
    }

    public void setHaberLinki(String haberLinki) {
        this.haberLinki = haberLinki;
    }

    public Long getSecilenKategoriId() {
        return secilenKategoriId;
    }

    public void setSecilenKategoriId(Long secilenKategoriId) {
        this.secilenKategoriId = secilenKategoriId;
    }

    public Long getSecilenKaynakId() {
        return secilenKaynakId;
    }

    public void setSecilenKaynakId(Long secilenKaynakId) {
        this.secilenKaynakId = secilenKaynakId;
    }

    public List<Kategori> getKategoriler() {
        return kategoriler;
    }

    public void setKategoriler(List<Kategori> kategoriler) {
        this.kategoriler = kategoriler;
    }

    public List<GazeteKaynagi> getGazeteKaynaklari() {
        return gazeteKaynaklari;
    }

    public void setGazeteKaynaklari(List<GazeteKaynagi> gazeteKaynaklari) {
        this.gazeteKaynaklari = gazeteKaynaklari;
    }
}
