package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileLocationMessage;
import seng302.team18.visualiser.ClientRace;

/**
 * Created by csl62 on 7/09/17.
 */
public class ProjectileLocationInterpreter extends MessageInterpreter {


    private ClientRace race;

    public ProjectileLocationInterpreter(ClientRace race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof ProjectileLocationMessage) {
            ProjectileLocationMessage projectileMessage = (ProjectileLocationMessage) message;
            race.updateProjectile(projectileMessage.getId(), projectileMessage.getLocation(),
                    projectileMessage.getHeading(), projectileMessage.getSpeed());
        }
    }
}
