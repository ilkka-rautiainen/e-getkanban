Feature: Game controller

  Scenario: Get all games
    Given I have first 2 game(s)
    When I call the action get games
    Then response status code should be 200
      And I should get 2 game(s) in response
