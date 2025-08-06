@swaggervalidations @BookingAPI
Feature: swagger validation

  Background:
    Given user hits endpoint "api/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

@performschemavalidation
Scenario: schema validation for booking room response
Given user hits endpoint "api/booking"
When asks the details of the room by room id 421
Then the response status code should be 200
Then validate the response with json schema "getbookingresponseschema.json"