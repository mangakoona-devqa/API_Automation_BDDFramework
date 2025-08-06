package com.booking.stepdefinitions;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import com.github.tomakehurst.wiremock.client.WireMock;
import base.Utilities;
import base.WebhookAssertions;

public class WebhookOperations extends Utilities {

    private Response response;
    private int bookingStubStatusCode;   // To hold dynamic status code

    @Given("WireMock is running on port {int}")
    public void wiremock_is_running_on_port(Integer port) {
        WireMock.configureFor("localhost", port);
        // Health check log only (no static assert)
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

    private String dynamicBookingId; // store dynamic booking id

    @And("user generates a random booking id")
    public void user_generates_a_random_booking_id() {
        dynamicBookingId = String.valueOf((int) (Math.random() * 1000000));
        System.out.println("Generated  booking ID: " + dynamicBookingId);
    }

    @And("user stubs booking API with status {int} and body:")
    public void user_stubs_booking_api_with_status_and_body(Integer statusCode, DataTable table) {
        String status = table.asMaps(String.class, String.class).get(0).get("status");
        String responseBody = "{ \"bookingid\": " + dynamicBookingId + ", \"status\": \"" + status + "\" }";

        stubFor(post(urlEqualTo("/api/booking"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(statusCode)
                        .withBody(responseBody)));
    }

    @Then("response should have booking id and status {string}")
    public void response_should_have_booking_id_and_status(String expectedStatus) {
        String returnedId = String.valueOf(response.jsonPath().getInt("bookingid"));
        String returnedStatus = response.jsonPath().getString("status");
        org.junit.Assert.assertEquals("Booking ID mismatch", dynamicBookingId, returnedId);
        org.junit.Assert.assertEquals("Status mismatch", expectedStatus, returnedStatus);
    }

    @And("user stubs delete booking with status {int} and body:")
    public void user_stubs_delete_booking_with_status_and_body(Integer statusCode, DataTable table) {
        String message = table.asMaps(String.class, String.class).get(0).get("message");
        String responseBody = "{ \"message\": \"" + message + "\" }";

        stubFor(delete(urlEqualTo("/api/booking/" + dynamicBookingId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(statusCode)
                        .withBody(responseBody)));
    }

    @When("user deletes the booking")
    public void user_deletes_the_booking() {
        response = given()
                .when()
                .delete("http://localhost:9090/api/booking/" + dynamicBookingId);
    }

    @Then("delete response should have:")
    public void delete_response_should_have(DataTable expectedTable) {
        WebhookAssertions.validateDeleteResponse(response, expectedTable);
    }

}
