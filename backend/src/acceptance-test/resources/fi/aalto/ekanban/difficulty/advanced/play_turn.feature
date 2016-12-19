Feature: Play Turn on Advanced level
  As a game player
  I want to play a turn after possibly adjusting wip limits, assigning&using resources, moving cards, drawing cards from backlog and possibly using the expedite lane
  So that I can see the current day's event card and I can follow the CFD and financial summary charts


  Scenario: Player plays the first turn of the game without any special options
    Given I have a new game with difficulty of Advanced
    When I draw as many cards to the first column as the WIP-limit allows
      And I press the next round button
    Then I have a game with difficulty of Advanced
      And I receive an event card for the first day


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

