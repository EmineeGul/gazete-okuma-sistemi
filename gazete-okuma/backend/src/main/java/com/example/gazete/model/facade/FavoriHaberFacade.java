package com.example.gazete.model.facade;

import com.example.gazete.model.entity.FavoriHaber;
import com.example.gazete.model.entity.Haber;
import com.example.gazete.model.entity.Kullanici;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Stateless
public class FavoriHaberFacade extends AbstractFacade<FavoriHaber> {

    @PersistenceContext(unitName = "GazeteHaberPU")
    private EntityManager varlikYoneticisi;

    public FavoriHaberFacade() {
        super(FavoriHaber.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return varlikYoneticisi;
    }

    public List<FavoriHaber> kullaniciyaGoreFavorileriGetir(Kullanici kullanici) {
        if (kullanici == null) {
            return Collections.emptyList();
        }

        TypedQuery<FavoriHaber> sorgu = varlikYoneticisi.createQuery(
                "SELECT f FROM FavoriHaber f JOIN FETCH f.haber h WHERE f.kullanici = :kullanici ORDER BY h.yayinTarihi DESC",
                FavoriHaber.class);
        sorgu.setParameter("kullanici", kullanici);

        List<FavoriHaber> favoriler = sorgu.getResultList();
        return favoriler == null ? Collections.emptyList() : favoriler;
    }

    public boolean favoriVarMi(Kullanici kullanici, Haber haber) {
        if (kullanici == null || haber == null) {
            return false;
        }

        TypedQuery<Long> sorgu = varlikYoneticisi.createQuery(
                "SELECT COUNT(f) FROM FavoriHaber f WHERE f.kullanici = :kullanici AND f.haber = :haber",
                Long.class);
        sorgu.setParameter("kullanici", kullanici);
        sorgu.setParameter("haber", haber);

        Long favoriSayisi = sorgu.getSingleResult();
        return favoriSayisi != null && favoriSayisi > 0;
    }

    public FavoriHaber favoriyiBul(Kullanici kullanici, Haber haber) {
        if (kullanici == null || haber == null) {
            return null;
        }

        TypedQuery<FavoriHaber> sorgu = varlikYoneticisi.createQuery(
                "SELECT f FROM FavoriHaber f WHERE f.kullanici = :kullanici AND f.haber = :haber",
                FavoriHaber.class);
        sorgu.setParameter("kullanici", kullanici);
        sorgu.setParameter("haber", haber);
        sorgu.setMaxResults(1);

        List<FavoriHaber> sonuc = sorgu.getResultList();
        return sonuc.isEmpty() ? null : sonuc.get(0);
    }
}
