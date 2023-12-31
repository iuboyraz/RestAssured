package ApiKeyTest;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiKeyTest {

    /*
    Interview da
    endpoint: https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user
    olarak verilir.
    Bearer Token yerine de Api Key -> key , value olarak verilir.

    key: x-api-key
    value:GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra

    POSTMAN da ApiKeyTest/Authorization � incele !
     */

    @Test
    public void test1 (){

        given()
                .header("x-api-key", "GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra")

                .when()
                .get("https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user")

                .then()
                .log().body()

                ;
    }
}
