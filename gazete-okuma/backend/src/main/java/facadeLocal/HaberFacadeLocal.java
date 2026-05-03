package facadeLocal;

import entity.GazeteKaynagi;
import entity.Haber;
import entity.Kategori;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface HaberFacadeLocal {

    void ekle(Haber varlik);

    Haber guncelle(Haber haber);

    void sil(Haber varlik);

    Haber idIleBul(Object id);

    List<Haber> tumunuGetir();

    List<Haber> araliktakileriGetir(int[] aralik);

    int kayitSayisiniGetir();

    List<Haber> kategoriyeAitHaberleriGetir(Kategori kategori);

    List<Haber> kaynagaAitHaberleriGetir(GazeteKaynagi gazeteKaynagi);

    List<Haber> kategoriVeKaynagaGoreHaberleriBul(Kategori kategori, GazeteKaynagi gazeteKaynagi);

    List<Haber> enSonHaberleriGetir(int limit);

    List<Haber> tumHaberleriGetir();

    List<Haber> haberleriSayfaliGetir(int ilkKayit, int sayfaBoyutu);

    Long toplamHaberSayisi();
}
