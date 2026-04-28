package com.example.gazete.model.facade;

import com.example.gazete.model.entity.Kategori;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class KategoriFacade extends AbstractFacade<Kategori> {

    @PersistenceContext(unitName = "GazeteHaberPU")
    private EntityManager varlikYoneticisi;

    public KategoriFacade() {
        super(Kategori.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return varlikYoneticisi;
    }

    public Kategori kategoriAdinaGoreBul(String kategoriAdi) {
        try {
            TypedQuery<Kategori> sorgu = varlikYoneticisi.createQuery(
                    "SELECT k FROM Kategori k WHERE k.kategoriAdi = :kategoriAdi", Kategori.class);
            sorgu.setParameter("kategoriAdi", kategoriAdi);
            return sorgu.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
