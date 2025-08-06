package com.booking.stepdefinitions;

import base.Utilities;
import base.WebhookAssertions;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
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


}
