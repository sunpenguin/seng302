package seng302.team18.visualiser.interpret.unique;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileCreationMessage;
import seng302.team18.model.Coordinate;
import seng302.team18.model.TigerShark;
import seng302.team18.visualiser.ClientRace;

/**
 * Class to interpret the projectile creation message
 */
public class ProjectileCreationInterpreter extends MessageInterpreter{

    ClientRace race;


    /**
     * Constructor for ProjectileGoneInterpreter.
     *
     * @param race to check.
     */
    public ProjectileCreationInterpreter(ClientRace race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof ProjectileCreationMessage) {
            ProjectileCreationMessage projectileMessage = (ProjectileCreationMessage) message;
            race.addProjectile(new TigerShark(projectileMessage.getProjectile_id(), new Coordinate(0,0),0));
        }
    }
}
