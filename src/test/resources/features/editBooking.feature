@EditBooking @BookingAPI
Feature: Edit Booking Details

  Background:
    Given user hits endpoint "api/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  @EditBookingPositiveFlow
  Scenario Outline: Edit the booking details dynamically
    Given user hits endpoint "api/booking/"
    And user books the room with the given details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then user should get the response code 200

    When User requests the details of the room by room id
    Then the response status code should be 200
    When the user edits the booking details
      | firstname   | lastname   | email                 | phone        | checkin    | checkout   |
      | <editFirst> | <editLast> | <editEmail>          | <editPhone>  | <editCheckin> | <editCheckout> |
    Then the response status code should be 200

    Examples:
      | firstname | lastname | email                | phone        | checkin    | checkout   | editFirst | editLast | editEmail            | editPhone   | editCheckin  | editCheckout |
      | User      | Koona    | user.koona@gmail.com | 46645895464  | 2025-09-15 | 2025-09-17 | User      | Manga    | user.manga@gmail.com | 46546321354 | 2025-08-21   | 2025-08-23   |
