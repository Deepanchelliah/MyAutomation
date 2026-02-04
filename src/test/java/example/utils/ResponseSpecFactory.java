package example.utils;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.containsString;

public class ResponseSpecFactory {
    private ResponseSpecFactory() {}

    public static ResponseSpecification jsonResponse() {
        return new ResponseSpecBuilder()
                .expectHeader("Content-Type", containsString("application/json"))
                .log(LogDetail.ALL)
                .build();
    }
}
