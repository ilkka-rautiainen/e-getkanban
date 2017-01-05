Feature: Play Turn on Normal level
  As a game player
  I want to play a turn after possibly adjusting wip limits
  So that I can see the result of different AI actions (assign&use resources, move cards and draw from backlog AIs) and the Cumulative Flow Diagram (CFD-diagram)


  Scenario: Player changes the wip limits and plays the turn with almost done cards
    Given I have a game with difficulty of Normal
      And game has one almost done card in first columns of the work phases

    When I change WIP limit of phase Analysis to 1
      And I change WIP limit of phase Development to 1
      And I change WIP limit of phase Test to 1
      And I press the next round button

    Then I should get a game with a turn played
      And game should have phase Analysis with WIP limit 1
      And game should have phase Development with WIP limit 1
      And game should have phase Test with WIP limit 1
      And the card in each work phase's first column has been worked on
      And the card in each work phase's first column has been moved to next phase
      And new cards should have been drawn from backlog to the first column


  Scenario: Player plays the turn with ready cards
    Given I have a game with difficulty of Normal
      And game has one ready card in first columns of the work phases

    When I press the next round button

    Then I should get a game with a turn played
      And the card in each work phase's first column has been moved to next phase
      And new cards should have been drawn from backlog to the first column


  Scenario: Player doesn't change anything and awaits the wip-limit to remain the same
    Given I have a game with difficulty of Normal
      And it has wip limit of 10 in phase Analysis
      And one turn has already been played

    When I press the next round button

    Then I should get a game with a turn played
      And game should have phase Analysis with WIP limit 10


  Scenario: Player changes wip-limit to less than the amount of cards
    Given I have a game with difficulty of Normal
      And it has wip limit of 10 in phase Analysis
      And one turn has already been played

    When I change WIP limit of phase Analysis to 1
      And I press the next round button

    Then I should get a game with a turn played
      And it has wip limit of 1 in phase Analysis
      And the phase Development should have some cards in its first column
      And the phase Analysis should still have more cards than it's WIP limit


  Scenario: Player tries to give a negative wip-limit value
    Given I have a game with difficulty of Normal
      And game has one ready card in first columns of the work phases

    When I change WIP limit of phase Analysis to -1
      And I press the next round button

    Then I should get an error


  Scenario: Player wants to see the CFD diagram after the first turn
    Given I have a game with difficulty of Normal

    When I press the next round button

    Then I should get a game with a turn played
      And the first column should have some cards drawn from backlog
      And game should include a CFD-diagram
        And the newest records in the CFD-diagram are for day 2
        And the CFD-diagram should show the line of cards entered to the board increased by the amount of cards that entered to the board


  Scenario: Player wants to see the CFD diagram update in the middle of the game
    Given I have a game with difficulty of Normal
      And game has one ready card in first columns of the work phases
      And the current day of the game is 5

    When I press the next round button

    Then I should get a game with a turn played
      And game should include a CFD-diagram
        And the newest records in the CFD-diagram are for day 6
        And the CFD-diagram should increase the line of cards entered the board by 1
        And the CFD-diagram should increase the line of cards passed the phase Analysis by 1
        And the CFD-diagram should increase the line of cards passed the phase Development by 1
        And the CFD-diagram should increase the line of cards deployed by 1


  Scenario: Player wants to see the lead time of the cards
    Given I have a game with difficulty of Normal
      And game has one ready card in first columns of the work phases
      And the current day of the game is 5

    When I press the next round button

    Then I should get a game with a turn played
      And one card should have been deployed
      And the card that was deployed should have a lead time calculated on it and it's 5
