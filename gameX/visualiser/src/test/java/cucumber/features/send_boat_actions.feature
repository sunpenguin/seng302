Feature: Sending boat actions

  Background: A player is playing the game and wishes to move their boat
    Given the user has connected to the game


  Scenario: Sending upwind action
    When the upwind key is pressed
    Then the message will be sent


  Scenario: Sending downwind action
    When the downwind key is pressed
    Then the message will be sent


  Scenario: Sending sail in / out action
    When the sail in / out key is pressed
    Then the message will be sent


  Scenario: Sending autopilot action
    When the autopilot key is pressed
    Then the message will be sent

  Scenario: Sending tack / gybe action
    When the tack / gybe key is pressed
    Then the message will be sent