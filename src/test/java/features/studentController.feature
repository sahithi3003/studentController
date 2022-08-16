Feature: Student feature
  Background: Details
    Given user details

  Scenario: Verify that user details are displayed
    When creating a user
    Then user details displayed

   Scenario: Verify that single user details are displayed
     When creating a user
     Then single user displayed

  Scenario: Verify that user is being created
    When creating a user
    Then user must be created

  Scenario: Verify that user updated successfully
    When creating a user
    And updating user
    Then user details updated

  Scenario: Verify that user deleted successfully
    When creating a user
    Then user deleted

  Scenario: Verify that name field is required
    When creating without name
    Then error thrown

  Scenario: Verify that email is required
    When creating without email
    Then email error thrown

  Scenario: Verify that Age field is required
    When creating without age
    Then age error thrown

  Scenario: Verify end point is required
    When creating a user
    And updating without endpoint
    Then method error

  Scenario: Verify deletion end point is required
    When creating a user
    And deleting user
    Then method error

  Scenario: Verify that id is required
    When creating without id
    Then Internal error thrown

  Scenario: Verify that duplicate deletion is not possible
    When creating a user
    And user deleted
    And duplicate delete
    Then Internal error thrown

