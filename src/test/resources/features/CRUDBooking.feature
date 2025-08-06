@BusinessFlow @BookingAPI
Feature: End-to-End Booking Flow

  Background:
    Given user hits endpoint "api/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  @EndToEndCRUD
  Scenario Outline: Perform full booking lifecycle → create, retrieve, update, delete
    Given user hits endpoint "api/booking"
    And user books the room with the given details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 200

    Given user hits endpoint "api/booking"
    When User requests the details of the room by room id
    Then the response status code should be 200

    Given user hits endpoint "api/booking/"
    When the user edits the booking details
      | firstname   | lastname    | email          | phone        | checkin       | checkout       |
      | <editFirst> | <editLast>  | <editEmail>    | <editPhone>  | <editCheckin> | <editCheckout> |
    Then the response status code should be 200

    When the user deletes the booking with booking ID
    Then the response status code should be 200

    Examples:
      | firstname | lastname | email                | phone        | checkin    | checkout   | editFirst | editLast | editEmail              | editPhone   | editCheckin | editCheckout |
      | User      | Five     | user.five@gmail.com  | 46645895464  | 2025-09-15 | 2025-09-17 | User      | Two      | user.two@gmail.com     | 46546321354 | 2025-08-21  | 2025-08-23   |
      | User      | Six      | user.six@gmail.com   | 46645895465  | 2025-10-10 | 2025-10-12 | User      | Three    | user.three@gmail.com   | 46546321355 | 2025-09-21  | 2025-09-23   |
      | User      | Seven    | user.seven@gmail.com | 46645895466  | 2025-11-05 | 2025-11-07 | User      | Four     | user.four@gmail.com    | 46546321356 | 2025-10-21  | 2025-10-23   |
