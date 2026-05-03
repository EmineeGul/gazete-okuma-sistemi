# Gazete Okuma

Java tabanli bir haber okuma ve yonetim uygulamasi. Proje, kullanicilarin haberleri listeleyip detaylarini inceleyebildigi, kategori ve kaynaga gore filtreleme yapabildigi, favori listesi olusturabildigi ve yorum yazabildigi bir platform sunar. Bunun yaninda yoneticiler icin haber, yorum ve kullanici yonetimi yapilabilen bir admin paneli bulunur.

## Proje Ne Yapiyor?

Bu uygulama iki temel tarafi olan bir haber sistemi sunar:

- Ziyaretci ve uye tarafi: haber listeleme, haber detayi goruntuleme, kategori ve kaynak bazli filtreleme
- Uye tarafi: kayit olma, giris yapma, sifre yenileme, favorilere ekleme, yorum yapma, profil guncelleme
- Admin tarafi: ayri admin girisi, haber ekleme, haber duzenleme, haber silme, tum yorumlari yonetme, kullanici listeleme ve silme

## One Cikan Ozellikler

- Ana sayfada sayfalama destekli haber listesi
- Haber detay sayfasinda yorum akisi
- Kategori ve gazete kaynagina gore filtreleme
- Kullanici kaydi ve giris islemleri
- "Sifremi unuttum" akisi ile sifre yenileme
- Favori haber ekleme ve favoriler sayfasi
- Profil sayfasinda kullanici bilgisi guncelleme
- Profil sayfasinda sifre degistirme
- Profil sayfasinda kullanicinin kendi yorumlarini duzenleme ve silme
- Admin panelinde haber CRUD islemleri
- Admin panelinde tum yorumlari goruntuleme ve silme
- Admin panelinde standart kullanicilari listeleme ve silme
- Oturum kontrolu icin `SessionFilter`

## Kullanici Rolleri

Uygulamada iki rol vardir:

- `USER`: normal kullanici, haberleri okuyabilir, yorum yapabilir, favori kullanabilir, profilini yonetebilir
- `ADMIN`: yonetici, admin paneline erisip haber, yorum ve kullanici yonetimi yapabilir

Admin girisi ayri olarak `admin-giris.xhtml` uzerinden yapilir.

## Teknolojiler

- Java 17
- Jakarta EE 10
- JSF (`.xhtml` sayfalari)
- CDI
- EJB
- JPA / EclipseLink
- PostgreSQL JDBC Driver
- Maven
- Bootstrap

## Mimari Yapi

Proje `backend` modulu altinda WAR olarak paketlenir.

- `backend/src/main/java/entity`
  JPA entity siniflari (`Kullanici`, `Haber`, `Kategori`, `GazeteKaynagi`, `FavoriHaber`, `Yorum`)
- `backend/src/main/java/bean`
  JSF managed bean siniflari ve sayfa akis mantigi
- `backend/src/main/java/facade`
  Veritabani erisim katmani
- `backend/src/main/java/filter`
  Oturum ve yetki kontrolu
- `backend/src/main/resources/META-INF/persistence.xml`
  JPA persistence ayari
- `backend/src/main/webapp`
  JSF sayfalari, CSS, JS ve gorseller

## Ekranlar

### Genel Kullanici Akisi

- `index.xhtml`: ana sayfa, haber kartlari ve sayfalama
- `haber-detay.xhtml`: haber icerigi ve yorumlar
- `kategori-haberleri.xhtml`: kategori ve kaynak filtreleme
- `giris.xhtml`: kullanici girisi
- `kayit.xhtml`: yeni kullanici kaydi
- `sifremi-unuttum.xhtml`: sifre yenileme

### Uye Paneli

- `panel/favoriler.xhtml`: favori haberler
- `panel/profil.xhtml`: profil bilgileri, sifre guncelleme, yorum yonetimi

### Admin Paneli

- `admin-giris.xhtml`: admin girisi
- `panel/admin-haber-liste.xhtml`: haber listesi
- `panel/admin-haber-ekle.xhtml`: haber ekleme
- `panel/admin-haber-duzenle.xhtml`: haber guncelleme
- `panel/admin-yorum-liste.xhtml`: yorum yonetimi
- `panel/admin-kullanici-liste.xhtml`: kullanici yonetimi

## Veritabani Modeli

Persistence unit adi: `GazeteHaberPU`

Projede kullanilan ana tablolar:

- `kullanici`
- `haber`
- `kategori`
- `gazete_kaynagi`
- `favori_haber`
- `yorum`

`persistence.xml` dosyasinda JTA datasource olarak `jdbc/gazeteHaberDS` beklenir. Ayrica schema generation kapali durumda:

```xml
<jta-data-source>jdbc/gazeteHaberDS</jta-data-source>
<property name="jakarta.persistence.schema-generation.database.action" value="none"/>
```

Bu nedenle tablo olusturma ve ilk veri yukleme islemleri uygulama sunucusu disinda hazir olmalidir.

## Kurulum

### Gereksinimler

- Java 17
- Maven 3+
- PostgreSQL
- Jakarta EE 10 uyumlu uygulama sunucusu
  Ornek: Payara, GlassFish, WildFly

### 1. Projeyi Derleme

Proje kok dizininde:

```bash
cd gazete-okuma/backend
mvn clean package
```

Basarili derleme sonrasi `target/gazete-okuma.war` olusur.

### 2. Veritabanini Hazirlama

- PostgreSQL tarafinda uygulamanin kullanacagi veritabanini olusturun
- `kullanici`, `haber`, `kategori`, `gazete_kaynagi`, `favori_haber`, `yorum` tablolarini olusturun
- Gerekliyse kategori, haber kaynagi ve admin kullanici seed verilerini ekleyin

Not: Uygulamada public bir admin kayit ekrani yoktur. Admin kullanici veritabaninda `role = 'ADMIN'` olacak sekilde olusturulmalidir.

### 3. Datasource Tanimlama

Uygulama sunucusunda `jdbc/gazeteHaberDS` isminde bir JTA datasource tanimlayin ve PostgreSQL veritabanina baglayin.

### 4. WAR Dosyasini Deploy Etme

Olusan WAR dosyasini uygulama sunucusuna deploy edin.

## Calistirma Sonrasi Beklenen Akis

- Uygulama acilis sayfasi: `index.xhtml`
- Normal kullanici girisi: `/giris.xhtml`
- Admin girisi: `/admin-giris.xhtml`
- Oturum gerektiren alanlar: `/panel/*` ve `/app/*`

`SessionFilter`, giris yapmamis kullanicilari korumali sayfalardan giris ekranina yonlendirir.

## Is Kurallari ve Dogrulamalar

Projede dikkat ceken bazi kurallar:

- Kayit ve sifre degistirme akisinda guclu sifre deseni kontrolu var
- Yorumlar bos olamaz ve en fazla 300 karakter olabilir
- Belirli yasakli kelimeler iceren yorumlar engellenir
- Ayni kullanicinin ayni habere ayni yorumu tekrar eklemesi engellenir
- Adminler yorum yapamaz, ancak yorum yonetebilir
- Kullanici silinirken iliskili yorum ve favori kayitlari da temizlenir
- Haber silinirken ilgili yorum ve favoriler de temizlenir

## Projede Dikkat Edilebilecek Gelistirme Alanlari

Mevcut yapi calisir durumdaki bir haber otomasyonu sunuyor; ancak uretim ortami icin su alanlar daha da iyilestirilebilir:

- Sifreler duz metin yerine hashlenerek saklanmali
- Veritabani migration araci eklenmeli (`Flyway` veya `Liquibase`)
- Otomatik testler eklenmeli
- Docker ile gelistirme ortami hazirlanabilir
- Rol bazli yetkilendirme daha merkezi hale getirilebilir
- API katmani eklenerek mobil ya da SPA istemciler desteklenebilir

## Klasor Yapisi

```text
gazete-okuma/
  README.md
  backend/
    pom.xml
    src/
      main/
        java/
          bean/
          entity/
          facade/
          facadeLocal/
          filter/
          enums/
        resources/
          META-INF/
            beans.xml
            persistence.xml
        webapp/
          WEB-INF/
          panel/
          resources/
```

## Ozet

Bu proje, Jakarta EE ekosistemiyle gelistirilmis tam akisli bir haber okuma ve yonetim sistemidir. GitHub'da inceleyen biri icin hem kullanici tarafindaki haber deneyimini hem de admin tarafindaki icerik yonetimini tek projede gosteren guzel bir portfolyo calismasi niteligindedir.
