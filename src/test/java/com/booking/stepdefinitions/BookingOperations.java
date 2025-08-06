package com.booking.stepdefinitions;

import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Map;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import base.BookingDates;
import base.Utilities;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class BookingOperations extends Utilities{

    BookingDates dates;
    int bookingId;

    public BookingOperations() {	}

    @Given("user hits endpoint {string}")
    public void user_hits_endpoint(String endpoint) {

        bookingRequest.setEndPoint(endpoint);
    }

    @When("user books the room with the given details")
    public void user_books_the_room_with_the_given_details(final DataTable dataTable) throws JsonProcessingException{

        int roomid = Integer.parseInt(generateRandomRoomId());
        dates = new BookingDates();
        for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
            bookingRequest.setFirstname(data.get("firstname"));
            bookingRequest.setLastname(data.get("lastname"));
            bookingRequest.setEmail(data.get("email"));
            bookingRequest.setPhone(data.get("phone"));
            dates.setCheckin(data.get("checkin"));
            dates.setCheckout(data.get("checkout"));
            bookingRequest.setBookingdates(dates);
            bookingRequest.setRoomid(roomid);
            bookingRequest.setDepositpaid(false);
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

    @Then("user should get the response code {int}")
    public void user_should_get_the_response_code(Integer statusCode) {
        assertEquals(Long.valueOf(statusCode), Long.valueOf(response.getStatusCode()));
    }

    @When("user creates a auth token with login authentication as {string} and {string}")
    public void user_creates_a_auth_token_with_login_authentication_as_and(String userName, String password) {
        JSONObject loginAuth = new JSONObject();
        loginAuth.put("username", userName);
        loginAuth.put("password", password);
        response = requestSetup().body(loginAuth.toString()).when()
                .post(bookingRequest.getEndPoint());
        String token = response.jsonPath().getString("token");
        bookingRequest.setToken(token);
    }

    @When("asks the details of the room by room id {int}")
    public void asks_the_details_of_the_room_by_room_id(int roomid) {
        response = requestSetup()
                .cookie("token", bookingRequest.getToken())
                .param("roomid", roomid)
                .when()
                .get(bookingRequest.getEndPoint());
    }

    @When("User requests the details of the room by room id")
    public void User_requests_the_details_of_the_room_by_room_id() {

        response = requestSetup()
                .cookie("token", bookingRequest.getToken())
                .param("roomid", bookingRequest.getRoomid())
                .when()
                .get(bookingRequest.getEndPoint());
        bookingId = response.jsonPath().getInt("bookings[0].bookingid");
        bookingRequest.setBookingId(bookingId);
        System.out.println("Booking ID of the booked room = " + bookingId);
        validateBookingResponse(bookingRequest.getFirstname() , bookingRequest.getLastname() , dates.getCheckin() , dates.getCheckout(), bookingRequest.getRoomid());
    }
    @When("the user deletes the booking with booking ID")
    public void theUserDeletesTheBookingWithBookingID() {
        System.out.println("fetchedBookingId = " + bookingRequest.getBookingId());
        int fetchedBookingId = bookingRequest.getBookingId();
        response = requestSetup().cookie("token", bookingRequest.getToken()).when()
                .delete(bookingRequest.getEndPoint() + fetchedBookingId);
    }

    @And("the user should see response with incorrect {string}")
    public void theUserShouldSeeTheResponseWithIncorrectField(final String errorMessage) {
        List<String> actualErrorMessage = response.jsonPath().getList("errors");
        assertEquals("Error message mismatch", errorMessage, actualErrorMessage.get(0));
    }

    @When("the user edits the booking details")
    public void theUserEditsTheBookingDetails(final DataTable dataTable) throws JsonProcessingException {
        int roomid = Integer.parseInt(generateRandomRoomId());
        dates = new BookingDates();
        for (Map<String, String> data : dataTable.asMaps(String.class, String.class)) {
            bookingRequest.setFirstname(data.get("firstname"));
            bookingRequest.setLastname(data.get("lastname"));
            dates.setCheckin(data.get("checkin"));
            dates.setCheckout(data.get("checkout"));
            bookingRequest.setBookingdates(dates);
            bookingRequest.setRoomid(roomid);
            bookingRequest.setDepositpaid(false);
        }

        response = requestSetup()
                .body(createRequestBody())
                .cookie("token", bookingRequest.getToken())
                .when()
                .put(bookingRequest.getEndPoint() + bookingId);


    }
    @When("user requests the room availability details from {string} to {string} dates")
    public void user_requests_the_room_availability_details_from_to_dates(String checkin, String checkout) {
        response = requestSetup()
                .cookie(bookingRequest.getToken())
                .param("checkin", checkin)
                .param("checkin", checkout)
                .when()
                .get(bookingRequest.getEndPoint());
    }

    @Then("validate the response with json schema {string}")
    public void validate_the_response_with_json_schema(String schemaFileName) {
        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + schemaFileName));
    }


}
