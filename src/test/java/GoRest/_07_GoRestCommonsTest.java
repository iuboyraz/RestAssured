package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _07_GoRestCommonsTest {
    Faker randomGenerator = new Faker();
    int commentID = 0; // POSTMAN deki gibi userID GET, PUT ve DELETE işlemlerinde bize gerekecek
    RequestSpecification requestSpec;
    @BeforeClass
    public void setUp() {

        baseURI = "https://gorest.co.in/public/v2/comments";

        requestSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer a65330413b2f4ed1a0b342a3370742c7e81b2f87b0e63739ee04ee110d8bd5ae")
                // her seferinde header'ı yazmamak için spec içine yerleştirdik ve ilerleyen testlerde kullanacağız.
                .setContentType(ContentType.JSON)
                .build()
        ;
    }

    @Test
    public void createCommentMap() { // POSTMAN CREATE
        /*
        {
        "id": 67078,
        "post_id": 82477,
        "name": "Deeptanshu Prajapat",
        "email": "deeptanshu_prajapat@denesik.example",
        "body": "Maiores aut id. Exercitationem fugiat quae."
        },
         */

        String rndName = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();
        String rndBody = randomGenerator.lorem().paragraph();

        Map<String, String> newComment = new HashMap<>();
        newComment.put("post_id", "82477");
        newComment.put("name", rndName);
        newComment.put("email", rndEmail);
        newComment.put("body", rndBody);

        commentID =
                given()

                        .spec(requestSpec)
                        .body(newComment)

                        .when()
                        .post(baseURI)

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("userID = " + commentID);
    }

    // POSTMAN deki gibi UserId yi CREATE metoduyla oluşturduktan sonra GET ile request işlemi yaptık.
    @Test(dependsOnMethods = "createCommentMap")
    public void getCommentById() { // POSTMAN GET

        given()
                .spec(requestSpec) // her seferinde .header() .contentType yazmamak için (29. satırda) requestSpec tanımladık @BeforeClass ta da çalıştırdık.

                .when()
                .get("" + commentID)// @BeforeClass ta baseURI yi tanımladık o yüzden burada "" şekilde

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))

        ;
    }

    @Test(dependsOnMethods = "getCommentById")
    public void updateComment() { // POSTMAN PUT

        Map<String, String> updateComment = new HashMap<>();
        updateComment.put("name", "Ali Cabbar");
        updateComment.put("email", "ali.cabbar@yahoo.com");

        given()
                .spec(requestSpec)
                .body(updateComment)

                .when()
                .put("" + commentID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(commentID))
                .body("name", equalTo("Ali Cabbar"))
                .body("email", equalTo("ali.cabbar@yahoo.com"))

        ;
    }

    @Test(dependsOnMethods = "updateComment")
    public void deleteComment() { // POSTMAN DELETE

        given()
                .spec(requestSpec)

                .when()
                .delete("" + commentID)

                .then()
                //.log().all()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteComment")
    public void deleteCommentNegative() { // POSTMAN DELETE NEGATIVE

        given()
                .spec(requestSpec)

                .when()
                .delete("" + commentID)

                .then()
                //.log().all()
                .statusCode(404)
        ;
    }
}
