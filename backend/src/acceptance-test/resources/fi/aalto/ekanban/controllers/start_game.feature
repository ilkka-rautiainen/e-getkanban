Feature: Start Game
  As a game player
  I want to start a fresh game when entering site
  So that I can practice for board version of kanban game

  Scenario: Player starts the game with normal difficulty
    Given I enter player name as Player
    Given I choose normal difficulty

    When I press start game

    Then I should get a new game
      And game should have player name as Player
      And game should include a board
        And board should include 1 backlog deck
          And backlog deck should have 50 cards
            And each card should contain phase points
              And 1. phase is Analysis
              And 2. phase is Dev
              And 3. phase is Test
            And each card should have empty day started
            And each card should have empty day deployed
            And each card should have financial value
        And board should include event card deck
          And event card deck should have 15 event cards
        And board should include different phases
          And 1. phase is Analysis
            And Analysis should have two columns
              And first column is In Progress
              And second column is Done
          And 2. phase is Development
            And Development should have two columns
              And first column is In Progress
              And second column is Done
          And 3. phase is Test
            And Test should have one column
          And 4. phase is Deployed
            And Deployed should have one column


