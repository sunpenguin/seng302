package seng302.team18.visualiser.interpret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.ProjectileCreationMessage;
import seng302.team18.message.ProjectileGoneMessage;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Projectile;
import seng302.team18.model.TigerShark;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.interpret.unique.ProjectileGoneInterpreter;

/**
 * Classs to test the projectile gone interpreter
 */
public class ProjectileGoneInterpreterTest {
    private ClientRace race;
    private MessageInterpreter interpreter;
    private ProjectileGoneMessage message1;
    private ProjectileGoneMessage message2;
    private ProjectileGoneMessage message3;
    private ProjectileGoneMessage message4;
    private ProjectileCreationMessage message5;


    @Before
    public void setUp() {

        race = new ClientRace();
        interpreter = new ProjectileGoneInterpreter(race);
        race.addProjectile(new TigerShark(300, new Coordinate(10,10), 5));
        race.addProjectile(new TigerShark(301, new Coordinate(10,10), 5));
        race.addProjectile(new TigerShark(302, new Coordinate(10,10), 5));
        message1 = new ProjectileGoneMessage(300);
        message2 = new ProjectileGoneMessage(301);
        message3 = new ProjectileGoneMessage(302);
        message4 = new ProjectileGoneMessage(320);
        message5 = new ProjectileCreationMessage( 301);
    }


    /**
     * Test to remove 1 projectile from the race
     */
    @Test
    public void remove1Test() {
        //Check its in the race
        boolean inRace = false;
        for(Projectile projectile: race.getProjectiles()){
            if (projectile.getId() == message1.getId()) {
                inRace = true;
            }
        }
        Assert.assertTrue(inRace);

        //Check the number of projectiles has decreased
        int initsize = race.getProjectiles().size();
        interpreter.interpret(message1);
        Assert.assertEquals(initsize -1, race.getProjectiles().size());

        //Check its no longer in the race
        inRace = false;
        for(Projectile projectile: race.getProjectiles()){
            if (projectile.getId() == message1.getId()) {
                inRace = true;
            }
        }
        Assert.assertFalse(inRace);

    }

    /**
     * Test to remove 1 projectile from the race
     */
    @Test
    public void removeMultipleTest() {
        interpreter.interpret(message1);
        //Check its no longer in the race
        boolean inRace = false;
        for(Projectile projectile: race.getProjectiles()){
            if (projectile.getId() == message1.getId()) {
                inRace = true;
            }
        }
        Assert.assertFalse(inRace);

        interpreter.interpret(message2);
        inRace = false;
        for(Projectile projectile: race.getProjectiles()){
            if (projectile.getId() == message2.getId()) {
                inRace = true;
            }
        }
        Assert.assertFalse(inRace);

        interpreter.interpret(message3);
        inRace = false;
        for(Projectile projectile: race.getProjectiles()){
            if (projectile.getId() == message3.getId()) {
                inRace = true;
            }
        }

        Assert.assertFalse(inRace);
        Assert.assertEquals(0, race.getProjectiles().size());

    }


    /**
     * Test for projectile id not in race
     */
    @Test
    public void wrongIdTest() {
        int startSize = race.getProjectiles().size();
        interpreter.interpret(message4);
        Assert.assertEquals(startSize, race.getProjectiles().size());
    }


    /**
     * Test for wrong type of message given
     */
    @Test
    public void wrongMessageTypeTest() {
        int startSize = race.getProjectiles().size();
        interpreter.interpret(message5);
        Assert.assertEquals(startSize, race.getProjectiles().size());
    }
}
