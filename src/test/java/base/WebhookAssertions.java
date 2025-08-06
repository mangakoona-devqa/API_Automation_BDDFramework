package base;
import io.restassured.response.Response;
import static org.junit.Assert.assertEquals;

public class WebhookAssertions {

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        assertEquals("Status code did not match", expectedStatusCode, response.getStatusCode());
    }

}
