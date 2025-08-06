@webhook @BookingAPI
Feature: Webhook booking notification simulation

  Background:
    Given WireMock is running on port 9090
    And user creates a stub for webhook endpoint "/webhook/bookingNotification"

  @Mock
  Scenario: Trigger webhook notification → validate 200 response
    When user triggers webhook endpoint "/webhook/bookingNotification"
    Then user should get the response code with 200
    And WireMock should have recorded the webhook request

  @BookingStub
  Scenario: Create booking stub → validate 201 response
    Given WireMock is running on port 9090
    And user creates a stub for booking API with response status 201 and body:
      | bookingid | status   |
      | 999978    | CREATED  |
    When user calls the mock booking endpoint with details:
      | roomid | firstname | lastname | depositpaid | email               | phone       | checkin     | checkout    |
      | 109    | User      | five     | false       | user.five@gmail.com | 46645895464 | 2025-09-15  | 2025-09-17  |
    Then user should get the response code with 201
    And response body should contain values:
      | bookingid | status   |
      | 999978    | CREATED  |

  @DeleteStub
  Scenario: Create and delete booking stub → validate 201 & 204 responses
    Given WireMock is running on port 9090
    And user generates a random booking id
    And user stubs booking API with status 201 and body:
      | status  |
      | CREATED |
    When user calls the mock booking endpoint with details:
      | roomid | firstname | lastname | depositpaid | email               | phone       | checkin     | checkout    |
      | 109    | User      | Five     | false       | user.five@gmail.com | 46645895464 | 2025-09-15  | 2025-09-17  |
    Then user should get the response code with 201
    And response should have booking id and status "CREATED"
    And user stubs delete booking with status 204 and body:
      | message                     |
      | Booking deleted successfully|
    When user deletes the booking
    Then user should get the response code with 204
    And delete response should have:
      | message                     |
      | Booking deleted successfully|