package base;
import io.cucumber.datatable.DataTable;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WebhookAssertions {

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        assertEquals("Status code did not match", expectedStatusCode, response.getStatusCode());
    }
    public static void validateBookingResponse(Response response, DataTable expectedTable) {
        String body = response.getBody() != null ? response.getBody().asString().trim() : "";

        if (body.isEmpty()) {
            return; // nothing to check if body is empty
        }

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, String>> expectedList = expectedTable.asMaps(String.class, String.class);

        for (Map<String, String> expected : expectedList) {
            if (expected.containsKey("bookingid")) {
                String actualBookingId = String.valueOf(jsonPath.getInt("bookingid"));
                assertEquals("Booking ID mismatch", expected.get("bookingid"), actualBookingId);
            }
            if (expected.containsKey("status")) {
                String actualStatus = jsonPath.getString("status");
                assertEquals("Status mismatch", expected.get("status"), actualStatus);
            }
        }
    }
}
