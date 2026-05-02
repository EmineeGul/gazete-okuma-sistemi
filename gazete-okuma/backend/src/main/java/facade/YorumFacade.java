package facade;

import entity.Haber;
import entity.Kullanici;
import entity.Yorum;
import facadeLocal.YorumFacadeLocal;
import jakarta.ejb.Stateless;
import java.util.Collections;
import java.util.List;

@Stateless
public class YorumFacade extends AbstractFacade<Yorum> implements YorumFacadeLocal {

    public YorumFacade() {
        super(Yorum.class);
    }

    @Override
    public void ekle(Yorum yorum) {
        entityManager.persist(yorum);
        entityManager.flush();
    }

    @Override
    public Yorum guncelle(Yorum yorum) {
        Yorum guncellenen = entityManager.merge(yorum);
        entityManager.flush();
        entityManager.clear();
        return guncellenen;
    }

    @Override
    public void sil(Yorum yorum) {
        Yorum silinecek = entityManager.merge(yorum);
        entityManager.remove(silinecek);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public Yorum bul(Long id) {
        return entityManager.find(Yorum.class, id);
    }

    @Override
    public List<Yorum> tumYorumlariGetir() {
        entityManager.clear();
        return entityManager.createQuery(
                "SELECT y FROM Yorum y ORDER BY y.yorumTarihi DESC",
                Yorum.class
        ).getResultList();
    }

    @Override
    public List<Yorum> habereGoreYorumlariGetir(Haber haber) {
        if (haber == null) {
            return Collections.emptyList();
        }

        entityManager.clear();
        return entityManager.createQuery(
                "SELECT y FROM Yorum y WHERE y.haber = :haber ORDER BY y.yorumTarihi DESC",
                Yorum.class
        ).setParameter("haber", haber).getResultList();
    }

    @Override
    public List<Yorum> kullaniciyaGoreYorumlariGetir(Kullanici kullanici) {
        if (kullanici == null) {
            return Collections.emptyList();
        }

        entityManager.clear();
        return entityManager.createQuery(
                "SELECT y FROM Yorum y WHERE y.kullanici = :kullanici ORDER BY y.yorumTarihi DESC",
                Yorum.class
        ).setParameter("kullanici", kullanici).getResultList();
    }

    @Override
    public void habereAitYorumlariSil(Haber haber) {
        if (haber == null || haber.getId() == null) {
            return;
        }

        entityManager.createQuery("DELETE FROM Yorum y WHERE y.haber = :haber")
                .setParameter("haber", haber)
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public long kullaniciYorumSayisi(Kullanici kullanici) {
        if (kullanici == null) {
            return 0L;
        }

        return entityManager.createQuery(
                "SELECT COUNT(y) FROM Yorum y WHERE y.kullanici = :kullanici",
                Long.class
        ).setParameter("kullanici", kullanici).getSingleResult();
    }

    @Override
    public void kullaniciYorumlariniSil(Kullanici kullanici) {
        if (kullanici == null || kullanici.getId() == null) {
            return;
        }

        List<Yorum> yorumlar = entityManager.createQuery(
                "SELECT y FROM Yorum y WHERE y.kullanici = :kullanici",
                Yorum.class
        ).setParameter("kullanici", kullanici).getResultList();

        for (Yorum yorum : yorumlar) {
            entityManager.remove(entityManager.merge(yorum));
        }

        entityManager.flush();
        entityManager.clear();
    }
}
