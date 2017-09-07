package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileGoneMessage;
import seng302.team18.visualiser.ClientRace;

/**
 * Interprets ProjectileGoneMessages.
 */
public class ProjectileGoneInterpreter extends MessageInterpreter {

    ClientRace race;


    /**
     * Constructor for ProjectileGoneInterpreter.
     *
     * @param race to check.
     */
    public ProjectileGoneInterpreter(ClientRace race) {
        this.race = race;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof ProjectileGoneMessage) {
            ProjectileGoneMessage projectileMessage = (ProjectileGoneMessage) message;
            race.removeProjectile(projectileMessage.getId());
        }
    }
}
