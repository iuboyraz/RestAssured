package Campus;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class US105 {
    Faker randomGenerator = new Faker();
    String studentGroupId;
    RequestSpecification requestSpec;
    String rndStudentGroupName;
    String rndDescription;
    Boolean rndPublicGroup;
    Boolean rndShowToStudent;
    String schoolId = "6576fd8f8af7ce488ac69b89";
    Map<String, String> newStudentGroup;

    @BeforeClass
    public void Login() {
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
                        .contentType(ContentType.JSON)
                        .log().body()

                        .statusCode(200)

                        .body("access_token", instanceOf(String.class))
                        .body("token_type", instanceOf(String.class))
                        .body("refresh_token", instanceOf(String.class))
                        .body("expires_in", instanceOf(Integer.class))
                        .body("scope", instanceOf(String.class))
                        .body("passwordChange", instanceOf(Boolean.class))
                        .body("username", instanceOf(String.class))
                        .body("iat", instanceOf(Integer.class))
                        .body("jti", instanceOf(String.class))
                        .body("is_2fa_enabled", instanceOf(Boolean.class))

                        .body("access_token", notNullValue())
                        .body("token_type", notNullValue())
                        .body("refresh_token", notNullValue())
                        .body("expires_in", greaterThan(0))
                        .body("scope", notNullValue())
                        .body("passwordChange", notNullValue())
                        .body("username", notNullValue())
                        .body("iat", notNullValue())
                        .body("jti", notNullValue())
                        .body("is_2fa_enabled", notNullValue())

                        .extract().response().detailedCookies();

        requestSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build()
        ;
    }

    @Test
    public void createNewStudentGroup() {
        rndStudentGroupName = randomGenerator.job().field() + randomGenerator.number().digits(5);
        rndDescription = randomGenerator.job().position();
        rndPublicGroup = randomGenerator.bool().bool();
        rndShowToStudent = randomGenerator.bool().bool();

        newStudentGroup = new HashMap<>();
        newStudentGroup.put("name", rndStudentGroupName);
        newStudentGroup.put("description", rndDescription);
        newStudentGroup.put("publicGroup", String.valueOf(rndPublicGroup));
        newStudentGroup.put("showToStudent", String.valueOf(rndShowToStudent));
        newStudentGroup.put("schoolId", schoolId);

        studentGroupId =
                given()

                        .spec(requestSpec)
                        .body(newStudentGroup)

                        .when()
                        .post("school-service/api/student-group/")

                        .then()
                        .contentType(ContentType.JSON)
                        .log().body()

                        .statusCode(201)

                        .body("id", instanceOf(String.class))
                        .body("schoolId", instanceOf(String.class))
                        .body("name", instanceOf(String.class))
                        .body("description", instanceOf(String.class))
                        .body("count", instanceOf(Integer.class))
                        .body("active", instanceOf(Boolean.class))
                        .body("publicGroup", instanceOf(Boolean.class))
                        .body("showToStudent", instanceOf(Boolean.class))

                        .body("id", notNullValue())
                        .body("schoolId", notNullValue())
                        .body("name", notNullValue())
                        .body("description", notNullValue())
                        .body("count", notNullValue())
                        .body("active", notNullValue())
                        .body("publicGroup", notNullValue())
                        .body("showToStudent", notNullValue())

                        .extract().path("id")
        ;
        System.out.println("studentGroupId = " + studentGroupId);
    }

    @Test
    public void assertNewStudentGroup(){

        Response body =
                given()
                        .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZENoYW5nZSI6ZmFsc2UsInVzZXJfbmFtZSI6InR1cmtleXRzIiwic2NvcGUiOlsib3BlbmlkIl0sImV4cCI6MTcwNDU2MDc0MCwiaWF0IjoxNzA0NTYwNDQwLCJhdXRob3JpdGllcyI6WyJST0xFX0VWRVJZT05FIiwiUk9MRV9URUNITk9fVEVTVCJdLCJqdGkiOiJFVDJ0TGhxNHQ1cFZHbGZ3dmhHeDA2VGdlU2siLCJjbGllbnRfaWQiOiJ3ZWJfYXBwIiwidXNlcm5hbWUiOiJ0dXJrZXl0cyJ9.RYN3ryWj5AOfwfKWefB2XD3XpwJZaDDxfWRRBd_qwZyNLkkTl4QAhxcmk9A1wzPhrNv0sPdxhA3SPXQvjRd0vut7QitZu2mE-9wakl62USHH4gMUxNYmltQnNpaydS920nlfV4J0liT5ibMtL-iXoAbaMu0tOdymsHHoeEDMsWGRIVFIRBHUOogNko3lfyWVGNGaj2WLbkuHtDVKuqBhCGOy2Q7GE6Px5UTW3Djo2FR9X05c-FIAEyJz6XSFxRQHF5ayB3x5asRsrFydV3tpIZd0Rw4cmB83HLRDrbZgoM7HImQyUgHd3yBvOJGR4Id6MHdrWve-d3C1-JvG5jJvmg")
                        .when()
                        .post("school-service/api/student-group/search")

                        .then()
                        .log().body()
                        .extract().response();

        String name = body.path("[0].name");

        /*
        List <String> names = body.path("name");
        List <String> descriptions = body.path("description");
        int characterCount = 1000;
        int descriptionCount = 5000;

         */

        System.out.println("name = " + name);
        /*
        System.out.println("descriptions = " + descriptions);

        for (String name:names) {
            if (name.length() < characterCount) {
                System.out.println("name is less than " + characterCount);
            } else
                System.out.println("name is bigger than " + characterCount);
        }

        for (String description:descriptions) {
            if (description.length() < characterCount) {
                System.out.println("name is less than " + descriptionCount);
            } else
                System.out.println("name is bigger than " + descriptionCount);
        }

         */

    }

    @Test(dependsOnMethods = "createNewStudentGroup")
    public void createNewStudentGroupNegative() {
        given()

                .spec(requestSpec)
                .body(newStudentGroup)

                .when()
                .post("school-service/api/student-group/")

                .then()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .log().body()
                .body("message", containsString("the same name already exists!"))

                .body("type", instanceOf(String.class))
                .body("status", instanceOf(Integer.class))
                .body("detail", instanceOf(String.class))
                .body("path", instanceOf(String.class))
                //.body("code", instanceOf(String.class))
                .body("message", instanceOf(String.class))
                //.body("lang", instanceOf(String.class))
                //.body("uri", instanceOf(String.class))

                .body("type", notNullValue())
                .body("status", notNullValue())
                .body("detail", notNullValue())
                .body("path", notNullValue())
                .body("code", nullValue())
                .body("message", notNullValue())
                .body("lang", nullValue())
                .body("uri", nullValue())
        ;
        System.out.println("studentGroupId = " + studentGroupId);
    }

    @Test(dependsOnMethods = "createNewStudentGroupNegative")
    public void updateStudentGroup() {

        String updatedStudentGroupName = randomGenerator.job().field() + randomGenerator.number().digits(5);
        String updatedDescription = randomGenerator.job().position();
        Boolean updatedActive = randomGenerator.bool().bool();
        Boolean updatedPublicGroup = randomGenerator.bool().bool();
        Boolean updatedShowToStudent = randomGenerator.bool().bool();

        Map<String, String> updateStudentGroup = new HashMap<>();

        updateStudentGroup.put("id", studentGroupId);
        updateStudentGroup.put("schoolId", schoolId);
        updateStudentGroup.put("name", updatedStudentGroupName);
        updateStudentGroup.put("description", updatedDescription);
        updateStudentGroup.put("active", String.valueOf(updatedActive));
        updateStudentGroup.put("publicGroup", String.valueOf(updatedPublicGroup));
        updateStudentGroup.put("showToStudent", String.valueOf(updatedShowToStudent));

        given()

                .spec(requestSpec)
                .body(updateStudentGroup)

                .when()
                .put("school-service/api/student-group/")

                .then()
                .contentType(ContentType.JSON)
                .log().body()

                .statusCode(200)
                .body("name", equalTo(updatedStudentGroupName))

                .body("id", instanceOf(String.class))
                .body("schoolId", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("description", instanceOf(String.class))
                .body("count", instanceOf(Integer.class))
                .body("active", instanceOf(Boolean.class))
                .body("publicGroup", instanceOf(Boolean.class))
                .body("showToStudent", instanceOf(Boolean.class))

                .body("id", notNullValue())
                .body("schoolId", notNullValue())
                .body("name", notNullValue())
                .body("description", notNullValue())
                .body("count", notNullValue())
                .body("active", notNullValue())
                .body("publicGroup", notNullValue())
                .body("showToStudent", notNullValue())

        //.body("name", hasLength(1000))
        //.body("description", hasLength(5000))
        ;
        System.out.println("studentGroupId = " + studentGroupId);
        System.out.println("updatedStudentGroupName = " + updatedStudentGroupName);
    }

    @Test(dependsOnMethods = "updateStudentGroup")
    public void deleteStudentGroup() {
        given()
                .spec(requestSpec)

                .when()
                .delete("school-service/api/student-group/" + studentGroupId)

                .then()
                .log().body()
                .statusCode(200)

        ;
        System.out.println("studentGroupId = " + studentGroupId);
    }

    @Test(dependsOnMethods = "deleteStudentGroup")
    public void deleteStudentGroupNegative() {
        given()
                .spec(requestSpec)

                .when()
                .delete("school-service/api/student-group/" + studentGroupId)

                .then()
                .contentType(ContentType.JSON)
                .log().body()
                .statusCode(400)
                .body("message", containsString("id does not exist!"))

                .body("type", instanceOf(String.class))
                .body("status", instanceOf(Integer.class))
                .body("detail", instanceOf(String.class))
                .body("path", instanceOf(String.class))
                //.body("code", instanceOf(String.class))
                .body("message", instanceOf(String.class))
                //.body("lang", instanceOf(String.class))
                //.body("uri", instanceOf(String.class))

                .body("type", notNullValue())
                .body("status", notNullValue())
                .body("detail", notNullValue())
                .body("path", notNullValue())
                .body("code", nullValue())
                .body("message", notNullValue())
                .body("lang", nullValue())
                .body("uri", nullValue())

        ;
        System.out.println("studentGroupId = " + studentGroupId);
    }
}
