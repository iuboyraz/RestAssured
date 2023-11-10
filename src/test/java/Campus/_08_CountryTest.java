package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.*;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _08_CountryTest {
    Faker randomGenerator = new Faker();
    String countryID;
    // Campus te create edilen countrynin id si String ("id": "6543d5f62849c34e335b4a2e")
    // o yüzden String tanýmlandý. POSTMAN'a bakabilirsin.
    RequestSpecification requestSpec;
    String rndCountryName;
    String rndCountryCode;
    Map<String, String> newCountry;
    @BeforeClass
    public void setUp() {
        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
        given()
                .body(userCredential)
                .contentType(ContentType.JSON)

                .when()
                .post("/auth/login")

                .then()
                //.log().all() // @Test in içi boþ iken çalýþtýrýldýðýnda gelen response taki tüm log u alabiliriz.
                .statusCode(200)
                .extract().response().detailedCookies()
        ;

        requestSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                // her seferinde access_token'ý yazmamak için her seferinde cookies ile gidip-geldiði için spec içine yerleþtirdik.
                // POSTMAN da Headers Cookie ye bakabilirsin.
                .setContentType(ContentType.JSON)
                .build()
        ;
    }
    @Test
    public void createCountry(){

        rndCountryName = randomGenerator.address().country() + randomGenerator.address().countryCode();
        rndCountryCode = randomGenerator.address().countryCode();

        newCountry = new HashMap<>();
        newCountry.put("name", rndCountryName);
        newCountry.put("code", rndCountryCode);

        countryID =
                given()

                        .spec(requestSpec)
                        .body(newCountry)

                        .when()
                        .post("school-service/api/countries/")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("countryID = " + countryID);
    }

    // Ayný countryName ve code gönderildiðinde kayýt yapýlmadýðýna dair ilgili testi yazýnýz.
    // Yani createCountryNegative testini yapýnýz.
    // Response da "Please provide valid data to update Country" olduðunu doðrulayýnýz.
    @Test (dependsOnMethods = "createCountry")
    public void createCountryNegative(){

                given()

                        .spec(requestSpec)
                        .body(newCountry)

                        .when()
                        .put("school-service/api/countries/")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message", containsString("Please provide valid data to update Country"))

        ;
        System.out.println("countryID = " + countryID);
    }
    // Update Country testini yapýnýz ve response da update edilen ismi kontrol ediniz.
    @Test (dependsOnMethods = "createCountryNegative")
    public void updateCountry(){
        String newCountryName = "Ali Cabbar Country " + randomGenerator.number().digits(5);

        Map<String, String> updateCountry = new HashMap<>();
        updateCountry.put("id", countryID);
        updateCountry.put("name", newCountryName);
        updateCountry.put("code", "AC");

        given()

                .spec(requestSpec)
                .body(updateCountry)

                .when()
                .put("school-service/api/countries/")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newCountryName))

        ;
        System.out.println("countryID = " + countryID);
        System.out.println("newCountryName = " + newCountryName);
    }
    // Delete Country testini yapýnýz.
    @Test (dependsOnMethods = "updateCountry")
    public void deleteCountry(){

        given()
                .spec(requestSpec)

                .when()
                .delete("school-service/api/countries/" + countryID)

                .then()
                .log().body()
                .statusCode(200)

        ;
        System.out.println("countryID = " + countryID);
    }
    // Delete Country Negative testini yapýnýz. Response da "Country not found" olduðunu kontrol ediniz.
    @Test (dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative(){

        given()
                .spec(requestSpec)

                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(500) //Jenkins report ta hatayý görmek için 400 -> 500 yapýldý
                .body("message", containsString("Country not found"))

        ;
        System.out.println("countryID = " + countryID);
    }

    // translateName i de (Map ile) göndermek istersek
    // aþaðýdaki 2 testten (createCountryAllParameter / createCountryAllParameterClass)
    // birini kullanacaðýz.
    @Test
    public void createCountryAllParameter(){

        rndCountryName = randomGenerator.address().country() + randomGenerator.address().countryCode();
        rndCountryCode = randomGenerator.address().countryCode();

        // Object [] arr = new Object[1];
        Map<String, Object> newCountry = new HashMap<>();
        newCountry.put("name", rndCountryName);
        newCountry.put("code", rndCountryCode);
        newCountry.put("translateName", new Object [1]); // arr de kullanabiliriz.

        // countryID = // countryID yi etkilemesin diye kaldýrdýk
                given()

                        .spec(requestSpec)
                        .body(newCountry)

                        .when()
                        .post("school-service/api/countries/")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("countryID = " + countryID);
    }

    // translateName i class (county) kullanarak gönderdik
    @Test
    public void createCountryAllParameterClass(){

        rndCountryName = randomGenerator.address().country() + randomGenerator.address().countryCode();
        rndCountryCode = randomGenerator.address().countryCode();

        County newCountry = new County();
        newCountry.name = rndCountryName;
        newCountry.code = rndCountryCode;
        newCountry.translateName = new Object[1];

        given()

                .spec(requestSpec)
                .body(newCountry)

                .when()
                .post("school-service/api/countries/")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;
        System.out.println("countryID = " + countryID);
    }
}
