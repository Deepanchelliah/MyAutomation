package example.resource;

import example.auth.TokenManager;
import example.routes.Routes;
import example.utils.RequestSpecFactory;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ResourceTests {

    @Test
    public void getOrders_shouldReturn200() {
        String token = TokenManager.getToken();

        given(RequestSpecFactory.bearerSpec(token))
                .when()
                .get(Routes.resourceOrderUrl())
                .then()
                .statusCode(200);
    }

    @Test
    public void getOrders_checkSize() {
        String token = TokenManager.getToken();

        Response res = given(RequestSpecFactory.bearerSpec(token))
        .when()
                .get(Routes.resourceOrderUrl());

        res.then()
                .body("size()", greaterThan(0))   // safer than hard-coding 3
                .body("status", everyItem(anyOf(is("PAID"), is("CANCELLED"))));

    }
}
