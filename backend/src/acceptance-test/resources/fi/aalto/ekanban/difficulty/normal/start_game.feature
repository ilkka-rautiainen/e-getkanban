Feature: Start Game
  As a game player
  I want to start a fresh game when entering site
  So that I can practice for board version of kanban game

  Scenario: Player starts the game with normal difficulty
    Given I enter player name as Player
      And I choose Normal difficulty

    When I press start game

    Then I should get a new game
      And game should have player name as Player
      And game should have current day of 1
      And game should have difficulty of Normal
      And game should include a board
        And board should include 1 backlog deck
          And backlog deck should have 15 cards
            And each card should contain phase points
              And 1. phase is Analysis
              And 2. phase is Development
              And 3. phase is Test
            And each card should have empty day started
            And each card should have empty day deployed
        And board should include different phases
          And 1. phase in board is Analysis
            And the phase Analysis should have 2 column(s)
              And 1. column in phase Analysis is In Progress
              And 2. column in phase Analysis is Waiting for Development
          And 2. phase in board is Development
            And the phase Development should have 2 column(s)
              And 1. column in phase Development is In Progress
              And 2. column in phase Development is Waiting for Test
          And 3. phase in board is Test
            And the phase Test should have 1 column(s)
          And 4. phase in board is Deployed
            And the phase Deployed should have 1 column(s)


