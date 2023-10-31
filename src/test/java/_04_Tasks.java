import Model.Location;
import Model.Place;
import Model.Typicode;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _04_Tasks {
    /*
     Task 1
     create a request to https://jsonplaceholder.typicode.com/todos/2
     expect status 200
     expect content type JSON
     expect title in response body to be "quis ut nam facilis et officia qui"
     */


    @Test
    public void Task1() {
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;
    }

    /*
     Task 2
     create a request to https://jsonplaceholder.typicode.com/todos/2
     expect status 200
     expect content type JSON
     a- expect response completed status to be false(using hamcrest)
     b- extract completed field and assert it using testNG Assertion
     */

    @Test
    public void Task2() {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo("false")) // verinin içeride kontrolü
        ;
    }

    //2. Yöntem
    @Test
    public void Task2_1() {
        Boolean bool =
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().path("completed");// verinin dýþarý alýnmasý
        Assert.assertFalse(bool);// ve verinin kontrolü
        //Assert.assertFalse(bool.equals("false"));
    }

    /*
    Task 3
    create a request to https://jsonplaceholder.typicode.com/todos/2
    converting into the POJO
    */
    @Test
    public void Task3() {
        Typicode typicode =
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .extract().body().as(Typicode.class)
                ;
        System.out.println("typicode = " + typicode);
    }
}


