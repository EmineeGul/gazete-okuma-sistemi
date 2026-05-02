package facadeLocal;

import entity.Kullanici;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface KullaniciFacadeLocal {

    void ekle(Kullanici varlik);

    Kullanici guncelle(Kullanici varlik);

    void sil(Kullanici varlik);

    Kullanici idIleBul(Object id);

    List<Kullanici> tumunuGetir();

    List<Kullanici> araliktakileriGetir(int[] aralik);

    int kayitSayisiniGetir();

    Kullanici kullaniciAdiIleBul(String kullaniciAdi);

    Kullanici epostaIleBul(String eposta);

    List<Kullanici> tumKullanicilariGetir();
}
