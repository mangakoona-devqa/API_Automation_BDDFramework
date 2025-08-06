@webhook @BookingAPI
Feature: Webhook booking notification simulation

  Background:
    Given WireMock is running on port 9090
    And user creates a stub for webhook endpoint "/webhook/bookingNotification"

  @Mock
  Scenario: Trigger webhook notification and verify response
    When user triggers webhook endpoint "/webhook/bookingNotification"
    Then user should get the response code with 200
    And WireMock should have recorded the webhook request

