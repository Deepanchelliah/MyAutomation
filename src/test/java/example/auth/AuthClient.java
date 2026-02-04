package example.auth;

import example.routes.Routes;
import example.utils.AuthRequest;
import example.utils.AuthResponse;
import example.utils.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {

    public AuthResponse login(String username, String password) {
        AuthRequest authRequest = new AuthRequest(username, password);

       Response res = given(RequestSpecFactory.requestSpecification())
                .body(authRequest)
                .when()
                .post(Routes.authUrl());

       res.then().statusCode(200);
       System.out.println("Login API called - generating new token");
        return res.as(AuthResponse.class);
    }
}
