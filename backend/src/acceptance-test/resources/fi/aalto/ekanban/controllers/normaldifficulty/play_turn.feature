Feature: Play Turn
  As a game player
  I want to play a turn after possibly adjusting wip limits
  So that I can see the result of different AI actions

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
      And new cards are drawn from backlog to the first column


  Scenario: Player plays the turn with ready cards
    Given I have a game with difficulty of Normal
      And game has one ready card in first columns of the work phases

    When I press the next round button

    Then I should get a game with a turn played
      And the card in each work phase's first column has been moved to next phase
      And new cards are drawn from backlog to the first column
