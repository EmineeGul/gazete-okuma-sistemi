package facade;

import entity.FavoriHaber;
import entity.Haber;
import entity.Kullanici;
import facadeLocal.FavoriHaberFacadeLocal;
import jakarta.ejb.Stateless;
import java.util.Collections;
import java.util.List;

@Stateless
public class FavoriHaberFacade extends AbstractFacade<FavoriHaber> implements FavoriHaberFacadeLocal {

    public FavoriHaberFacade() {
        super(FavoriHaber.class);
    }

    public List<FavoriHaber> kullanicininFavorileriniGetir(Kullanici kullanici) {
        if (kullanici == null) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<FavoriHaber> favoriler = entityManager
                .createQuery("SELECT f FROM FavoriHaber f JOIN FETCH f.haber h WHERE f.kullanici = :kullanici ORDER BY h.yayinTarihi DESC")
                .setParameter("kullanici", kullanici)
                .getResultList();
        return favoriler == null ? Collections.emptyList() : favoriler;
    }

    public boolean favoriVarMi(Kullanici kullanici, Haber haber) {
        if (kullanici == null || haber == null) {
            return false;
        }

        @SuppressWarnings("unchecked")
        List<FavoriHaber> favoriler = entityManager
                .createQuery("SELECT f FROM FavoriHaber f WHERE f.kullanici = :kullanici AND f.haber = :haber")
                .setParameter("kullanici", kullanici)
                .setParameter("haber", haber)
                .setMaxResults(1)
                .getResultList();
        return !favoriler.isEmpty();
    }

    public FavoriHaber kullanicininFavorisiniBul(Kullanici kullanici, Haber haber) {
        if (kullanici == null || haber == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        List<FavoriHaber> sonuc = entityManager
                .createQuery("SELECT f FROM FavoriHaber f WHERE f.kullanici = :kullanici AND f.haber = :haber")
                .setParameter("kullanici", kullanici)
                .setParameter("haber", haber)
                .setMaxResults(1)
                .getResultList();
        return sonuc.isEmpty() ? null : sonuc.get(0);
    }

    @Override
    public void habereAitFavorileriSil(Haber haber) {
        if (haber == null || haber.getId() == null) {
            return;
        }

        entityManager.createQuery("DELETE FROM FavoriHaber f WHERE f.haber = :haber")
                .setParameter("haber", haber)
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public long kullaniciFavoriSayisi(Kullanici kullanici) {
        if (kullanici == null) {
            return 0L;
        }

        return entityManager.createQuery(
                "SELECT COUNT(f) FROM FavoriHaber f WHERE f.kullanici = :kullanici",
                Long.class
        ).setParameter("kullanici", kullanici).getSingleResult();
    }

    @Override
    public void kullaniciFavorileriniSil(Kullanici kullanici) {
        if (kullanici == null || kullanici.getId() == null) {
            return;
        }

        List<FavoriHaber> favoriler = entityManager.createQuery(
                "SELECT f FROM FavoriHaber f WHERE f.kullanici = :kullanici",
                FavoriHaber.class
        ).setParameter("kullanici", kullanici).getResultList();

        for (FavoriHaber favori : favoriler) {
            entityManager.remove(entityManager.merge(favori));
        }

        entityManager.flush();
        entityManager.clear();
    }
}
