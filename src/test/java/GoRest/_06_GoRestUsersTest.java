package GoRest;

import Model.Data;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class _06_GoRestUsersTest {
    /*
        Postman i aç automotize edeceðin methodun içindeki gerekli verileri (aþaðýdaki gibi) bir .txt dosyasýna al sonra iþlemlerine baþla

        https://gorest.co.in/public/v2/users
        POST

        request body
        {
        "name":"{{$randomFullName}}",
        "gender":"male",
        "email":"{{$randomEmail}}",
        "status":"active"
        }

        Authorization
        Bearer Token
        Bearer a65330413b2f4ed1a0b342a3370742c7e81b2f87b0e63739ee04ee110d8bd5ae

        dönüþ 201
        id extract
    */

    Faker randomGenerator = new Faker();
    int userID = 0; // Postman deki gibi userID PUT ve DELETE iþlemlerinde bize gerekecek

    @Test
    public void createUserJSon() {

        String rndFullName = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();

        userID =
                given()
                        // giden body, token, contentType burada belirleniyor.
                        .header("Authorization", "Bearer a65330413b2f4ed1a0b342a3370742c7e81b2f87b0e63739ee04ee110d8bd5ae")
                        .body("{\n" +
                                "\"name\":\"" + rndFullName + "\",\n" +
                                "\"gender\":\"male\",\n" +
                                "\"email\":\"" + rndEmail + "\",\n" +
                                "\"status\":\"active\"\n" +
                                "}") // giden body
                        .contentType(ContentType.JSON)

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("userID = " + userID);
    }

    @Test
    public void createUserMap() {

        String rndFullName = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();

        Map <String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullName);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID =
                given()

                        .header("Authorization", "Bearer a65330413b2f4ed1a0b342a3370742c7e81b2f87b0e63739ee04ee110d8bd5ae")
                        .body(newUser)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("userID = " + userID);
    }

    @Test
    public void createUserClass() {

        String rndFullName = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();

        // Data class verilerini kullanmak için variables larýn access modifier larýný public yaptýk. Yoksa eriþemeyiz.
        Data newUser = new Data ();
        newUser.name = rndFullName;
        newUser.email = rndEmail;
        newUser.gender = "male";
        newUser.status = "active";

        userID =
                given()

                        .header("Authorization", "Bearer a65330413b2f4ed1a0b342a3370742c7e81b2f87b0e63739ee04ee110d8bd5ae")
                        .body(newUser)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("userID = " + userID);
    }
}
