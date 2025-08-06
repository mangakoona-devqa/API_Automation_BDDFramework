@BookingAPI @CreateBooking
Feature: Create Booking API

  @CreateBookingPositiveFlow
  Scenario Outline: Successfully create a new booking
    Given user hits endpoint "api/booking"
    When user books the room with the given details
      | firstname   | lastname   | email                   | phone       | checkin     | checkout    |
      | <firstname> | <lastname> | <email>                 | <phone>     | <checkin>   | <checkout>  |
    Then the response status code should be 200

    Examples:
      | firstname | lastname  | email                    | phone        | checkin    | checkout   |
      | User      | Manga     | user.manga@gmail.com     | 96774589856  | 2025-06-08 | 2025-07-08 |
      | User      | Lakshmi   | user.lakshmi@gmail.com   | 94417971456  | 2025-07-08 | 2025-08-08 |
      | User      | Prasanna  | user.prasanna@gmail.com  | 85003256986  | 2025-08-08 | 2025-09-08 |


  @CreateBookingNegativeFlow
  Scenario Outline: Validate error messages for invalid booking input
    Given user hits endpoint "api/booking"
    When user books the room with the given details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 400
    And the user should see response with error message "<FieldError>"

    Examples:
      | firstname | lastname | email               | phone         | checkin    | checkout   | FieldError                          |
      |           | Manga    | user@gmail.com      | 87676543456   | 2025-03-15 | 2025-03-18 | Firstname should not be blank       |
      | user.     | k        | last@gmail.com      | 90898786765   | 2025-03-15 | 2025-03-18 | size must be between 3 and 30       |
      | Uasr      | LAST     | user                | 90898786765   | 2025-03-15 | 2025-03-18 | must be a well-formed email address |
      | user      | last     | userlast@gmail.com  | 458789        | 2025-03-15 | 2025-03-18 | size must be between 11 and 21      |
      | user      | last     | user.last@gmail.com | 879558797034  |            | 2025-03-18 | must not be null                    |