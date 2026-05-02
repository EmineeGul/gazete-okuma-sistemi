package bean;

import entity.FavoriHaber;
import entity.Kullanici;
import entity.Yorum;
import enums.RoleEnum;
import facadeLocal.FavoriHaberFacadeLocal;
import facadeLocal.KullaniciFacadeLocal;
import facadeLocal.YorumFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Named("profilBean")
@ViewScoped
public class ProfilBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Pattern SIFRE_DESENI =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,}$");

    @EJB
    private FavoriHaberFacadeLocal favoriHaberFacade;

    @EJB
    private YorumFacadeLocal yorumFacade;

    @EJB
    private KullaniciFacadeLocal kullaniciFacade;

    private Kullanici kullanici;
    private List<FavoriHaber> favoriHaberler;
    private List<Yorum> yorumlar;
    private String ad;
    private String soyad;
    private String kullaniciAdi;
    private String mevcutSifre;
    private String yeniSifre;
    private String yeniSifreTekrar;
    private Long duzenlenenYorumId;
    private String duzenlenenYorumIcerik;

    @PostConstruct
    public void init() {
        favoriHaberler = Collections.emptyList();
        yorumlar = Collections.emptyList();

        kullanici = sessionKullanicisiniGetir();
        if (kullanici == null) {
            yonlendir("/giris.xhtml");
            return;
        }

        if (kullanici.getRole() != null && kullanici.getRole() == RoleEnum.ADMIN) {
            yonlendir("/panel/index.xhtml");
            return;
        }

        formAlanlariniDoldur();
        profilVerileriniYukle();
    }

    public void profilVerileriniYukle() {
        if (kullanici == null || kullanici.getId() == null) {
            favoriHaberler = Collections.emptyList();
            yorumlar = Collections.emptyList();
            return;
        }

        Kullanici guncelKullanici = kullaniciFacade.idIleBul(kullanici.getId());
        if (guncelKullanici != null) {
            kullanici = guncelKullanici;
            sessionKullanicisiniGuncelle(guncelKullanici);
            formAlanlariniDoldur();
        }

        favoriHaberler = guvenliFavoriListesi(favoriHaberFacade.kullanicininFavorileriniGetir(kullanici));
        yorumlar = guvenliYorumListesi(yorumFacade.kullaniciyaGoreYorumlariGetir(kullanici));
    }

    public String bilgileriGuncelle() {
        if (kullanici == null || kullanici.getId() == null) {
            yonlendir("/giris.xhtml");
            return null;
        }

        if (bosMu(ad) || bosMu(soyad) || bosMu(kullaniciAdi)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Ad, soyad ve kullanici adi bos birakilamaz.");
            return null;
        }

        String temizKullaniciAdi = kullaniciAdi.trim();
        Kullanici ayniAdaSahipKullanici = kullaniciFacade.kullaniciAdiIleBul(temizKullaniciAdi);

        if (ayniAdaSahipKullanici != null && !ayniAdaSahipKullanici.getId().equals(kullanici.getId())) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu kullanici adi baska bir hesap tarafindan kullaniliyor.");
            return null;
        }

        try {
            kullanici.setAd(ad.trim());
            kullanici.setSoyad(soyad.trim());
            kullanici.setKullaniciAdi(temizKullaniciAdi);
            kullanici = kullaniciFacade.guncelle(kullanici);
            sessionKullanicisiniGuncelle(kullanici);
            formAlanlariniDoldur();
            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Profil bilgileriniz guncellendi.");
            return null;
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Profil bilgileri guncellenemedi.");
            return null;
        }
    }

    public String sifreDegistir() {
        if (kullanici == null || kullanici.getId() == null) {
            yonlendir("/giris.xhtml");
            return null;
        }

        if (bosMu(mevcutSifre) || bosMu(yeniSifre) || bosMu(yeniSifreTekrar)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Sifre alanlarinin tamami doldurulmalidir.");
            return null;
        }

        if (!mevcutSifre.equals(kullanici.getSifre())) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Mevcut sifre dogru degil.");
            return null;
        }

        if (!yeniSifre.equals(yeniSifreTekrar)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yeni sifre ile tekrar ayni olmalidir.");
            return null;
        }

        if (mevcutSifre.equals(yeniSifre)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yeni sifre eski sifreyle ayni olamaz.");
            return null;
        }

        if (!sifreKurallariGecerliMi(yeniSifre)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata",
                    "Yeni sifre en az 8 karakter olmali, buyuk harf, kucuk harf ve ozel karakter icermelidir.");
            return null;
        }

        try {
            kullanici.setSifre(yeniSifre);
            kullanici = kullaniciFacade.guncelle(kullanici);
            sessionKullanicisiniGuncelle(kullanici);
            mevcutSifre = null;
            yeniSifre = null;
            yeniSifreTekrar = null;
            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Sifre basariyla guncellendi.");
            return null;
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Sifre guncellenemedi.");
            return null;
        }
    }

    public boolean sifreKurallariGecerliMi(String sifre) {
        return sifre != null && SIFRE_DESENI.matcher(sifre).matches();
    }

    public String yorumuDuzenlemeyeBasla(Yorum yorum) {
        if (!yorumaIslemYapilabilirMi(yorum)) {
            return null;
        }

        duzenlenenYorumId = yorum.getId();
        duzenlenenYorumIcerik = yorum.getIcerik();
        return null;
    }

    public String yorumDuzenlemeIptal() {
        duzenlenenYorumId = null;
        duzenlenenYorumIcerik = null;
        return null;
    }

    public String yorumuGuncelle() {
        if (duzenlenenYorumId == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Guncellenecek yorum bulunamadi.");
            return null;
        }

        if (kullanici == null || kullanici.getId() == null) {
            yonlendir("/giris.xhtml");
            return null;
        }

        Yorum yorum = yorumFacade.bul(duzenlenenYorumId);
        if (yorum == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum bulunamadi.");
            yorumDuzenlemeIptal();
            profilVerileriniYukle();
            return null;
        }

        if (!yorumSahibiAyniKullaniciMi(yorum)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu yorumu duzenleme yetkiniz yok.");
            yorumDuzenlemeIptal();
            return null;
        }

        if (bosMu(duzenlenenYorumIcerik)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum bos olamaz.");
            return null;
        }

        String temizIcerik = duzenlenenYorumIcerik.trim();
        if (temizIcerik.length() > 300) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum en fazla 300 karakter olabilir.");
            return null;
        }

        if (yasakliIcerikVarMi(temizIcerik)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum uygun olmayan icerik iceriyor.");
            return null;
        }

        try {
            yorum.setIcerik(temizIcerik);
            yorumFacade.guncelle(yorum);
            yorumDuzenlemeIptal();
            profilVerileriniYukle();
            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Yorum basariyla guncellendi.");
            return null;
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum guncellenemedi.");
            return null;
        }
    }

    public String yorumuSil(Yorum yorum) {
        if (kullanici == null || kullanici.getId() == null) {
            yonlendir("/giris.xhtml");
            return null;
        }

        if (yorum == null || yorum.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek yorum bulunamadi.");
            return null;
        }

        Yorum silinecekYorum = yorumFacade.bul(yorum.getId());
        if (silinecekYorum == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum bulunamadi.");
            profilVerileriniYukle();
            return null;
        }

        if (!yorumSahibiAyniKullaniciMi(silinecekYorum)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu yorumu silme yetkiniz yok.");
            return null;
        }

        try {
            yorumFacade.sil(silinecekYorum);
            if (duzenlenenYorumId != null && duzenlenenYorumId.equals(silinecekYorum.getId())) {
                yorumDuzenlemeIptal();
            }
            profilVerileriniYukle();
            mesajEkle(FacesMessage.SEVERITY_INFO, "Basarili", "Yorum basariyla silindi.");
            return null;
        } catch (Exception e) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum silinemedi.");
            return null;
        }
    }

    private void yonlendir(String hedefYol) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try {
            externalContext.redirect(externalContext.getRequestContextPath() + hedefYol);
            facesContext.responseComplete();
        } catch (IOException e) {
            throw new IllegalStateException("Yonlendirme yapilamadi: " + hedefYol, e);
        }
    }

    private Kullanici sessionKullanicisiniGetir() {
        Object user = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");
        return user instanceof Kullanici ? (Kullanici) user : null;
    }

    private void sessionKullanicisiniGuncelle(Kullanici guncelKullanici) {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("user", guncelKullanici);
    }

    private void formAlanlariniDoldur() {
        if (kullanici == null) {
            ad = null;
            soyad = null;
            kullaniciAdi = null;
            return;
        }

        ad = kullanici.getAd();
        soyad = kullanici.getSoyad();
        kullaniciAdi = kullanici.getKullaniciAdi();
    }

    private boolean bosMu(String deger) {
        return deger == null || deger.trim().isEmpty();
    }

    private boolean yorumaIslemYapilabilirMi(Yorum yorum) {
        if (kullanici == null || kullanici.getId() == null) {
            yonlendir("/giris.xhtml");
            return false;
        }

        if (yorum == null || yorum.getId() == null) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Yorum bulunamadi.");
            return false;
        }

        if (!yorumSahibiAyniKullaniciMi(yorum)) {
            mesajEkle(FacesMessage.SEVERITY_ERROR, "Hata", "Bu yoruma islem yapma yetkiniz yok.");
            return false;
        }

        return true;
    }

    private boolean yorumSahibiAyniKullaniciMi(Yorum yorum) {
        return yorum != null
                && yorum.getKullanici() != null
                && yorum.getKullanici().getId() != null
                && kullanici != null
                && kullanici.getId() != null
                && yorum.getKullanici().getId().equals(kullanici.getId());
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

    private void mesajEkle(FacesMessage.Severity seviye, String baslik, String detay) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(seviye, baslik, detay));
    }

    private List<FavoriHaber> guvenliFavoriListesi(List<FavoriHaber> kaynak) {
        return kaynak == null ? Collections.emptyList() : new ArrayList<>(kaynak);
    }

    private List<Yorum> guvenliYorumListesi(List<Yorum> kaynak) {
        return kaynak == null ? Collections.emptyList() : new ArrayList<>(kaynak);
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public List<FavoriHaber> getFavoriHaberler() {
        return favoriHaberler;
    }

    public List<Yorum> getYorumlar() {
        return yorumlar;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getMevcutSifre() {
        return mevcutSifre;
    }

    public void setMevcutSifre(String mevcutSifre) {
        this.mevcutSifre = mevcutSifre;
    }

    public String getYeniSifre() {
        return yeniSifre;
    }

    public void setYeniSifre(String yeniSifre) {
        this.yeniSifre = yeniSifre;
    }

    public String getYeniSifreTekrar() {
        return yeniSifreTekrar;
    }

    public void setYeniSifreTekrar(String yeniSifreTekrar) {
        this.yeniSifreTekrar = yeniSifreTekrar;
    }

    public Long getDuzenlenenYorumId() {
        return duzenlenenYorumId;
    }

    public void setDuzenlenenYorumId(Long duzenlenenYorumId) {
        this.duzenlenenYorumId = duzenlenenYorumId;
    }

    public String getDuzenlenenYorumIcerik() {
        return duzenlenenYorumIcerik;
    }

    public void setDuzenlenenYorumIcerik(String duzenlenenYorumIcerik) {
        this.duzenlenenYorumIcerik = duzenlenenYorumIcerik;
    }
}
