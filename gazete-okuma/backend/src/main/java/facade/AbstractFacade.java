package facade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractFacade<T> {

    @PersistenceContext(unitName = "GazeteHaberPU")
    protected EntityManager entityManager;

    private final Class<T> varlikSinifi;

    public AbstractFacade(Class<T> varlikSinifi) {
        this.varlikSinifi = varlikSinifi;
    }

    public void ekle(T varlik) {
        entityManager.persist(varlik);
    }

    public T guncelle(T varlik) {
        return entityManager.merge(varlik);
    }

    public void sil(T varlik) {
        entityManager.remove(entityManager.merge(varlik));
    }

    public T idIleBul(Object id) {
        return entityManager.find(varlikSinifi, id);
    }

    public List<T> tumunuGetir() {
        String sorguMetni = "SELECT kayit FROM " + varlikSinifAdi() + " kayit";
        @SuppressWarnings("unchecked")
        List<T> kayitlar = entityManager.createQuery(sorguMetni).getResultList();
        return kayitlar;
    }

    public List<T> araliktakileriGetir(int[] aralik) {
        String sorguMetni = "SELECT kayit FROM " + varlikSinifAdi() + " kayit";
        @SuppressWarnings("unchecked")
        List<T> kayitlar = entityManager.createQuery(sorguMetni)
                .setMaxResults(aralik[1] - aralik[0])
                .setFirstResult(aralik[0])
                .getResultList();
        return kayitlar;
    }

    public int kayitSayisiniGetir() {
        String sorguMetni = "SELECT COUNT(kayit) FROM " + varlikSinifAdi() + " kayit";
        List<?> sonucListesi = entityManager.createQuery(sorguMetni).getResultList();
        if (sonucListesi.isEmpty() || sonucListesi.get(0) == null) {
            return 0;
        }

        Number kayitSayisi = (Number) sonucListesi.get(0);
        return kayitSayisi.intValue();
    }

    private String varlikSinifAdi() {
        return varlikSinifi.getSimpleName();
    }
}
