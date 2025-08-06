@getbooking @BookingAPI
Feature: Get booking details

  Background:
    Given user hits endpoint "api/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  @getroomdetails
  Scenario Outline: Validate booking details response codes
    Given user hits endpoint "<endpoint>"
    When asks the details of the room by room id <roomId>
    Then the response status code should be <expectedStatusCode>

    Examples:
      | endpoint        | roomId | expectedStatusCode |
      | api/booking     | 4213    | 200               |
      | uuuuu           | 4213    | 404               |
