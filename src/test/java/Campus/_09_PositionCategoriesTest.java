package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class _09_PositionCategoriesTest {
    Faker randomGenerator = new Faker();
    String positionCategoryId;
    RequestSpecification requestSpec;
    String rndPositionCategoryName;
    Map<String, String> newPositionCategory;
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
                .statusCode(200)
                .extract().response().detailedCookies()
        ;

        requestSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build()
        ;
    }
    @Test
    public void createPositionCategory(){

        rndPositionCategoryName = randomGenerator.job().field();

        newPositionCategory = new HashMap<>();
        newPositionCategory.put("name", rndPositionCategoryName);

        positionCategoryId =
                given()

                        .spec(requestSpec)
                        .body(newPositionCategory)

                        .when()
                        .post("school-service/api/position-category/")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("positionCategoryId = " + positionCategoryId);
    }

    // Ayný countryName ve code gönderildiðinde kayýt yapýlmadýðýna dair ilgili testi yazýnýz.
    // Yani createCountryNegative testini yapýnýz.
    // Response da "Please provide valid data to update Country" olduðunu doðrulayýnýz.
    @Test (dependsOnMethods = "createPositionCategory")
    public void createPositionCategoryNegative(){

                given()

                        .spec(requestSpec)
                        .body(newPositionCategory)

                        .when()
                        .put("school-service/api/position-category/")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message", containsString("Please, provide valid data to update 'Position Category', it's not created to update"))

        ;
        System.out.println("positionCategoryId = " + positionCategoryId);
    }
    @Test (dependsOnMethods = "createPositionCategoryNegative")
    public void updatePositionCategory(){
        String newPositionCategoryName = randomGenerator.job().field() + randomGenerator.number().digits(5);

        Map<String, String> updatePositionCategory = new HashMap<>();
        updatePositionCategory.put("id", positionCategoryId);
        updatePositionCategory.put("name", newPositionCategoryName);

        given()

                .spec(requestSpec)
                .body(updatePositionCategory)

                .when()
                .put("school-service/api/position-category/")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newPositionCategoryName))

        ;
        System.out.println("positionCategoryId = " + positionCategoryId);
        System.out.println("newPositionCategoryName = " + newPositionCategoryName);
    }
    @Test (dependsOnMethods = "updatePositionCategory")
    public void deletePositionCategory(){

        given()
                .spec(requestSpec)

                .when()
                .delete("school-service/api/position-category/" + positionCategoryId)

                .then()
                .log().body()
                .statusCode(204)

        ;
        System.out.println("positionCategoryId = " + positionCategoryId);
    }
    @Test (dependsOnMethods = "deletePositionCategory")
    public void deletePositionCategoryNegative(){

        given()
                .spec(requestSpec)

                .when()
                .delete("school-service/api/position-category/" + positionCategoryId)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("PositionCategory not  found"))

        ;
        System.out.println("positionCategoryId = " + positionCategoryId);
    }
}
