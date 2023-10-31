import Model.Location;
import Model.Place;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class _03_ApiPOJO {
    // POJO (Plain Old Java Object): JSON nesnesi demek
    // (JavaScript Object Notation = datanýn Server ile client arasý taþýnmasýnda kullanýlan protokol/format)
    // Aþaðýdaki örnekte location nesnesi POJO oluyor.

    @Test
    public void extractJsonAllPOJO() {
        Location location = // yani Ogrenci ogr1 = new Ogrenci() gibi
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().body().as(Location.class) // Location class ýna göre body i extract et
                ;

        System.out.println("location = " + location);

        System.out.println("location.getCountry() = " + location.getCountry());

        System.out.println("location.getPlaces() = " + location.getPlaces());

        for (Place p : location.getPlaces())
            System.out.println("p = " + p);

        // JsonSerialise(location);  developer bu þekilde Location nesnesini Json'a dönüþtürmüþtü
        // bende tersine çevirdim yani JsonDeserialize yaptým.
        // Böylece NESNE yi elde ettim.
    }
    // Soru:
    // http://api.zippopotam.us/tr/01000  endpointinden dönen verilerden
    // "Dörtaðaç Köyü" içeren datanýn tüm bilgilerini yazdýrýnýz.

    @Test
    public void extractJsonAllPOJOSoru() {
        Location location =
                given()

                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        .extract().body().as(Location.class);

        for (Place p : location.getPlaces()) {
            if (p.getPlacename().equalsIgnoreCase("Dörtaðaç Köyü"))
            {System.out.println("p = " + p);}
        }
    }
}
