package facade;

import entity.Kullanici;
import facadeLocal.KullaniciFacadeLocal;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class KullaniciFacade extends AbstractFacade<Kullanici> implements KullaniciFacadeLocal {

    public KullaniciFacade() {
        super(Kullanici.class);
    }

    public Kullanici kullaniciAdiIleBul(String kullaniciAdi) {
        return tekKullaniciGetir(
                "SELECT k FROM Kullanici k WHERE k.kullaniciAdi = :kullaniciAdi",
                "kullaniciAdi",
                kullaniciAdi);
    }

    public Kullanici epostaIleBul(String eposta) {
        return tekKullaniciGetir(
                "SELECT k FROM Kullanici k WHERE k.eposta = :eposta",
                "eposta",
                eposta);
    }

    @Override
    public List<Kullanici> tumKullanicilariGetir() {
        entityManager.clear();
        return entityManager.createQuery(
                "SELECT k FROM Kullanici k ORDER BY k.id DESC",
                Kullanici.class
        ).getResultList();
    }

    @Override
    public void sil(Kullanici kullanici) {
        Kullanici silinecek = entityManager.merge(kullanici);
        entityManager.remove(silinecek);
        entityManager.flush();
        entityManager.clear();
    }

    private Kullanici tekKullaniciGetir(String sorguMetni, String parametreAdi, String parametreDegeri) {
        @SuppressWarnings("unchecked")
        List<Kullanici> kullanicilar = entityManager.createQuery(sorguMetni)
                .setParameter(parametreAdi, parametreDegeri)
                .setMaxResults(1)
                .getResultList();
        return kullanicilar.isEmpty() ? null : kullanicilar.get(0);
    }
}
