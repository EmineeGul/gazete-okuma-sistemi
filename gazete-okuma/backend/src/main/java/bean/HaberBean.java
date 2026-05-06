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
    private static final int ANA_SAYFA_SAYFA_BOYUTU = 9;
    private static final int ADMIN_HABER_SAYFA_BOYUTU = 10;

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
    private int sayfaBoyutu = ANA_SAYFA_SAYFA_BOYUTU;
    private int toplamSayfaSayisi;
    private Long toplamHaberSayisi;
    private List<Integer> sayfaNumaralari;
    private int secilecekSayfa;
    private int toplamSayfa;
    private Long toplamHaber;
    private List<Haber> haberListesi;
    private List<Integer> sayfalar;
    private String aramaMetni;
    private boolean goruntulenmeArtirildiMi;

    @PostConstruct
    public void init() {
        if (adminHaberListeSayfasiMi()) {
            sayfaBoyutu = ADMIN_HABER_SAYFA_BOYUTU;
            toplamHaber = haberFacade.haberSayisiniGetir();
            toplamSayfa = toplamHaber > 0 ? (int) Math.ceil(toplamHaber / (double) sayfaBoyutu) : 0;
            sayfalariHazirla();
            haberleriYukle();
            return;
        }

        if (anaSayfaMi()) {
            sayfaBoyutu = ANA_SAYFA_SAYFA_BOYUTU;
            haberleriSayfaliYukle();
            return;
        }

        haberleriYukle();
    }

    public void haberleriYukle() {
        if (adminHaberListeSayfasiMi()) {
            try {
                if (toplamHaber == null) {
                    toplamHaber = haberFacade.haberSayisiniGetir();
                }

                toplamSayfa = toplamHaber > 0 ? (int) Math.ceil(toplamHaber / (double) sayfaBoyutu) : 0;

                if (toplamSayfa == 0) {
                    aktifSayfa = 1;
                    haberListesi = Collections.emptyList();
                    haberler = haberListesi;
                    sayfalariHazirla();
                    return;
                }

                if (aktifSayfa < 1) {
                    aktifSayfa = 1;
                } else if (aktifSayfa > toplamSayfa) {
                    aktifSayfa = toplamSayfa;
                }

                int baslangic = (aktifSayfa - 1) * sayfaBoyutu;
                haberListesi = haberFacade.adminSayfaliHaberleriGetir(baslangic, sayfaBoyutu);
                haberler = haberListesi;
                sayfalariHazirla();
            } catch (Exception e) {
                toplamHaber = 0L;
                toplamSayfa = 0;
                haberListesi = Collections.emptyList();
                haberler = haberListesi;
                sayfalariHazirla();
            }
            return;
        }

        try {
            haberler = haberFacade.tumHaberleriGetir();
        } catch (Exception e) {
            haberler = Collections.emptyList();
        }
    }

    public void haberleriSayfaliYukle() {
        try {
            sayfaBoyutu = ANA_SAYFA_SAYFA_BOYUTU;
            toplamHaberSayisi = aramaMetniVarMi()
                    ? haberFacade.arananHaberSayisi(aramaMetni)
                    : haberFacade.toplamHaberSayisi();
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
            haberler = aramaMetniVarMi()
                    ? haberFacade.haberleriAraSayfaliGetir(aramaMetni, ilkKayit, sayfaBoyutu)
                    : haberFacade.haberleriSayfaliGetir(ilkKayit, sayfaBoyutu);
            sayfaNumaralariniHazirla();
        } catch (Exception e) {
            toplamHaberSayisi = 0L;
            toplamSayfaSayisi = 0;
            haberler = Collections.emptyList();
            sayfaNumaralariniHazirla();
        }
    }

    public String haberAra() {
        aktifSayfa = 1;
        haberleriSayfaliYukle();
        return null;
    }

    public String aramayiTemizle() {
        aramaMetni = null;
        aktifSayfa = 1;
        haberleriSayfaliYukle();
        return null;
    }

    public void sayfayaGit() {
        sayfayaGit(secilecekSayfa);
    }

    public void sayfayaGit(int sayfa) {
        if (!adminHaberListeSayfasiMi()) {
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
            return;
        }

        if (toplamSayfa <= 0) {
            aktifSayfa = 1;
            haberleriYukle();
            return;
        }

        if (sayfa < 1) {
            sayfa = 1;
        } else if (sayfa > toplamSayfa) {
            sayfa = toplamSayfa;
        }

        aktifSayfa = sayfa;
        haberleriYukle();
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

    public void onceki() {
        if (aktifSayfa > 1) {
            aktifSayfa--;
            haberleriYukle();
        }
    }

    public void sonraki() {
        if (aktifSayfa < toplamSayfa) {
            aktifSayfa++;
            haberleriYukle();
        }
    }

    private void sayfaNumaralariniHazirla() {
        sayfaNumaralari = new ArrayList<>();

        for (int sayfa = 1; sayfa <= toplamSayfaSayisi; sayfa++) {
            sayfaNumaralari.add(sayfa);
        }
    }

    private void sayfalariHazirla() {
        sayfalar = new ArrayList<>();

        for (int sayfa = 1; sayfa <= toplamSayfa; sayfa++) {
            sayfalar.add(sayfa);
        }
    }

    public void seciliHaberiYukle() {
        seciliHaber = null;

        if (haberId == null) {
            return;
        }

        try {
            seciliHaber = haberFacade.idIleBul(haberId);

            if (seciliHaber != null && !goruntulenmeArtirildiMi) {
                haberFacade.goruntulenmeSayisiniArtir(seciliHaber);
                seciliHaber.setGoruntulenmeSayisi(seciliHaber.getGoruntulenmeSayisi() + 1);
                goruntulenmeArtirildiMi = true;
            }
        } catch (Exception e) {
            seciliHaber = null;
        }
    }

    public Long goruntulenmeSayisiGetir(Haber haber) {
        return haber == null ? 0L : haber.getGoruntulenmeSayisi();
    }

    public long favoriSayisiGetir(Haber haber) {
        try {
            return favoriHaberFacade.haberiFavorileyenKullaniciSayisi(haber);
        } catch (Exception e) {
            return 0L;
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

            if (adminHaberListeSayfasiMi()) {
                toplamHaber = haberFacade.haberSayisiniGetir();
                toplamSayfa = toplamHaber > 0 ? (int) Math.ceil(toplamHaber / (double) sayfaBoyutu) : 0;

                if (toplamSayfa > 0 && aktifSayfa > toplamSayfa) {
                    aktifSayfa = toplamSayfa;
                }

                haberleriYukle();
            } else {
                haberleriYukle();
            }

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

    private boolean aramaMetniVarMi() {
        return aramaMetni != null && !aramaMetni.trim().isEmpty();
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

    public void setSayfaBoyutu(int sayfaBoyutu) {
        this.sayfaBoyutu = sayfaBoyutu;
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

    public String getAramaMetni() {
        return aramaMetni;
    }

    public void setAramaMetni(String aramaMetni) {
        this.aramaMetni = aramaMetni;
    }

    public boolean isAramaYapildiMi() {
        return aramaMetniVarMi();
    }

    public int getToplamSayfa() {
        return toplamSayfa;
    }

    public Long getToplamHaber() {
        return toplamHaber;
    }

    public List<Haber> getHaberListesi() {
        if (haberListesi == null) {
            haberleriYukle();
        }
        return haberListesi;
    }

    public List<Integer> getSayfalar() {
        if (sayfalar == null) {
            sayfalariHazirla();
        }
        return sayfalar;
    }

    private boolean anaSayfaMi() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext == null || facesContext.getViewRoot() == null) {
            return false;
        }

        String gorunumId = facesContext.getViewRoot().getViewId();
        return "/index.xhtml".equals(gorunumId);
    }

    private boolean adminHaberListeSayfasiMi() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext == null || facesContext.getViewRoot() == null) {
            return false;
        }

        String gorunumId = facesContext.getViewRoot().getViewId();
        return "/panel/admin-haber-liste.xhtml".equals(gorunumId);
    }

}
