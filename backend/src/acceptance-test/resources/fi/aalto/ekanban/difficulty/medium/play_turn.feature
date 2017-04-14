Feature: Play Turn on Medium level
  As a game player
  I want to play a turn and in addition to adjusting wip limits I want to assign resource dice to the cards
  So that I can control by myself which cards are worked on a turn

  Scenario: Player assigns the resource dice to primary phase and plays the turn
    Given I have a game with difficulty of Medium
      And the game has 2 cards in first column of Analysis phase
      And first card needs 15 points in Analysis to be done
      And the game has been played for 1 rounds already so that I've resource dice at my disposal

    When I assign the first resource dice of Analysis phase to the first card in Analysis phase
      And I assign the second resource dice of Analysis phase to the first card in Analysis phase
      And I press the next round button

    Then first card in Analysis phase should have the dice primary values summed in Analysis resource points



  Scenario: Player assigns the resource dice to secondary phases and plays the turn
    Given I have a game with difficulty of Medium
      And the game has 1 cards in first column of Analysis phase
      And the game has 2 cards in first column of Development phase
      And the game has been played for 1 rounds already so that I've resource dice at my disposal

    When I assign the first resource dice of Analysis phase to the first card in Development phase
      And I assign the first resource dice of Development phase to the first card in Analysis phase
      And I change WIP limit of phase Analysis to 1
      And I press the next round button

    Then first card in Analysis phase should have the first secondary dice value from the first die of Development phase
      And first card in Development phase should have the first secondary dice value from the first die of Analysis phase
      And second card in Development has not been worked on
      And In progress column of Analysis should hold only one card


  @focus
  Scenario: Player assigns the resource dice from one phase to two other secondary phases and plays the turn
    Given I have a game with difficulty of Medium
    And the game has 2 cards in first column of Analysis phase
    And the game has 1 cards in first column of Test phase
    And the game has been played for 3 rounds already so that I've resource dice at my disposal

    When I assign the first resource dice of Development phase to the first card in Analysis phase
    And I assign the second resource dice of Development phase to the first card in Test phase
    And I press the next round button

    Then first card in Analysis phase should have the first secondary dice value from the first die of Development phase
    And first card in Test phase should have the second secondary dice value from the second die of Development phase

