import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class _02_ApiTestExtract {
    @Test
    public void extractingJsonPath1() {
        String countryName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country")
                ;
        // Response ile gelen "body" nin i�indeki
        // country de�i�keninin yolunu al�p
        // String tipindeki countryName de�i�kenine e�itleyerek
        // veriyi d��ar� alm�� oldum.

        System.out.println("countryName = " + countryName); // d��ar�ya ald���m veriyi kontrol ama�l� yazd�rd�m.
        Assert.assertEquals(countryName,"United States");// d��ar�ya ald���m veriyi TestNG ile assert ettim.
    }

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint inden d�nen
    // places dizisinin ilk eleman�n�n state de�erinin "California"
    // oldu�unu TestNG Assertion ile do�rulay�n�z
    @Test
    public void extractingJsonPath2() {
        String stateName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].state")
                ;
        System.out.println("stateName = " + stateName);
        Assert.assertEquals(stateName,"California");
    }

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne d�nen
    // places dizisinin ilk eleman�n�n place name de�erinin  "Beverly Hills"
    // oldu�unu testNG Assertion ile do�rulay�n�z
    @Test
    public void extractingJsonPath3(){

        String placeName=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].'place name'")  // places[0]["place name"] bu da olur
                ;

        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName,"Beverly Hills");
    }

    // Soru : "https://gorest.co.in/public/v1/users"  endpoint inden d�nen
    // limit bilgisinin 10 oldu�unu TestNG ile do�rulay�n�z.
    @Test
    public void extractingJsonPath4() {
        int limit =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10); // Assert.assertTrue(limit==10);
    }

    @Test
    public void extractingJsonPath5() { // List olu�turma
        List <Integer> ids =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body() // t�m listeyi al�p https://jsonpathfinder.com/ dan id lerin path'ini bulup sonraki komutta kullanabiliriz.
                        .extract().path("data.id") // data[0].id gibi bir index kullanmad���m�z i�in t�m data.id leri listeye at�yor.
                ;
        System.out.println("idler = " + ids);
    }

    @Test
    public void extractingJsonPath6() {
        List <String> names =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.name")
                ;
        System.out.println("names = " + names);
    }

    @Test
    public void extractingJsonPathresponseAll() {
        Response body = // Response dan d�nen t�m body'i alm�� olduk. B�ylece gelen datay� istedi�imiz gibi kullanabiliriz.
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response()
                ;

        List<Integer> ids = body.path("data.id");
        List<String> names = body.path("data.name");
        int limit = body.path("meta.pagination.limit");

        System.out.println("ids = " + ids);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Mahesh Menon"));
        Assert.assertTrue(ids.contains(5599126));
        Assert.assertTrue(limit==10);
    }
}