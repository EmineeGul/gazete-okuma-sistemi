package bean;

import entity.Haber;
import entity.Kategori;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import facadeLocal.HaberFacadeLocal;
import facadeLocal.KategoriFacadeLocal;

@Named("kategoriFiltreBean")
@ViewScoped
public class KategoriFiltreBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private HaberFacadeLocal haberFacade;

    @EJB
    private KategoriFacadeLocal kategoriFacade;

    private List<Haber> haberler;
    private List<Kategori> kategoriler;
    private Long secilenKategoriId;

    @PostConstruct
    public void init() {
        kategorileriYukle();
        haberler = haberFacade.tumHaberleriGetir();
    }

    public void kategorileriYukle() {
        kategoriler = kategoriFacade.tumunuGetir();
    }

    public void kategoriyeGoreFiltrele() {
        if (secilenKategoriId == null) {
            haberler = haberFacade.tumHaberleriGetir();
            return;
        }

        Kategori kategori = kategoriFacade.idIleBul(secilenKategoriId);
        haberler = kategori == null
                ? haberFacade.tumHaberleriGetir()
                : haberFacade.kategoriyeAitHaberleriGetir(kategori);
    }

    public void filtreyiTemizle() {
        secilenKategoriId = null;
        haberler = haberFacade.tumHaberleriGetir();
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

    public Long getSecilenKategoriId() {
        return secilenKategoriId;
    }

    public void setSecilenKategoriId(Long secilenKategoriId) {
        this.secilenKategoriId = secilenKategoriId;
    }
}
