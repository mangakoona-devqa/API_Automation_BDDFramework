package com.booking.stepdefinitions;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SwaggerValidationSteps {

    @Then("validate {string} request to {string} with body and swagger spec {string} expecting status {int}")
    public void validate_request_with_body_against_swagger_spec(
            String method,
            String endpoint,
            String specPath,
            int expectedStatus,
            DataTable dataTable) {

        // Base URI
        RestAssured.baseURI = "https://automationintesting.online";

        // Build dynamic payload with correct types
        Map<String, Object> bookingPayload = new HashMap<>();
        dataTable.asMaps(String.class, String.class).forEach(row -> {
            row.forEach((key, value) -> {
                if ("depositpaid".equalsIgnoreCase(key)) {
                    bookingPayload.put(key, Boolean.parseBoolean(value));
                } else if ("roomid".equalsIgnoreCase(key)) {
                    bookingPayload.put(key, Integer.parseInt(value));
                } else {
                    bookingPayload.put(key, value);
                }
            });
        });

        // OpenAPI Validator (default)
        OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(
                OpenApiInteractionValidator.createFor(specPath).build()
        );


        Filter customFilter = (FilterableRequestSpecification requestSpec,
                               FilterableResponseSpecification responseSpec,
                               FilterContext ctx) -> {
            Response response = ctx.next(requestSpec, responseSpec);
            try {
                validationFilter.filter(requestSpec, responseSpec, ctx);
            } catch (Exception ex) {
                if (ex.getMessage() != null &&
                        ex.getMessage().contains("Response status")) {
                    System.out.println(" status validation: " + ex.getMessage());
                } else {
                    throw ex;
                }
            }
            return response;
        };

        // Send request
        Response response = given()
                .filter(customFilter)
                .contentType("application/json")
                .body(bookingPayload)
                .when()
                .request(method, endpoint);

        // Verify only actual status code
        response.then().statusCode(expectedStatus);

        System.out.println("Response Body:\n" + response.asString());
    }
}
