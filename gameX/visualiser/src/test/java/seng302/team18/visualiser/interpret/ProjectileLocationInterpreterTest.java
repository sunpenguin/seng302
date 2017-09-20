package seng302.team18.visualiser.interpret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.ProjectileLocationMessage;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Projectile;
import seng302.team18.model.TigerShark;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.interpret.unique.ProjectileLocationInterpreter;

/**
 * Class to test projectile location interpreter
 */
public class ProjectileLocationInterpreterTest {
    private ClientRace race;
    private MessageInterpreter interpreter;
    private ProjectileLocationMessage message1;
    private ProjectileLocationMessage message2;
    private ProjectileLocationMessage message3;
    private Projectile projectile1;
    private Projectile projectile2;


    @Before
    public void setUp() {
        race = new ClientRace();
        interpreter = new ProjectileLocationInterpreter(race);
        projectile1 = new TigerShark(300, new Coordinate(10,10), 5);
        projectile2 = new TigerShark(301, new Coordinate(10,10), 5);
        race.addProjectile(projectile1);
        race.addProjectile(projectile2);
        message1 = new ProjectileLocationMessage(300, 11,10,60,50);
        message2 = new ProjectileLocationMessage(301,10,10,7,50);
        message3 = new ProjectileLocationMessage(302,60,55, 90, 50);
    }


    /**
     * Test to check for 1 projectile message
     */
    @Test
    public void interpret1Test() {
        Coordinate pro2OldCoord = projectile2.getLocation();
        interpreter.interpret(message1);

        Assert.assertEquals(pro2OldCoord, projectile2.getLocation()); //Assert projectile 2 hasnt moved

        Assert.assertEquals(message1.getLocation(),projectile1.getLocation());
        Assert.assertEquals(message1.getSpeed(), projectile1.getSpeed(), 0);
        Assert.assertEquals(message1.getHeading(), projectile1.getHeading(), 0);

    }


    /**
     * Test to check for multiple projectile messages
     */
    @Test
    public void interpretMultipleTest() {
        Coordinate pro2OldCoord = projectile2.getLocation();

        interpreter.interpret(message1);
        interpreter.interpret(message2);

        Assert.assertEquals(pro2OldCoord, projectile2.getLocation()); //Assert projectile 2 hasn't moved

        Assert.assertEquals(message1.getLocation(),projectile1.getLocation());
        Assert.assertEquals(message1.getSpeed(), projectile1.getSpeed(), 0);
        Assert.assertEquals(message1.getHeading(), projectile1.getHeading(), 0);

        Assert.assertEquals(message2.getLocation(),projectile2.getLocation());
        Assert.assertEquals(message2.getSpeed(), projectile2.getSpeed(), 0);
        Assert.assertEquals(message2.getHeading(), projectile2.getHeading(), 0);

    }


    /**
     * Test to check message with wrong id
     */
    @Test
    public void wrongIdTest() {
        Coordinate pro1OldCoord = projectile1.getLocation();
        Coordinate pro2OldCoord = projectile2.getLocation();

        interpreter.interpret(message3);

        Assert.assertEquals(pro1OldCoord, projectile1.getLocation());
        Assert.assertEquals(pro2OldCoord, projectile2.getLocation());
    }
}
