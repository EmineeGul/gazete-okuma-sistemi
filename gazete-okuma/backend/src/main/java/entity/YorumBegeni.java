package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(
        name = "yorum_begeni",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"kullanici_id", "yorum_id"})
        }
)
public class YorumBegeni implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @ManyToOne(optional = false)
    @JoinColumn(name = "yorum_id", nullable = false)
    private Yorum yorum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Yorum getYorum() {
        return yorum;
    }

    public void setYorum(Yorum yorum) {
        this.yorum = yorum;
    }
}
