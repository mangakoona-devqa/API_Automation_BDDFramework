package base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Utilities {
    ObjectMapper mapper;
    String CONTENT_TYPE;


    public Utilities() {
        mapper = new ObjectMapper();
    }

    public RequestSpecification requestSetup() {

        RestAssured.baseURI = LoadProperties.getProperty("appURL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        return RestAssured.given().contentType(CONTENT_TYPE).accept(CONTENT_TYPE);
    }


}
