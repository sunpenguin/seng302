Feature: Sending boat actions

  Background: A player is playing the game and wishes to move their boat
    Given a course
    And a boat inside the courses bounds


  Scenario: Stays within bounds
    When the player stays inside the boundary
    Then the players status will stay the same


  Scenario: Player moves out of bounds
    When the player moves outside the boundary
    Then the players status will be set to disqualified.