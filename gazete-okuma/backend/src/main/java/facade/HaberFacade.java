package facade;

import entity.GazeteKaynagi;
import entity.Haber;
import entity.Kategori;
import facadeLocal.HaberFacadeLocal;
import jakarta.ejb.Stateless;
import java.util.Collections;
import java.util.List;

@Stateless
public class HaberFacade extends AbstractFacade<Haber> implements HaberFacadeLocal {

    public HaberFacade() {
        super(Haber.class);
    }

    @Override
    public void sil(Haber haber) {
        Haber silinecek = entityManager.merge(haber);
        entityManager.remove(silinecek);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public Haber guncelle(Haber haber) {
        System.out.println("MERGE CALISTI: " + haber.getId());
        Haber guncellenen = entityManager.merge(haber);
        entityManager.flush();
        entityManager.clear();
        return guncellenen;
    }

    public List<Haber> kategoriyeAitHaberleriGetir(Kategori kategori) {
        entityManager.clear();
        @SuppressWarnings("unchecked")
        List<Haber> haberler = entityManager
                .createQuery("SELECT h FROM Haber h WHERE h.kategori = :kategori ORDER BY h.yayinTarihi DESC")
                .setParameter("kategori", kategori)
                .getResultList();
        return sonucYoksaBosListeDon(haberler);
    }

    public List<Haber> kaynagaAitHaberleriGetir(GazeteKaynagi gazeteKaynagi) {
        entityManager.clear();
        @SuppressWarnings("unchecked")
        List<Haber> haberler = entityManager
                .createQuery("SELECT h FROM Haber h WHERE h.haberKaynagi = :gazeteKaynagi")
                .setParameter("gazeteKaynagi", gazeteKaynagi)
                .getResultList();
        return sonucYoksaBosListeDon(haberler);
    }

    @Override
    public List<Haber> kategoriVeKaynagaGoreHaberleriBul(Kategori kategori, GazeteKaynagi gazeteKaynagi) {
        entityManager.clear();
        @SuppressWarnings("unchecked")
        List<Haber> haberler = entityManager
                .createQuery("SELECT h FROM Haber h WHERE h.kategori = :kategori "
                        + "AND h.haberKaynagi = :gazeteKaynagi ORDER BY h.yayinTarihi DESC")
                .setParameter("kategori", kategori)
                .setParameter("gazeteKaynagi", gazeteKaynagi)
                .getResultList();
        return sonucYoksaBosListeDon(haberler);
    }

    public List<Haber> enSonHaberleriGetir(int limit) {
        int guvenliLimit = (limit > 0) ? limit : 10;
        entityManager.clear();
        @SuppressWarnings("unchecked")
        List<Haber> haberler = entityManager
                .createQuery("SELECT h FROM Haber h ORDER BY h.yayinTarihi DESC")
                .setMaxResults(guvenliLimit)
                .getResultList();
        return sonucYoksaBosListeDon(haberler);
    }

    public List<Haber> tumHaberleriGetir() {
        entityManager.clear();
        @SuppressWarnings("unchecked")
        List<Haber> haberler = entityManager
                .createQuery("SELECT h FROM Haber h ORDER BY h.yayinTarihi DESC")
                .getResultList();
        return sonucYoksaBosListeDon(haberler);
    }

    @Override
    public List<Haber> haberleriSayfaliGetir(int ilkKayit, int sayfaBoyutu) {
        entityManager.clear();
        List<Haber> haberler = entityManager.createQuery(
                "SELECT h FROM Haber h ORDER BY h.yayinTarihi DESC",
                Haber.class)
                .setFirstResult(ilkKayit)
                .setMaxResults(sayfaBoyutu)
                .getResultList();
        return sonucYoksaBosListeDon(haberler);
    }

    @Override
    public Long toplamHaberSayisi() {
        entityManager.clear();
        return entityManager.createQuery(
                "SELECT COUNT(h) FROM Haber h",
                Long.class)
                .getSingleResult();
    }

    private List<Haber> sonucYoksaBosListeDon(List<Haber> haberler) {
        return haberler == null ? Collections.emptyList() : haberler;
    }
}
