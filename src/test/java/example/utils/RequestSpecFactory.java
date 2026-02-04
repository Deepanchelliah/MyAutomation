package example.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private RequestSpecFactory(){}

    public static RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification bearerSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(requestSpecification())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}
