import Model.Data;
import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class _05_PathAndJsonPath {

    @Test
    public void extractPath() {

        String postCode = // deðiþkeni int tipinde tanýmladýðýmýzda hata alýyoruz.
                // Çünkü tipi String olmalý.
                // Bunun için aþaðýdaki extractJsonPath testindeki dönüþümü uyguluyoruz.
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("'post code'");
        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractJsonPath() {

        int postCode =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'")
                // postCode deðiþkeni String tipinde olmasýna raðmen
                // int tipinde tanýmladýðýmýzda hata almamak için jsonPath() ile dönüþüm uyguluyoruz.
                ;
        System.out.println("postCode = " + postCode);
    }

    @Test
    public void getZipCode() {

        Response response =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().response();

        Location locationPathAs = response.as(Location.class); // Bu þekilde bütün class larý tek tek yazmak zorundasýn
        System.out.println("locationPathAs.getPlaces() = " + locationPathAs.getPlaces());

        List<Place> places = response.jsonPath().getList("places", Place.class); // Bu þekilde ise nokta atýþý istediðimiz nesneyi aldýk
        System.out.println("places = " + places);

        // Daha önceki örneklerde (as) Class dönüþümleri için tüm yapýya karþýlýk gelen
        // gerekli tüm classlarý yazarak dönüþtürüp istediðimiz elemanlara ulaþýyorduk.

        // Burada ise almak istediðimiz veriyi class a dönüþtürerek bir list olarak almamýza imkan veren JSONPATH i kullandýk.
        // Böylece diðer class lara gerek kalmadan tek class ile veri alýnmýþ oldu

        // path() : class veya tip dönüþümüne imkan vermez, direk veriyi verir. ör: List<String> gibi
        // jsonPath() : class dönüþümüne ve tip dönüþümüne izin vererek, veriyi istediðimiz formatta verir.
    }

    // Soru :
    // https://gorest.co.in/public/v1/users  endpointte dönen sadece Data Kýsmýný POJO
    // dönüþümü ile alarak yazdýrýnýz.

    @Test
    public void getData1() {

        Response response =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response();

        List<Data> data = response.jsonPath().getList("data", Data.class);
        // System.out.println("data = " + data);
        for (int i = 0; i < data.size(); i++) {
            System.out.println("data " + i + "= " + data.get(i));
        }
    }

    @Test
    public void getData2() {

        List<Data> data =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().jsonPath().getList("data", Data.class);

        // System.out.println("data = " + data);
        for (Data d : data){
            System.out.println("data = " + d);
        }
    }
}
