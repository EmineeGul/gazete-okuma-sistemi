# Gazete Okuma ve Haber Takip Sistemi

Java 17, Jakarta EE 10, JSF, CDI, JPA ve Maven WAR paketleme kullanan temel proje iskeleti.

## Yapı
- `src/main/java` - uygulama kodu
- `src/main/resources/META-INF/persistence.xml` - JPA yapılandırması
- `src/main/webapp` - JSF sayfaları ve web kaynakları
- `src/main/webapp/WEB-INF/beans.xml` - CDI etkinleştirme
- `pom.xml` - Maven yapılandırması

## Çalıştırma
1. Maven ile derleyin: `mvn clean package`
2. WAR dosyasını Jakarta EE 10 uyumlu bir uygulama sunucusuna dağıtın.
