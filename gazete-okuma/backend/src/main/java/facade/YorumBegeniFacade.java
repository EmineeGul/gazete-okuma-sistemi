package facade;

import entity.Kullanici;
import entity.Yorum;
import entity.YorumBegeni;
import facadeLocal.YorumBegeniFacadeLocal;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class YorumBegeniFacade extends AbstractFacade<YorumBegeni> implements YorumBegeniFacadeLocal {

    public YorumBegeniFacade() {
        super(YorumBegeni.class);
    }

    @Override
    public boolean yorumBegeniVarMi(Kullanici kullanici, Yorum yorum) {
        if (kullanici == null || yorum == null || yorum.getId() == null) {
            return false;
        }

        try {
            Long begeniSayisi = entityManager.createQuery(
                    "SELECT COUNT(yb) FROM YorumBegeni yb WHERE yb.kullanici = :kullanici AND yb.yorum = :yorum",
                    Long.class
            ).setParameter("kullanici", kullanici)
                    .setParameter("yorum", yorum)
                    .getSingleResult();

            return begeniSayisi != null && begeniSayisi > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void yorumuBegen(Kullanici kullanici, Yorum yorum) {
        if (kullanici == null || yorum == null || yorum.getId() == null || yorumBegeniVarMi(kullanici, yorum)) {
            return;
        }

        try {
            YorumBegeni yorumBegeni = new YorumBegeni();
            yorumBegeni.setKullanici(kullanici);
            yorumBegeni.setYorum(yorum);

            entityManager.persist(yorumBegeni);
            entityManager.flush();
        } catch (Exception e) {
            entityManager.clear();
        }
    }

    @Override
    public void yorumBegeniKaldir(Kullanici kullanici, Yorum yorum) {
        if (kullanici == null || yorum == null || yorum.getId() == null) {
            return;
        }

        try {
            List<YorumBegeni> begeniler = entityManager.createQuery(
                    "SELECT yb FROM YorumBegeni yb WHERE yb.kullanici = :kullanici AND yb.yorum = :yorum",
                    YorumBegeni.class
            ).setParameter("kullanici", kullanici)
                    .setParameter("yorum", yorum)
                    .getResultList();

            for (YorumBegeni begeni : begeniler) {
                entityManager.remove(entityManager.merge(begeni));
            }

            entityManager.flush();
        } catch (Exception e) {
            entityManager.clear();
        }
    }

    @Override
    public long yorumBegeniSayisi(Yorum yorum) {
        if (yorum == null || yorum.getId() == null) {
            return 0L;
        }

        try {
            Long begeniSayisi = entityManager.createQuery(
                    "SELECT COUNT(yb) FROM YorumBegeni yb WHERE yb.yorum = :yorum",
                    Long.class
            ).setParameter("yorum", yorum).getSingleResult();

            return begeniSayisi == null ? 0L : begeniSayisi;
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public void yorumaAitBegenileriSil(Yorum yorum) {
        if (yorum == null || yorum.getId() == null) {
            return;
        }

        try {
            entityManager.createQuery("DELETE FROM YorumBegeni yb WHERE yb.yorum = :yorum")
                    .setParameter("yorum", yorum)
                    .executeUpdate();
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            entityManager.clear();
        }
    }
}
