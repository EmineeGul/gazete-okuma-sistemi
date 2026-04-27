package com.example.gazete.controller;

import com.example.gazete.model.entity.Haber;
import com.example.gazete.service.HaberService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Named
@RequestScoped
public class HaberBean implements Serializable {

    @Inject
    private HaberService haberService;

    private Haber seciliHaber;

    public List<Haber> getHaberler() {
        return haberService.tumunuBul();
    }

    public Haber getSeciliHaber() {
        if (seciliHaber == null) {
            Map<String, String> parametreler = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String idParametresi = parametreler.get("id");
            if (idParametresi != null) {
                try {
                    Long id = Long.valueOf(idParametresi);
                    Optional<Haber> haberSecenegi = haberService.idIleBul(id);
                    if (haberSecenegi.isPresent()) {
                        seciliHaber = haberSecenegi.get();
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return seciliHaber;
    }
}
