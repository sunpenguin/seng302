Feature: Boat Loses Life For leaving Boundary

  Background: A player is playing bumper boats and wishes to move their boat
    Given a bumperBoat race
    And a boat inside the courses bumperBoat race bounds

  Scenario: Stays within bounds
    When the player stays inside the bumperBoat boundary
    Then the players lives will stay the same

  Scenario: Leaves bounds with many lives
    When the player has 3 lives
    And the player moves outside the the bumperBoat boundary
    Then the player will have 2 lives

  Scenario: Leaves bound with 1 lives
    When the player has 1 lives
    And the player moves outside the the bumperBoat boundary
    Then the players bumperBoat status will be set to disqualified.