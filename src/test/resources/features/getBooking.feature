@getbooking @BookingAPI
Feature: Get booking details

  Background:
    Given user hits endpoint "api/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  @getroomdetails
  Scenario Outline: get booking by <description> → validate <expectedStatusCode> response
    Given user hits endpoint "<endpoint>"
    When asks the details of the room by room id <roomId>
    Then the response status code should be <expectedStatusCode>

    Examples:
      | description           | endpoint    | roomId | expectedStatusCode |
      | valid roomId          | api/booking | 4213   | 200                |
      | invalid endpoint      | uuuuu       | 4213   | 404                |

  @invalidpassword
  Scenario Outline: login with <description> → validate <expectedStatusCode> response
    Given user hits endpoint "api/auth/login"
    When user creates a auth token with login authentication as "<username>" and "<password>"
    Then user should get the response code <expectedStatusCode>

    Examples:
      | description       | username | password     | expectedStatusCode |
      | invalid password  | admin    | password123  | 401                |

  @getroomavailability
  Scenario: Get the details of the room availability
    Given user hits endpoint "api/room"
    When user requests the room availability details from "2025-08-17" to "2025-08-19" dates
    Then the response status code should be 200
