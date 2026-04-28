package com.example.gazete.model.facade;

import com.example.gazete.model.entity.Kullanici;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class KullaniciFacade extends AbstractFacade<Kullanici> {

    @PersistenceContext(unitName = "GazeteHaberPU")
    private EntityManager varlikYoneticisi;

    public KullaniciFacade() {
        super(Kullanici.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return varlikYoneticisi;
    }

    public Kullanici kullaniciAdinaGoreBul(String kullaniciAdi) {
        return tekilKullaniciBul(
                "SELECT k FROM Kullanici k WHERE k.kullaniciAdi = :kullaniciAdi",
                "kullaniciAdi",
                kullaniciAdi);
    }

    public Kullanici epostayaGoreBul(String eposta) {
        return tekilKullaniciBul(
                "SELECT k FROM Kullanici k WHERE k.eposta = :eposta",
                "eposta",
                eposta);
    }

    private Kullanici tekilKullaniciBul(String sorguMetni, String parametreAdi, String parametreDegeri) {
        try {
            TypedQuery<Kullanici> sorgu = varlikYoneticisi.createQuery(sorguMetni, Kullanici.class);
            sorgu.setParameter(parametreAdi, parametreDegeri);
            return sorgu.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
