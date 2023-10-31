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
        // Response ile gelen "body" nin içindeki
        // country deðiþkeninin yolunu alýp
        // String tipindeki countryName deðiþkenine eþitleyerek
        // veriyi dýþarý almýþ oldum.

        System.out.println("countryName = " + countryName); // dýþarýya aldýðým veriyi kontrol amaçlý yazdýrdým.
        Assert.assertEquals(countryName,"United States");// dýþarýya aldýðým veriyi TestNG ile assert ettim.
    }

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint inden dönen
    // places dizisinin ilk elemanýnýn state deðerinin "California"
    // olduðunu TestNG Assertion ile doðrulayýnýz
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

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
    // places dizisinin ilk elemanýnýn place name deðerinin  "Beverly Hills"
    // olduðunu testNG Assertion ile doðrulayýnýz
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

    // Soru : "https://gorest.co.in/public/v1/users"  endpoint inden dönen
    // limit bilgisinin 10 olduðunu TestNG ile doðrulayýnýz.
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
    public void extractingJsonPath5() { // List oluþturma
        List <Integer> ids =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body() // tüm listeyi alýp https://jsonpathfinder.com/ dan id lerin path'ini bulup sonraki komutta kullanabiliriz.
                        .extract().path("data.id") // data[0].id gibi bir index kullanmadýðýmýz için tüm data.id leri listeye atýyor.
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
        Response body = // Response dan dönen tüm body'i almýþ olduk. Böylece gelen datayý istediðimiz gibi kullanabiliriz.
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