package com.example.gazete.model.facade;

import com.example.gazete.model.entity.GazeteKaynagi;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class GazeteKaynagiFacade extends AbstractFacade<GazeteKaynagi> {

    @PersistenceContext(unitName = "GazeteHaberPU")
    private EntityManager varlikYoneticisi;

    public GazeteKaynagiFacade() {
        super(GazeteKaynagi.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return varlikYoneticisi;
    }

    public GazeteKaynagi kaynakAdinaGoreBul(String kaynakAdi) {
        try {
            TypedQuery<GazeteKaynagi> sorgu = varlikYoneticisi.createQuery(
                    "SELECT g FROM GazeteKaynagi g WHERE g.ad = :kaynakAdi", GazeteKaynagi.class);
            sorgu.setParameter("kaynakAdi", kaynakAdi);
            return sorgu.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
