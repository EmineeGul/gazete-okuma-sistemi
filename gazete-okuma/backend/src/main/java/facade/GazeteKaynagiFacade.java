package facade;

import entity.GazeteKaynagi;
import facadeLocal.GazeteKaynagiFacadeLocal;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class GazeteKaynagiFacade extends AbstractFacade<GazeteKaynagi> implements GazeteKaynagiFacadeLocal {

    public GazeteKaynagiFacade() {
        super(GazeteKaynagi.class);
    }

    public GazeteKaynagi kaynakAdiIleBul(String kaynakAdi) {
        @SuppressWarnings("unchecked")
        List<GazeteKaynagi> kaynaklar = entityManager
                .createQuery("SELECT g FROM GazeteKaynagi g WHERE g.ad = :kaynakAdi")
                .setParameter("kaynakAdi", kaynakAdi)
                .setMaxResults(1)
                .getResultList();
        return kaynaklar.isEmpty() ? null : kaynaklar.get(0);
    }
}
