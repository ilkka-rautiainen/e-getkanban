Feature: Play Turn on Advanced level
  As a game player
  I want to play a turn doing all the possible actions myself
  So that I can see how they affect the game and its entities day by day

  Scenario Outline: Player tries to draw from backlog when 3-day cycle is enabled
    Given I have a game with difficulty of Advanced
      And In Progress column of Analysis has <inProgAnalysisCards> cards
      And Waiting for Development column of Analysis has <waitingForTestCards> cards
      And the current day of the game is <currentDay>

    When I change WIP limit of phase Analysis to 5
      And I change WIP limit of phase Development to 0
      And I press the next round button

    Then game should have current day of <nextDay>
      And Analysis phase should contain <analysisCardCount> cards

    Examples:
      | currentDay | inProgAnalysisCards | waitingForTestCards | nextDay | analysisCardCount |
      | 0          |  0                  |  0                  |  1      | 5                 |
      | 1          |  1                  |  0                  |  2      | 1                 |

  @focus
  Scenario Outline: Player tries to deploy a card when 3-day cycle is enabled
    Given I have a game with difficulty of Advanced
      And Deployed phase has 0 cards
      And Test phase has 1 cards
      And the current day of the game is <currentDay>

    When I assign the first resource dice of Test phase to the first card in Test phase
      And I assign the second resource dice of Test phase to the first card in Test phase
      And I assign the first resource dice of Development phase to the first card in Test phase
      And I assign the second resource dice of Development phase to the first card in Test phase
      And I assign the third resource dice of Development phase to the first card in Test phase
      And I press the next round button

    Then game should have current day of <nextDay>
      And Deployed phase should contain <deployedCardCount> cards

    Examples:
      | currentDay |  nextDay | deployedCardCount |
      | 5          |   6      |   0               |
      | 6          |   7      |   1               |

  @ignore
  Scenario: Player plays the first turn of the game without any special options
    Given I have a new game with difficulty of Advanced
    When I draw as many cards to the first column as the WIP-limit allows
      And I press the next round button
    Then I have a game with difficulty of Advanced
      And I receive an event card for the first day

  @ignore
  Scenario: Player wants to be able to move cards to the cycled phases on the day 3 when the cycles are enabled
    Given I have a new game with difficulty of Advanced
      And the game has the option of the 3 days cycles between getting new cards from backlog to the Selected-phase
      And the game has the option of the 3 days cycles between deploying cards from Testing-phase done-column to Deployed-phase
      And game should have current day of 2

    When I press the next round button

    Then I should get a game with a turn played
      And game should have current day of 3
      And moving cards to the phase Selected is enabled
      And moving cards to the phase Deployed is enabled

  @ignore
  Scenario: Player wants not to be able to move cards to the cycled phases on the day 4 when the cycles are enabled
    Given I have a new game with difficulty of Advanced
      And the game has the option of the 3 days cycles between getting new cards from backlog to the Selected-phase
      And the game has the option of the 3 days cycles between deploying cards from Testing-phase done-column to Deployed-phase
      And game should have current day of 3

    When I press the next round button

    Then I should get a game with a turn played
      And game should have current day of 4
      And moving cards to the phase Selected is disabled
      And moving cards to the phase Deployed is disabled

  # TODO: scenarios for how the expedite lane should work, e.g. expedited cards also affect the CFD

  # TODO: scenarios for how the financial summary chart should work

