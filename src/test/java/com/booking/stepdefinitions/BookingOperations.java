package com.booking.stepdefinitions;

import base.BookingDates;
import base.Utilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BookingOperations extends Utilities  {

    public static Response response;

    BookingDates dates;


    public BookingOperations() {	}

    @Given("user hits endpoint {string}")
    public void user_hits_endpoint(String endpoint) {

        bookingRequest.setEndPoint(endpoint);
    }

    @When("user books the room with the given details")
    public void user_books_the_room_with_the_given_details(final DataTable dataTable) throws JsonProcessingException {

        int roomid = Integer.parseInt(generateRandomRoomId());
        dates = new BookingDates();
        for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
            bookingRequest.setFirstname(data.get("firstname"));
            bookingRequest.setLastname(data.get("lastname"));
            bookingRequest.setEmail(data.get("email"));
            bookingRequest.setPhone(data.get("phone"));
            dates.setCheckin(data.get("checkin"));
            dates.setCheckout(data.get("checkout"));
            bookingRequest.setRoomid(roomid);

        }

        response = requestSetup()
                .body(createRequestBody())
                .when()
                .post("api/booking");
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {

        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @And("the user should see response with incorrect {string}")
    public void theUserShouldSeeTheResponseWithIncorrectField(final String errorMessage) {
        List<String> actualErrorMessage = response.jsonPath().getList("errors");
        assertEquals("Error message mismatch", errorMessage, actualErrorMessage.get(0));
    }



}
