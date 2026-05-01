package facade;

import entity.Kategori;
import facadeLocal.KategoriFacadeLocal;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class KategoriFacade extends AbstractFacade<Kategori> implements KategoriFacadeLocal {

    public KategoriFacade() {
        super(Kategori.class);
    }

    public Kategori kategoriAdiIleBul(String kategoriAdi) {
        @SuppressWarnings("unchecked")
        List<Kategori> kategoriler = entityManager
                .createQuery("SELECT k FROM Kategori k WHERE k.kategoriAdi = :kategoriAdi")
                .setParameter("kategoriAdi", kategoriAdi)
                .setMaxResults(1)
                .getResultList();
        return kategoriler.isEmpty() ? null : kategoriler.get(0);
    }
}
