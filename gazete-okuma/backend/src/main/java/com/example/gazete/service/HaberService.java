package com.example.gazete.service;

import com.example.gazete.model.entity.Haber;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class HaberService {

    private final List<Haber> haberler = new ArrayList<>();

    @PostConstruct
    public void init() {
        Haber ilkHaber = new Haber(
                "Yerel Gazete Uygulamasi Yayinda",
                "Yeni gazete okuma sistemi Jakarta EE 10 ile hazir.",
                "Bu sistem, JSF ve JPA kullanarak haber akis saglar.",
                LocalDateTime.now().minusHours(1));
        ilkHaber.setId(1L);

        Haber ikinciHaber = new Haber(
                "Ekonomi Haberleri Guncellendi",
                "Piyasa verileri ve ekonomi gelismeleri burada.",
                "Guncel doviz kurlari, borsa analizi ve ekonomi haberleri.",
                LocalDateTime.now().minusHours(3));
        ikinciHaber.setId(2L);

        haberler.add(ilkHaber);
        haberler.add(ikinciHaber);
    }

    public List<Haber> tumunuBul() {
        return Collections.unmodifiableList(haberler);
    }

    public Optional<Haber> idIleBul(Long id) {
        return haberler.stream().filter(haber -> haber.getId() != null && haber.getId().equals(id)).findFirst();
    }
}
