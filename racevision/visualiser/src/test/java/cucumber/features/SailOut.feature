Feature: SailOut
  The user should have the ability to toggle between sail powered up and sail luffing

#  Background:
#    Given I am controling a boat

  Scenario: Putting sail out
    Given the sail is in
    When I press the shift key
    Then a BoatAction message should be sent
    And sailIn should be false

  Scenario: Bringing sail in
    Given the sail is out
    When I press the shift key
    Then a BoatAction message should be sent
    And sailIn should be true

