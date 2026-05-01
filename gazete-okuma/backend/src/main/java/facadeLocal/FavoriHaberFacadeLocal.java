package facadeLocal;

import entity.FavoriHaber;
import entity.Haber;
import entity.Kullanici;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface FavoriHaberFacadeLocal {

    void ekle(FavoriHaber varlik);

    FavoriHaber guncelle(FavoriHaber varlik);

    void sil(FavoriHaber varlik);

    FavoriHaber idIleBul(Object id);

    List<FavoriHaber> tumunuGetir();

    List<FavoriHaber> araliktakileriGetir(int[] aralik);

    int kayitSayisiniGetir();

    List<FavoriHaber> kullanicininFavorileriniGetir(Kullanici kullanici);

    boolean favoriVarMi(Kullanici kullanici, Haber haber);

    FavoriHaber kullanicininFavorisiniBul(Kullanici kullanici, Haber haber);

    void habereAitFavorileriSil(Haber haber);
}
