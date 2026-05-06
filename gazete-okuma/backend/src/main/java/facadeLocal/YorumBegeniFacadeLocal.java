package facadeLocal;

import entity.Kullanici;
import entity.Yorum;
import entity.YorumBegeni;
import jakarta.ejb.Local;

@Local
public interface YorumBegeniFacadeLocal {

    boolean yorumBegeniVarMi(Kullanici kullanici, Yorum yorum);

    void yorumuBegen(Kullanici kullanici, Yorum yorum);

    void yorumBegeniKaldir(Kullanici kullanici, Yorum yorum);

    long yorumBegeniSayisi(Yorum yorum);

    void yorumaAitBegenileriSil(Yorum yorum);
}
