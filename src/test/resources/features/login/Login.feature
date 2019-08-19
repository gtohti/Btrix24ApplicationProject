@VYT37-107
  Feature:
    As user I can login to the page
  @help_desk
    Scenario: Login as a help desk
    Then user logs in as a help desk staff
    And user verifies that "Activity Stream" button is displayed

  @negative
    Scenario: Verify warning message for invalid credentials
      Then user logs in with "wrong" username and "wrong" password
      And user verifies that "Incorrect login or password" warning message is displayed

  @marketing
  Scenario: Login as marketing
    Then user logs in as a marketing staff
    And user verifies that "Activity Stream" button is displayed

  @hr
  Scenario: Login as HR
    Then user logs in as a HR staff
    And user verifies that "Activity Stream" button is displayed

  @Scenario Outline: login as different user
    Given user logs in as a "<user_type>"
    And user verifies that "<button>" is displayed
    Examples:
      |user_type|   button      |
      |help_desk|Activity Stream|
      |marketing|Activity Stream|
      |hr       |Activity Stream|
