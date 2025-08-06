package base;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utilities {

    ObjectMapper mapper;
    protected BookingRequest bookingRequest = new BookingRequest();;
    String CONTENT_TYPE;
    protected Response response;
    RequestSpecification requestSpecification;
    JSONObject requestBody;

    public Utilities() {
        mapper = new ObjectMapper();
    }

    public String createRequestBody() throws JsonProcessingException {
        String requestbody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookingRequest);
        return requestbody;
    }

    public RequestSpecification requestSetup() {

        RestAssured.baseURI = LoadProperties.getProperty("appURL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        return RestAssured.given().contentType(CONTENT_TYPE).accept(CONTENT_TYPE);
    }

    public String generateRandomRoomId() {

        final Random random = new Random();
        return String.valueOf(2000 + random.nextInt(900));

    }


    public void validateBookingResponse(String firstname, String lastname, String checkin, String checkout,int roomid) {

        List<Integer> roomIds = response.jsonPath().getList("bookings.roomid");
        int responseRoomId = roomIds.get(0);
        String responseFirstname = response.jsonPath().getString("bookings[0].firstname");
        String responseLastname = response.jsonPath().getString("bookings[0].lastname");
        String responseCheckin = response.jsonPath().getString("bookings[0].bookingdates.checkin");
        String responseCheckout = response.jsonPath().getString("bookings[0].bookingdates.checkout");

        assertEquals("First Name did not match", firstname, responseFirstname);
        assertEquals("Room Id did not match", roomid, responseRoomId);
        assertEquals("Last Name did not match", lastname, responseLastname);
        assertEquals("Check in date did not match", checkin, responseCheckin);
        assertEquals("Checkout date did not match", checkout, responseCheckout);
    }


}
