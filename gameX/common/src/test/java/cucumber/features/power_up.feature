Feature: Power Ups

  Background: A power up exists in the current race
    Given a race has started
    And a power up is in the race


  Scenario: Power Up expires
    When a power up expires
    Then the power up will disappear from the race.


  Scenario: Power Ups recreated
    When a power up expires
    Then another power up will replace it.