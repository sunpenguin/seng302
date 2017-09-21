package seng302.team18.visualiser.interpret.americascup;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.ClientRace;

import java.util.function.Consumer;


/**
 * Interpreter to check if a race has been completed.
 */
public class RaceStatusInterpreter extends MessageInterpreter {

    private final ClientRace race;
    private final Consumer<Boolean> onRaceFinish;
    private final Consumer<Boolean> onRaceStart;

    /**
     * Constructor for a RaceStatusInterpreter.
     *
     * @param race         the race
     * @param onRaceFinish callback on race finishing
     * @param onRaceStart  callback on race starting
     */
    public RaceStatusInterpreter(ClientRace race, Consumer<Boolean> onRaceFinish, Consumer<Boolean> onRaceStart) {
        this.race = race;
        this.onRaceFinish = onRaceFinish;
        this.onRaceStart = onRaceStart;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            RaceStatus statusCode = RaceStatus.from(statusMessage.getRaceStatus());

            boolean isChangedStatus = !race.getStatus().equals(statusCode);
            race.setStatus(statusCode);
            if (isChangedStatus) {
                switch (statusCode) {
                    case STARTED:
                        onRaceStart.accept(true);
                        break;
                    case FINISHED:
                        onRaceFinish.accept(true);
                        break;
                }
            }
        }
    }
}
