package bean;

import entity.Haber;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("gorselYoluBean")
@RequestScoped
public class GorselYoluBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_GORSEL = "/resources/images/default-news.jpg";

    public String gorselYoluGetir(Haber haber) {
        String contextPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestContextPath();

        if (haber == null || haber.getGorselUrl() == null || haber.getGorselUrl().isBlank()) {
            return contextPath + DEFAULT_GORSEL;
        }

        String gorsel = haber.getGorselUrl().trim();

        if (gorsel.startsWith("http://") || gorsel.startsWith("https://")) {
            return gorsel;
        }

        if (gorsel.startsWith(contextPath)) {
            return gorsel;
        }

        if (gorsel.startsWith("/resources/images/")) {
            return contextPath + gorsel;
        }

        if (gorsel.startsWith("resources/images/")) {
            return contextPath + "/" + gorsel;
        }

        if (gorsel.startsWith("/")) {
            return contextPath + gorsel;
        }

        return contextPath + "/resources/images/" + gorsel;
    }
}
