Feature: Receiving boat action messages from controllers.


#  Background:
#    Given a player has connected to the mock


  Scenario: Receiving upwind messages
    Given a player has connected to the mock
    When the mock gets sent an upwind message
    Then the players boat will head upwind.


  Scenario: Receiving downwind messages
    Given a player has connected to the mock
    When the mock gets sent a downwind message
    Then the players boat will head downwind.


  Scenario: Receiving sail in / out messages
    Given a player has connected to the mock
    When the mock gets sent a sail in message
    Then the players boat will have its sails in.


  Scenario: Receiving autopilot messages
    Given a player has connected to the mock
    And the boat is heading directly upwind.
    When the mock gets sent an autopilot message.
    Then the players boat will have its heading and speed changed to the optimal upwind vmg.