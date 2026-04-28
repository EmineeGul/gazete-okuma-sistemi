package com.example.gazete.model.facade;

import com.example.gazete.model.entity.GazeteKaynagi;
import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.entity.Kategori;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Stateless
public class HaberFacade extends AbstractFacade<Haber> {

    @PersistenceContext(unitName = "GazeteHaberPU")
    private EntityManager varlikYoneticisi;

    public HaberFacade() {
        super(Haber.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return varlikYoneticisi;
    }

    public List<Haber> kategoriyeGoreHaberleriBul(Kategori kategori) {
        TypedQuery<Haber> sorgu = varlikYoneticisi.createQuery(
                "SELECT h FROM Haber h WHERE h.kategori = :kategori", Haber.class);
        sorgu.setParameter("kategori", kategori);
        return listeyiBosDon(sorgu);
    }

    public List<Haber> kaynagaGoreHaberleriBul(GazeteKaynagi gazeteKaynagi) {
        TypedQuery<Haber> sorgu = varlikYoneticisi.createQuery(
                "SELECT h FROM Haber h WHERE h.haberKaynagi = :gazeteKaynagi", Haber.class);
        sorgu.setParameter("gazeteKaynagi", gazeteKaynagi);
        return listeyiBosDon(sorgu);
    }

    public List<Haber> sonHaberleriGetir(int limit) {
        int guvenliLimit = (limit > 0) ? limit : 10;
        TypedQuery<Haber> sorgu = varlikYoneticisi.createQuery(
                "SELECT h FROM Haber h ORDER BY h.yayinTarihi DESC", Haber.class);
        sorgu.setMaxResults(guvenliLimit);
        return listeyiBosDon(sorgu);
    }

    private List<Haber> listeyiBosDon(TypedQuery<Haber> sorgu) {
        List<Haber> haberler = sorgu.getResultList();
        return haberler == null ? Collections.emptyList() : haberler;
    }
}
