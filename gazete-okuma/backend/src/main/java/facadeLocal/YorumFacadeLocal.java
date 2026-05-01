package facadeLocal;

import entity.Haber;
import entity.Yorum;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface YorumFacadeLocal {

    void ekle(Yorum yorum);

    void sil(Yorum yorum);

    Yorum bul(Long id);

    List<Yorum> tumYorumlariGetir();

    List<Yorum> habereGoreYorumlariGetir(Haber haber);

    void habereAitYorumlariSil(Haber haber);
}
