package facadeLocal;

import entity.Haber;
import entity.Kullanici;
import entity.Yorum;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface YorumFacadeLocal {

    void ekle(Yorum yorum);

    Yorum guncelle(Yorum yorum);

    void sil(Yorum yorum);

    Yorum bul(Long id);

    List<Yorum> tumYorumlariGetir();

    List<Yorum> habereGoreYorumlariGetir(Haber haber);

    List<Yorum> kullaniciyaGoreYorumlariGetir(Kullanici kullanici);

    void habereAitYorumlariSil(Haber haber);

    long kullaniciYorumSayisi(Kullanici kullanici);

    void kullaniciYorumlariniSil(Kullanici kullanici);
}
