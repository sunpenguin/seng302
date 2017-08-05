Feature: Receiving boat action messages from controllers

  Background: A player wants to move their boat in some way
    Given a player has connected to the mock


  Scenario: Receiving upwind messages
    When the mock gets sent an upwind message
    Then the players boat will head upwind


  Scenario: Receiving downwind messages
    When the mock gets sent a downwind message
    Then the players boat will head downwind


  Scenario: Receiving sail in / out messages
    When the mock gets sent a sail in message
    Then the players boat will have its sails in


  Scenario: Receiving autopilot messages
    And the boat is heading directly upwind
    When the mock gets sent an autopilot message
    Then the players boat will have its heading and speed changed to the optimal upwind vmg


  Scenario: Receiving tack messages
    And the boat is heading upwind
    When the mock gets sent a tack / gybe message
    Then the players boat will tack


  Scenario: Receiving gybe messages
    And the boat is heading downwind
    When the mock gets sent a tack / gybe message
    Then the players boat will gybe