Feature: Send boat actions.

  Background:
    Given the user has connected to the game.


  Scenario: Sending upwind action
    When the upwind key is pressed.
    Then the message will be sent.


  Scenario: Sending downwind action
    When the downwind key is pressed.
    Then the message will be sent.


  Scenario: Sending sail in / out action.
    When the sail in / out key is pressed.
    Then the message will be sent.


  Scenario: Sending autopilot action.
    When the autopilot key is pressed.
    Then the message will be sent.