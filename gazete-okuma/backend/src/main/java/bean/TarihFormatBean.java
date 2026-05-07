package bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Named("tarihFormatBean")
@ApplicationScoped
public class TarihFormatBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter TARIH_SAAT_FORMATI =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter TARIH_FORMATI =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String tarihSaatFormatla(LocalDateTime tarih) {
        if (tarih == null) {
            return "Belirtilmedi";
        }

        return tarih.format(TARIH_SAAT_FORMATI);
    }

    public String tarihFormatla(LocalDateTime tarih) {
        if (tarih == null) {
            return "Belirtilmedi";
        }

        return tarih.format(TARIH_FORMATI);
    }
}
