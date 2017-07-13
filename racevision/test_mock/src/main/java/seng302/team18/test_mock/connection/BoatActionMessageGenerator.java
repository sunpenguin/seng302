package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.Boat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2017/7/13.
 */
public class BoatActionMessageGenerator extends ScheduledMessageGenerator {
    private Boat boat;


    /**
     * Constructor used by subclasses.
     */
    public BoatActionMessageGenerator(Boat boat) {
        super(5, AC35MessageType.BOAT_ACTION.getCode());
        this.boat = boat;
    }


    @Override
    protected byte[] getPayload() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte autopilot = checkAutoPilot(boat);
        byte sailsIn = checkSailIn(boat);
        byte sailsOut = checkSailOut(boat);
        byte tackGybe = checkTackGybe(boat);
        byte upWind = checkUpWind(boat);
        byte downWind = checkDownWind(boat);

        outStream.write(autopilot);
        outStream.write(sailsIn);
        outStream.write(sailsOut);
        outStream.write(tackGybe);
        outStream.write(upWind);
        outStream.write(downWind);

        return outStream.toByteArray();
    }


    private byte checkDownWind(Boat boat) {
        if (boat.isDownWind()) {
            return 1;
        }
        return 0;
    }

    private byte checkUpWind(Boat boat) {
        if (boat.isUpWind()) {
            return 1;
        }
        return 0;
    }

    private byte checkTackGybe(Boat boat) {
        if (boat.isTackGybe()) {
            return 1;
        }
        return 0;
    }

    private byte checkSailOut(Boat boat) {
        if (boat.isSailOut()) {
            return 1;
        }
        return 0;
    }

    private byte checkSailIn(Boat boat) {
        if (!boat.isSailOut()) {
            return 1;
        }
        return 0;
    }

    private byte checkAutoPilot(Boat boat) {
        if (boat.isAutoPilot()) {
            return 1;
        }
        return 0;
    }
}
