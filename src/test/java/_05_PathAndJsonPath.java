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

        String postCode = // de�i�keni int tipinde tan�mlad���m�zda hata al�yoruz.
                // ��nk� tipi String olmal�.
                // Bunun i�in a�a��daki extractJsonPath testindeki d�n���m� uyguluyoruz.
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
                // postCode de�i�keni String tipinde olmas�na ra�men
                // int tipinde tan�mlad���m�zda hata almamak i�in jsonPath() ile d�n���m uyguluyoruz.
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

        Location locationPathAs = response.as(Location.class); // Bu �ekilde b�t�n class lar� tek tek yazmak zorundas�n
        System.out.println("locationPathAs.getPlaces() = " + locationPathAs.getPlaces());

        List<Place> places = response.jsonPath().getList("places", Place.class); // Bu �ekilde ise nokta at��� istedi�imiz nesneyi ald�k
        System.out.println("places = " + places);

        // Daha �nceki �rneklerde (as) Class d�n���mleri i�in t�m yap�ya kar��l�k gelen
        // gerekli t�m classlar� yazarak d�n��t�r�p istedi�imiz elemanlara ula��yorduk.

        // Burada ise almak istedi�imiz veriyi class a d�n��t�rerek bir list olarak almam�za imkan veren JSONPATH i kulland�k.
        // B�ylece di�er class lara gerek kalmadan tek class ile veri al�nm�� oldu

        // path() : class veya tip d�n���m�ne imkan vermez, direk veriyi verir. �r: List<String> gibi
        // jsonPath() : class d�n���m�ne ve tip d�n���m�ne izin vererek, veriyi istedi�imiz formatta verir.
    }

    // Soru :
    // https://gorest.co.in/public/v1/users  endpointte d�nen sadece Data K�sm�n� POJO
    // d�n���m� ile alarak yazd�r�n�z.

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
