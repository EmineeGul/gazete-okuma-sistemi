package facade;

import entity.Haber;
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
    public void sil(Yorum yorum) {
        Yorum silinecek = entityManager.merge(yorum);
        entityManager.remove(silinecek);
        entityManager.flush();
    }

    @Override
    public Yorum bul(Long id) {
        return entityManager.find(Yorum.class, id);
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
}
