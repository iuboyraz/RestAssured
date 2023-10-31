import Model.Location;
import Model.Place;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class _03_ApiPOJO {
    // POJO (Plain Old Java Object): JSON nesnesi demek
    // (JavaScript Object Notation = datan�n Server ile client aras� ta��nmas�nda kullan�lan protokol/format)
    // A�a��daki �rnekte location nesnesi POJO oluyor.

    @Test
    public void extractJsonAllPOJO() {
        Location location = // yani Ogrenci ogr1 = new Ogrenci() gibi
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().body().as(Location.class) // Location class �na g�re body i extract et
                ;

        System.out.println("location = " + location);

        System.out.println("location.getCountry() = " + location.getCountry());

        System.out.println("location.getPlaces() = " + location.getPlaces());

        for (Place p : location.getPlaces())
            System.out.println("p = " + p);

        // JsonSerialise(location);  developer bu �ekilde Location nesnesini Json'a d�n��t�rm��t�
        // bende tersine �evirdim yani JsonDeserialize yapt�m.
        // B�ylece NESNE yi elde ettim.
    }
    // Soru:
    // http://api.zippopotam.us/tr/01000  endpointinden d�nen verilerden
    // "D�rta�a� K�y�" i�eren datan�n t�m bilgilerini yazd�r�n�z.

    @Test
    public void extractJsonAllPOJOSoru() {
        Location location =
                given()

                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        .extract().body().as(Location.class);

        for (Place p : location.getPlaces()) {
            if (p.getPlacename().equalsIgnoreCase("D�rta�a� K�y�"))
            {System.out.println("p = " + p);}
        }
    }
}
