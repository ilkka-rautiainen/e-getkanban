Feature: Play Turn on Medium level
  As a game player
  I want to play a turn and in addition to adjusting wip limits I want to assign resource dice to the cards in the working phases
  So that I can see the result of the resource usage and different AI actions (move cards & draw from backlog AIs)


  Scenario: Player assigns the resource dice and plays the turn
    Given I have a game with difficulty of Medium
      And the game has all work phases full of in progress cards
      And the game has been played for one round already so that I've resource dice at my disposal

    When I assign the resource dice to the first cards in each phase
      And I press the next round button

    Then I have a game with difficulty of Medium
      And the cards that I assigned resources on have been worked on with the amount of the dice value
      And no other cards have been worked on than the ones that received the dice from me
