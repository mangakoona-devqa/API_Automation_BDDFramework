# Rest API Automation Test Framework

## Overview
This is a Rest-Assured and Cucumber BDD-based test automation framework designed to test a Hotel Booking API. The framework supports dynamic request construction, schema validation, webhook simulation, and end-to-end CRUD scenarios.

 ## Key Features
- Test automation framework designed for **Hotel Room Booking APIs**.
- Built on **Cucumber BDD** .
- Dynamic JSON request creation using **Jackson POJOs**.
- Schema validation using `JsonSchemaValidator`.
- **Swagger validation**: response is validated against the schema `getbookingresponseschema.json`.
- **WireMock** integration: used to simulate and validate **webhook booking notifications**.
- Utility class provides all reusable methods and configurations.
- Logging is managed using `log4j`.
  
##  Prerequisites

- Java 17+
- Maven
- IntelliJ IDEA or Eclipse (Cucumber & Maven plugins recommended)
  
## Installation
Clone the repository: git clone https://github.com/mangakoona-devqa/API_Automation_BDDFramework.git

## How to Run the Tests

Update the tags value in the TestRunner.java file located at src/test/java/com.booking/TestRunner.java
Update the tags = "@BookingAPI" and run using command mvn clean install.
To execute specific test scenarios :@performschemavalidation , @EndToEndCRUD , @EditBooking , @BookingStub etc..
---
## CRUD Operations 
- Covers Create, Retrieve, Update, and Delete booking scenarios.
- Validates both positive flows (e.g., successful creation/deletion) and negative validations (e.g., invalid input, missing fields).
- Ensures proper response codes and payload structures for each operation.
##  Webhook & WireMock Integration

- WireMock is used to simulate and validate webhook responses for booking events.
- WireMock starts on port `9090`.
- Supported Mock Tests:
  - Webhook notification trigger and response validation.
  - Mock booking POST calls and response body validations.
  - Deleting mocked bookings using stubbed DELETE requests.

## 🧾 JSON Schema Validation

The framework validates the response from booking API against the defined JSON Schema: `getbookingresponseschema.json` under `src/test/resources/schemas`.

## 📌 Notes

- **WireMock** is conditionally initialized based on test tag `@webhooktests`.
- **Swagger Schema Validation** helps contract testing by comparing API responses to a predefined schema.
- You can run individual or grouped tests using tags to streamline test execution.
- Ensure the endpoint URLs and schema paths are correctly configured in the `properties` file.
