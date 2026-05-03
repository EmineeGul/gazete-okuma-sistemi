package bean;

import entity.Haber;
import facadeLocal.FavoriHaberFacadeLocal;
import facadeLocal.HaberFacadeLocal;
import facadeLocal.YorumFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named("haberBean")
@ViewScoped
public class HaberBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private HaberFacadeLocal haberFacade;

    @EJB
    private FavoriHaberFacadeLocal favoriHaberFacade;

    @EJB
    private YorumFacadeLocal yorumFacade;

    private Long haberId;
    private List<Haber> haberler;
    private Haber seciliHaber;
    private int aktifSayfa = 1;
    private int sayfaBoyutu = 10;
    private int toplamSayfaSayisi;
    private Long toplamHaberSayisi;
    private List<Integer> sayfaNumaralari;
    private int secilecekSayfa;

    @PostConstruct
    public void init() {
        if (anaSayfaMi()) {
            haberleriSayfaliYukle();
            return;
        }

        haberleriYukle();
    }

    public void haberleriYukle() {
        try {
            haberler = haberFacade.tumHaberleriGetir();
        } catch (Exception e) {
            haberler = Collections.emptyList();
        }
    }

    public void haberleriSayfaliYukle() {
        try {
            toplamHaberSayisi = haberFacade.toplamHaberSayisi();
            toplamSayfaSayisi = (int) Math.ceil(toplamHaberSayisi / (double) sayfaBoyutu);

            if (toplamSayfaSayisi == 0) {
                aktifSayfa = 1;
                haberler = Collections.emptyList();
                sayfaNumaralariniHazirla();
                return;
            }

            if (aktifSayfa < 1) {
                aktifSayfa = 1;
            } else if (aktifSayfa > toplamSayfaSayisi) {
                aktifSayfa = toplamSayfaSayisi;
            }

            int ilkKayit = (aktifSayfa - 1) * sayfaBoyutu;
            haberler = haberFacade.haberleriSayfaliGetir(ilkKayit, sayfaBoyutu);
            sayfaNumaralariniHazirla();
        } catch (Exception e) {
            toplamHaberSayisi = 0L;
            toplamSayfaSayisi = 0;
            haberler = Collections.emptyList();
            sayfaNumaralariniHazirla();
        }
    }

    public void sayfayaGit(int sayfa) {
        if (toplamSayfaSayisi <= 0) {
            aktifSayfa = 1;
            haberleriSayfaliYukle();
            return;
        }

        if (sayfa < 1) {
            sayfa = 1;
        } else if (sayfa > toplamSayfaSayisi) {
            sayfa = toplamSayfaSayisi;
        }

        aktifSayfa = sayfa;
        haberleriSayfaliYukle();
    }

    public void sayfayaGit() {
        sayfayaGit(secilecekSayfa);
    }

    public boolean oncekiSayfaVarMi() {
        return aktifSayfa > 1;
    }

    public boolean sonrakiSayfaVarMi() {
        return toplamSayfaSayisi > 0 && aktifSayfa < toplamSayfaSayisi;
    }

    public void oncekiSayfa() {
        if (oncekiSayfaVarMi()) {
            sayfayaGit(aktifSayfa - 1);
        }
    }

    public void sonrakiSayfa() {
        if (sonrakiSayfaVarMi()) {
            sayfayaGit(aktifSayfa + 1);
        }
    }

    private void sayfaNumaralariniHazirla() {
        sayfaNumaralari = new ArrayList<>();

        for (int sayfa = 1; sayfa <= toplamSayfaSayisi; sayfa++) {
            sayfaNumaralari.add(sayfa);
        }
    }

    public void seciliHaberiYukle() {
        seciliHaber = null;

        if (haberId == null) {
            return;
        }

        try {
            seciliHaber = haberFacade.idIleBul(haberId);
        } catch (Exception e) {
            seciliHaber = null;
        }
    }

    public String haberSil(Haber haber) {
        if (haber == null || haber.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek haber bulunamadi.");
            return null;
        }

        try {
            Haber silinecekHaber = haberFacade.idIleBul(haber.getId());

            if (silinecekHaber == null) {
                mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Haber veritabaninda bulunamadi.");
                return null;
            }

            yorumFacade.habereAitYorumlariSil(silinecekHaber);
            favoriHaberFacade.habereAitFavorileriSil(silinecekHaber);
            haberFacade.sil(silinecekHaber);
            haberleriYukle();
            seciliHaber = null;

            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Haber basariyla silindi.");
            return "/panel/admin-haber-liste.xhtml?faces-redirect=true";
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Haber silinemedi.");
            return null;
        }
    }

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    public List<Haber> getHaberler() {
        if (haberler == null) {
            if (anaSayfaMi()) {
                haberleriSayfaliYukle();
            } else {
                haberleriYukle();
            }
        }
        return haberler;
    }

    public void setHaberler(List<Haber> haberler) {
        this.haberler = haberler;
    }

    public Long getHaberId() {
        return haberId;
    }

    public void setHaberId(Long haberId) {
        this.haberId = haberId;
    }

    public Haber getSeciliHaber() {
        return seciliHaber;
    }

    public void setSeciliHaber(Haber seciliHaber) {
        this.seciliHaber = seciliHaber;
    }

    public int getAktifSayfa() {
        return aktifSayfa;
    }

    public void setAktifSayfa(int aktifSayfa) {
        this.aktifSayfa = aktifSayfa;
    }

    public int getSayfaBoyutu() {
        return sayfaBoyutu;
    }

    public Long getToplamHaberSayisi() {
        return toplamHaberSayisi;
    }

    public int getToplamSayfaSayisi() {
        return toplamSayfaSayisi;
    }

    public List<Integer> getSayfaNumaralari() {
        if (sayfaNumaralari == null) {
            sayfaNumaralariniHazirla();
        }
        return sayfaNumaralari;
    }

    public int getSecilecekSayfa() {
        return secilecekSayfa;
    }

    public void setSecilecekSayfa(int secilecekSayfa) {
        this.secilecekSayfa = secilecekSayfa;
    }

    private boolean anaSayfaMi() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext == null || facesContext.getViewRoot() == null) {
            return false;
        }

        String gorunumId = facesContext.getViewRoot().getViewId();
        return "/index.xhtml".equals(gorunumId);
    }

}
