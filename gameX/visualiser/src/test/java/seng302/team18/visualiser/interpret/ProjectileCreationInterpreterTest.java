package seng302.team18.visualiser.interpret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileCreationMessage;
import seng302.team18.visualiser.ClientRace;

/**
 * Class to test projectile creation interpreter
 */
public class ProjectileCreationInterpreterTest {
    private ClientRace race;
    private MessageInterpreter interpreter;
    private ProjectileCreationMessage message1;
    private ProjectileCreationMessage message2;
    private ProjectileCreationMessage message3;


    @Before
    public void setUp() {

        race = new ClientRace();
        interpreter = new ProjectileCreationInterpreter(race);
        message1 = new ProjectileCreationMessage(300);
        message2 = new ProjectileCreationMessage(301);
        message3 = new ProjectileCreationMessage(302);
    }


    /**
     * Test to add 1 projectile
     */
    @Test
    public void add1Test(){
        int initsize = race.getProjectiles().size();
        interpreter.interpret(message1);
        Assert.assertEquals(initsize +1, race.getProjectiles().size());
        Assert.assertEquals(message1.getProjectile_id(), race.getProjectiles().get(0).getId());
    }
    /**
     * Test to add multiple projectile
     */
    @Test
    public void addMultipleTest(){
        int initsize = race.getProjectiles().size();
        interpreter.interpret(message1);
        interpreter.interpret(message2);
        interpreter.interpret(message3);
        Assert.assertEquals(initsize +3, race.getProjectiles().size());
        Assert.assertEquals(message1.getProjectile_id(), race.getProjectiles().get(0).getId());
        Assert.assertEquals(message2.getProjectile_id(), race.getProjectiles().get(1).getId());
        Assert.assertEquals(message3.getProjectile_id(), race.getProjectiles().get(2).getId());
    }

}
