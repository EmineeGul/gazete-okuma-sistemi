package facadeLocal;

import entity.GazeteKaynagi;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface GazeteKaynagiFacadeLocal {

    void ekle(GazeteKaynagi varlik);

    GazeteKaynagi guncelle(GazeteKaynagi varlik);

    void sil(GazeteKaynagi varlik);

    GazeteKaynagi idIleBul(Object id);

    List<GazeteKaynagi> tumunuGetir();

    List<GazeteKaynagi> araliktakileriGetir(int[] aralik);

    int kayitSayisiniGetir();

    GazeteKaynagi kaynakAdiIleBul(String kaynakAdi);
}
