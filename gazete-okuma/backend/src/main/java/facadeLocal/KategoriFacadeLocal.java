package facadeLocal;

import entity.Kategori;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface KategoriFacadeLocal {

    void ekle(Kategori varlik);

    Kategori guncelle(Kategori varlik);

    void sil(Kategori varlik);

    Kategori idIleBul(Object id);

    List<Kategori> tumunuGetir();

    List<Kategori> araliktakileriGetir(int[] aralik);

    int kayitSayisiniGetir();

    Kategori kategoriAdiIleBul(String kategoriAdi);
}
