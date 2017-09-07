package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileGoneMessage;
import seng302.team18.visualiser.ClientRace;

/**
 * Created by csl62 on 7/09/17.
 */
public class ProjectileGoneInterpreter extends MessageInterpreter{


    ClientRace race;

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
