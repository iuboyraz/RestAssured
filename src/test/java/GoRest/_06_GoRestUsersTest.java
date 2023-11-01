package GoRest;

import Model.Data;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _06_GoRestUsersTest {
    /*
        POSTMAN i aç automotize edeceğin methodun içindeki gerekli (aşağıdaki gibi)verileri bir .txt dosyasına al sonra işlemlerine başla

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

        dönüş 201
        id extract
    */

    Faker randomGenerator = new Faker();
    int userID = 0; // POSTMAN deki gibi userID GET, PUT ve DELETE işlemlerinde bize gerekecek

    RequestSpecification requestSpec;
    @BeforeClass
    public void setUp (){

        baseURI = "https://gorest.co.in/public/v2/users";

        requestSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer a65330413b2f4ed1a0b342a3370742c7e81b2f87b0e63739ee04ee110d8bd5ae")
                // her seferinde header'ı yazmamak için spec içine yerleşitrdik ve ilerleyen testlerde kullanacağız.
                .setContentType(ContentType.JSON)
                .build()

        ;

    }

    @Test(enabled = false)
    public void createUserJSon() {

        String rndFullName = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();
        String rndGender = randomGenerator.demographic().sex();

        userID =
                given()
                        // giden body, token, contentType burada belirleniyor.
                        .header("Authorization", "Bearer a65330413b2f4ed1a0b342a3370742c7e81b2f87b0e63739ee04ee110d8bd5ae")
                        .body("{\n" +
                                "\"name\":\"" + rndFullName + "\",\n" +
                                "\"gender\":\""+rndGender+"\",\n" +
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
    public void createUserMap() { // POSTMAN CREATE

        String rndFullName = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();
        String rndGender = randomGenerator.demographic().sex();

        Map <String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullName);
        newUser.put("gender", rndGender);
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

    @Test(enabled = false)
    public void createUserClass() {

        String rndFullName = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();
        String rndGender = randomGenerator.demographic().sex();

        // Data class verilerini kullanmak için variables ların access modifier larını public yaptık. Yoksa erişemeyiz.
        Data newUser = new Data ();
        newUser.name = rndFullName;
        newUser.email = rndEmail;
        newUser.gender = rndGender;
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
    // POSTMAN deki gibi UserId yi CREATE metoduyla oluşturduktan sonra GET ile request işlemi yaptık.
    @Test (dependsOnMethods = "createUserMap")
    public void getUserById() { // POSTMAN GET

        given()
                .spec(requestSpec) // her seferinde .header() .contentType yazmamak için (43. satırda) requestSpec tanımladık @BeforeClass ta da çalıştırdık.

                .when()
                .get("" + userID)// @BeforeClass ta baseURI yi tanımladık o yüzden burada "" şekilde

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))

                ;
    }

    @Test (dependsOnMethods = "getUserById")
    public void updateUser (){ // POSTMAN PUT

        Map<String,String> updateUser = new HashMap<>();
        updateUser.put("name", "Ali Cabbar");

        given()
                .spec(requestSpec)
                .body(updateUser)

                .when()
                .put("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo("Ali Cabbar"))

                ;
    }

    @Test (dependsOnMethods = "updateUser")
    public void deleteUser (){ // POSTMAN DELETE

        given()
                .spec(requestSpec)

                .when()
                .delete("" + userID)

                .then()
                //.log().all()
                .statusCode(204)
                ;
    }

    @Test (dependsOnMethods = "deleteUser")
    public void deleteUserNegative (){ // POSTMAN DELETE NEGATIVE

        given()
                .spec(requestSpec)

                .when()
                .delete("" + userID)

                .then()
                //.log().all()
                .statusCode(404)
        ;
    }
}
