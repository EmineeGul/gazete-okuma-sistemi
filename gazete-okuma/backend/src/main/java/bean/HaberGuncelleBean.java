package bean;

import entity.Haber;
import facadeLocal.HaberFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Named("haberGuncelleBean")
@ViewScoped
public class HaberGuncelleBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORM_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @EJB
    private HaberFacadeLocal haberFacade;

    private Long haberId;
    private Haber haber;
    private String yayinTarihiText;

    public void haberiYukle() {
        if (!FacesContext.getCurrentInstance().isPostback() && haberId != null) {
            System.out.println("HABER YUKLE CALISTI, haberId=" + haberId);
            haber = haberFacade.idIleBul(haberId);

            if (haber == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Guncellenecek haber bulunamadi."));
            } else {
                System.out.println("HABER BULUNDU, mevcut baslik=" + haber.getBaslik());
                if (haber.getYayinTarihi() != null) {
                    yayinTarihiText = haber.getYayinTarihi().format(FORM_FORMATTER);
                }
            }
        }
    }

    public String haberGuncelle() {
        System.out.println("GUNCELLEME METODU CALISTI");
        if (haber == null || haber.getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Guncellenecek haber bulunamadi."));
            return null;
        }

        try {
            System.out.println("Haber ID: " + haber.getId());
            System.out.println("Yeni baslik: " + haber.getBaslik());
            if (yayinTarihiText != null && !yayinTarihiText.isBlank()) {
                haber.setYayinTarihi(LocalDateTime.parse(yayinTarihiText, FORM_FORMATTER));
            }
            haber = haberFacade.guncelle(haber);
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Basarili", "Haber basariyla guncellendi."));
            return "/panel/admin-haber-liste.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Haber guncellenemedi."));
            return null;
        }
    }

    public String guncelle() {
        return haberGuncelle();
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

    public String getYayinTarihiText() {
        return yayinTarihiText;
    }

    public void setYayinTarihiText(String yayinTarihiText) {
        this.yayinTarihiText = yayinTarihiText;
    }
}
