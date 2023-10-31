import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _01_ApiTest {
    @Test
    public void test1() {
        given()
                // giden body, token, contentType(JSON metin) burada belirleniyor.

                .when()
                // Metod (POSTMAN: GET, POST, UPDATE, DELETE gibi)
                // ve endpoint (url) yi verip istek gönderme yapılan bölüm

                .then()
                // POSTMAN, Response kısmındaki "Test Results" bölümü ile ilgili assertion, test, data işlemleri
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // Response da "Body" kısmındaki datayı json tipinde console yazdırıyor, .log().all() // HTTP protokolünde gelen tüm datayı yazdırıyor.
                .statusCode(200) // POSTMAN, Response kısmındaki "Test Results" sonucuna bakıyor.
        ;
    }

    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // Response da "Body" kısmındaki datayı json tipinde console yazdırıyor, .log().all() // HTTP protokolünde gelen tüm datayı gösteriyor.
                .statusCode(200) // POSTMAN, Response kısmındaki "Test Results" sonucuna bakıyor.
                .contentType(ContentType.JSON) // Response daki "Body" kısmında gelen datanın tipini assert ediyor (JSON mı diye).
        ;
    }

    @Test
    public void checkCountryInResponseBody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // Json tipinde data yazdırıyor
                .statusCode(200) // status code un assertion ı
                .body("country", equalTo("United States")) // country nin assertion ı
        ;
    }

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint de dönen
    // places dizisinin ilk elemanının state değerinin  "California"
    // olduğunu doğrulayınız.
    @Test
    public void checkPlacesStateInResponseBody() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California")) // places in state nin assertion ı [0] demeseydik tüm diziye bakardı.
        ;
    }

    // Soru : "http://api.zippopotam.us/tr/01000"  endpoint de dönen
    // places dizisinin herhangi bir elemanında "Dörtağaç Köyü" değerinin
    // olup/olmadığını doğrulayınız.
    @Test
    public void checkhasItem() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü")) // places in place name nin assertion ı
        ;
    }

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint inde dönen
    // places dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.
    @Test
    public void checkhasSize() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1)) // places in size nin assertion ı
        ;
    }

    // 2. Yöntem
    @Test
    public void bodyArrayHasSizeTest2() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places.size()", equalTo(1)) // places ın item size 1 e eşit mi?
        ;
    }

    @Test
    public void combiningTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"));
    }

    @Test
    public void pathParamTest() {
        // pathParam tipleri:
        // https://gorest.co.in/public/v2/users/628354
        // http://api.zippopotam.us/us/90210

        given()
                .pathParam("ulke", "us") // aşağıdaki link e /us/90210 olarak ekliyor
                .pathParam("postaKod", "90210") // Dışarıdan veri almamız gerektiği durumda bu şekilde kullanılabilir.
                .log().uri() // request in gönderilmeden önceki durumu

                .when()
                // {ulke} ve {postaKod} değerlerine parametre olarak .pathParam dan değerler geliyor.
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200);
    }

    @Test
    public void queryParamTest() {
        // queryParam tipleri:
        // https://sonuc.osym.gov.tr/Sorgu.aspx?SonucID=9750
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .queryParam("page", 3) // aşağıdaki link e ?page=3 olarak ekliyor // queryParam yerine param da kullanılabilir
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(200)
                .log().body();
    }

    // https://gorest.co.in/public/v1/users?page=3
    // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
    // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=1

        for (int i = 1; i <= 10; i++) {
            given()
                    .queryParam("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i));
            // Aslında bu tek komut dolayısıyla bu komutu 10 kere fori ile döndürüyoruz.
            // given().queryParam("page", i).log().uri().when().get("https://gorest.co.in/public/v1/users").then().statusCode(200).body("meta.pagination.page", equalTo(i));
        }
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setup() {
        baseURI = "https://gorest.co.in/public/v1";

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void requestResponseSpecification() {
        // https://gorest.co.in/public/v1/users?page=1

        given()

                .queryParam("page", 1)
                .spec(requestSpecification)

                .when()
                .get("/users")

                .then()
                .spec(responseSpecification);
    }
}
