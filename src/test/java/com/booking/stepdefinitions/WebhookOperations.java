package com.booking.stepdefinitions;

import base.Utilities;
import base.WebhookAssertions;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class WebhookOperations extends Utilities {

    private Response response;
    private int bookingStubStatusCode;   // To hold dynamic status code

    @Given("WireMock is running on port {int}")
    public void wiremock_is_running_on_port(Integer port) {
        WireMock.configureFor("localhost", port);
        response = given().when().get("http://localhost:" + port + "/__admin/health");

    }

    @And("user creates a stub for webhook endpoint {string}")
    public void user_creates_a_stub_for_webhook_endpoint(String endpoint) {
        stubFor(post(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"status\":\"Webhook received\"}")));
    }

    @When("user triggers webhook endpoint {string}")
    public void user_triggers_webhook_endpoint(String endpoint) {
        response = given()
                .header("Content-Type", "application/json")
                .body("{\"bookingId\":9999,\"status\":\"CONFIRMED\"}")
                .when()
                .post("http://localhost:9090" + endpoint);
    }

    @Then("user should get the response code with {int}")
    public void user_should_get_the_response_code_with(Integer expectedCode) {
        WebhookAssertions.validateStatusCode(response, expectedCode);
    }

    @And("WireMock should have recorded the webhook request")
    public void wiremock_should_have_recorded_the_webhook_request() {
        verify(postRequestedFor(urlEqualTo("/webhook/bookingNotification")));
    }
    @And("user creates a stub for booking API with response status {int} and body:")
    public void user_creates_a_stub_for_booking_api_with_response_status_and_body(Integer statusCode, DataTable responseTable) {
        bookingStubStatusCode = statusCode;
        Map<String, String> responseMap = responseTable.asMaps().get(0);

        String responseBody = "{ \"bookingid\": " + responseMap.get("bookingid") +
                ", \"status\": \"" + responseMap.get("status") + "\" }";

        stubFor(post(urlEqualTo("/api/booking"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(bookingStubStatusCode)
                        .withBody(responseBody)));
    }

    @When("user calls the mock booking endpoint with details:")
    public void user_calls_the_mock_booking_endpoint_with_details(DataTable requestTable) {
        Map<String, String> req = requestTable.asMaps().get(0);

        String body = "{ " +
                "\"roomid\": " + req.get("roomid") + "," +
                "\"firstname\": \"" + req.get("firstname") + "\"," +
                "\"lastname\": \"" + req.get("lastname") + "\"," +
                "\"depositpaid\": " + req.get("depositpaid") + "," +
                "\"email\": \"" + req.get("email") + "\"," +
                "\"phone\": \"" + req.get("phone") + "\"," +
                "\"bookingdates\": {\"checkin\":\"" + req.get("checkin") + "\",\"checkout\":\"" + req.get("checkout") + "\"}" +
                "}";

        response = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("http://localhost:9090/api/booking");
    }

    @And("response body should contain values:")
    public void response_body_should_contain_values(DataTable expectedTable) {
        WebhookAssertions.validateBookingResponse(response, expectedTable);
    }



}
