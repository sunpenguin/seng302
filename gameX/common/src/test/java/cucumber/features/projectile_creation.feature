Feature: Projectile Creation

  Background: A boat is in a race
    Given a race with projectiles
    And two boats
    And the first boat has a tiger shark power up

  Scenario: The boat fires one projectile after another
    When the boat fires the projectile
    And picks up another projectile
    And fires the second projectile
    Then the the first projectiles id will differ form the second projectiles id

  Scenario: The boat fires the projectile out of bounds
    When the boat fires the projectile
    And the projectile leave the boundaries
    Then the projectile will cease to exist

  Scenario: The boat fires the projectile to stun another boat
    When the boat fires the projectile
    And it hits a second boat
    Then the second boat will be stunned
    And the projectile will cease to exist


