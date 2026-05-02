package bean;

import entity.GazeteKaynagi;
import entity.Haber;
import entity.Kategori;
import facadeLocal.GazeteKaynagiFacadeLocal;
import facadeLocal.HaberFacadeLocal;
import facadeLocal.KategoriFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("kategoriFiltreBean")
@ViewScoped
public class KategoriFiltreBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private HaberFacadeLocal haberFacade;

    @EJB
    private KategoriFacadeLocal kategoriFacade;

    @EJB
    private GazeteKaynagiFacadeLocal gazeteKaynagiFacade;

    private List<Haber> haberler;
    private List<Kategori> kategoriler;
    private List<GazeteKaynagi> gazeteKaynaklari;
    private Long seciliKategoriId;
    private Long seciliKaynakId;

    @PostConstruct
    public void init() {
        kategoriler = guvenliListe(kategoriFacade.tumunuGetir());
        gazeteKaynaklari = guvenliListe(gazeteKaynagiFacade.tumunuGetir());
        haberler = haberFacade.tumHaberleriGetir();
    }

    public void filtrele() {
        if (seciliKategoriId == null && seciliKaynakId == null) {
            haberler = haberFacade.tumHaberleriGetir();
            return;
        }

        Kategori kategori = seciliKategoriId == null ? null : kategoriFacade.idIleBul(seciliKategoriId);
        GazeteKaynagi gazeteKaynagi = seciliKaynakId == null ? null : gazeteKaynagiFacade.idIleBul(seciliKaynakId);

        if (seciliKategoriId != null && kategori == null) {
            haberler = Collections.emptyList();
            return;
        }

        if (seciliKaynakId != null && gazeteKaynagi == null) {
            haberler = Collections.emptyList();
            return;
        }

        if (kategori != null && gazeteKaynagi != null) {
            haberler = haberFacade.kategoriVeKaynagaGoreHaberleriBul(kategori, gazeteKaynagi);
            return;
        }

        if (kategori != null) {
            haberler = haberFacade.kategoriyeAitHaberleriGetir(kategori);
            return;
        }

        haberler = haberFacade.kaynagaAitHaberleriGetir(gazeteKaynagi);
    }

    public void filtreyiTemizle() {
        seciliKategoriId = null;
        seciliKaynakId = null;
        haberler = haberFacade.tumHaberleriGetir();
    }

    private <T> List<T> guvenliListe(List<T> liste) {
        return liste == null ? Collections.emptyList() : liste;
    }

    public List<Haber> getHaberler() {
        return haberler;
    }

    public void setHaberler(List<Haber> haberler) {
        this.haberler = haberler;
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

    public Long getSeciliKategoriId() {
        return seciliKategoriId;
    }

    public void setSeciliKategoriId(Long seciliKategoriId) {
        this.seciliKategoriId = seciliKategoriId;
    }

    public Long getSeciliKaynakId() {
        return seciliKaynakId;
    }

    public void setSeciliKaynakId(Long seciliKaynakId) {
        this.seciliKaynakId = seciliKaynakId;
    }
}
