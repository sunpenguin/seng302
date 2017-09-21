package seng302.team18.visualiser.interpret.unique;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileCreationMessage;
import seng302.team18.model.Coordinate;
import seng302.team18.model.TigerShark;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.util.Procedure;

/**
 * Class to interpret the projectile creation message
 */
public class ProjectileCreationInterpreter extends MessageInterpreter {

    private ClientRace race;
    private Procedure callback;


    /**
     * Constructor for ProjectileGoneInterpreter.
     *
     * @param race     race to check
     * @param callback procedure to run when a projectile is added
     */
    public ProjectileCreationInterpreter(ClientRace race, Procedure callback) {
        this.race = race;
        this.callback = callback;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof ProjectileCreationMessage) {
            ProjectileCreationMessage projectileMessage = (ProjectileCreationMessage) message;
            race.addProjectile(new TigerShark(projectileMessage.getProjectile_id(), new Coordinate(0, 0), 0));
            callback.execute();
        }
    }
}
