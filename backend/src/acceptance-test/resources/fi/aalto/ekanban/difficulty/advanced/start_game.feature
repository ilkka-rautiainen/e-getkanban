Feature: Start Game on Advanced level
  As a game player
  I want to start a fresh game when entering site
  So that I can practice full version of kanban game


  Scenario: Player starts an advanced game with no special options
    Given I enter player name as Kekkosen Urkki
      And I choose Advanced difficulty

    When I press start game

    Then I should get a new game
      And game should have player name as Kekkosen Urkki
      And game should have difficulty of Advanced
      And game should include a board
        And board should include different phases
          And 1. phase in board is Analysis
            And In Progress column of Analysis should allow new cards to be drawn in every third day
            And Waiting for Development column of Analysis should allow new cards to be drawn in every day
          And 2. phase in board is Development
            And In Progress column of Development should allow new cards to be drawn in every day
            And Waiting for Test column of Development should allow new cards to be drawn in every day
          And 3. phase in board is Test
            And column of Test should allow new cards to be drawn in every day
          And 4. phase in board is Deployed
            And column of Deployed should allow new cards to be drawn in every third day

  @ignore
  Scenario: Player starts an advanced game with no special options
    Given I enter player name as Kekkosen Urkki
      And I choose Advanced difficulty

    When I press start game

    Then I should get a new game
      And game should have player name as Kekkosen Urkki
      And game should have current day of 0
      And game should have difficulty of Advanced
      And game should include a financial summary chart
      And game should include a board
        And the cards on the board should have financial values
        And board should include different phases
          And 1. phase in board is Analysis
          And 2. phase in board is Development
          And 3. phase in board is Test
            And the phase Test should have 1 column(s)
          And 4. phase in board is Deployed
      And board should include colored track lines
        And there should be a track line for the cards entered the board
        And there should be a track line for the cards passed the phase Analysis
        And there should be a track line for the cards passed the phase Development
        And there should be a track line for the cards that have been deployed
      And game should include a CFD-diagram
        And the CFD-diagram should include a line for the cards passed the track line of phase Analysis
        And the CFD-diagram should include a line for the cards passed the track line of phase Development
        And the CFD-diagram should include a line for the cards passed the track line of phase Deployed

  @ignore
  Scenario: Player starts the game with advanced difficulty with the cycles
    Given I enter player name as Kekkosen Urkki
      And I choose Advanced difficulty
      And I choose to have the 3 days cycles between getting new cards from backlog to the Selected-phase
      And I choose to have the 3 days cycles between deploying cards from Testing-phase done-column to Deployed-phase

    When I press start game

    Then I should get a new game
      And game should have current day of 0
      And game should include a board
        And 1. phase in board is Selected
          And the phase Selected should have 1 column(s)
          And moving cards to the phase Selected is enabled
        And 2. phase in board is Analysis
        And 3. phase in board is Development
        And 4. phase in board is Test
          And the phase Test should have 2 column(s)
            And 1. column in phase Test is In Progress
            And 2. column in phase Test is Waiting for being deployed
        And 5. phase in board is Deployed
          And moving cards to the phase Deployed is enabled
      And board should include colored track lines
        And there should be a track line for the cards entered the board
        And there should be a track line for the cards passed the phase Selected
        And there should be a track line for the cards passed the phase Analysis
        And there should be a track line for the cards passed the phase Development
        And there should be a track line for the cards passed the phase Test
        And there should be a track line for the cards that have been deployed
      And game should include a CFD-diagram
        And the CFD-diagram should include a line for the cards passed the track line of phase Selected
        And the CFD-diagram should include a line for the cards passed the track line of phase Analysis
        And the CFD-diagram should include a line for the cards passed the track line of phase Development
        And the CFD-diagram should include a line for the cards passed the track line of phase Test
        And the CFD-diagram should include a line for the cards passed the track line of phase Deployed

  @ignore
  Scenario: Player starts the game with advanced difficulty with the expedite lane
    Given I enter player name as Kekkosen Urkki
      And I choose Advanced difficulty
      And I choose to have the expedite lane

    When I press start game

    Then I should get a new game
      And game should include a board
        And the board should include an expedite lane
        # TO CONSIDER: should expedite lane have phases and columns embedded into it separately? pros: could ease things technically, cons: duplicate structure
