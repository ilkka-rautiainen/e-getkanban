Feature: Start Game on Medium level
  As a game player
  I want to start a fresh game when entering site
  So that I can practice for board version of kanban game

  Scenario: Player starts the game with medium difficulty
    Given I enter player name as Halosen Tarja
      And I choose Medium difficulty

    When I press start game

    Then I should get a new game
      And game should have player name as Halosen Tarja
      And game should have current day of 0
      And game should have difficulty of Medium
