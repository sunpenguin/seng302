package seng302.team18.test_mock.connection;

import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by hqi19 on 21/04/17.
 */
public class RaceMessageGenerator extends ScheduledMessageGenerator {

    private Race race;
    private String message;

    public RaceMessageGenerator(Race race) {
        super(2, 12);

        this.race = race;
    }

    @Override
    public byte[] getPayload() throws IOException {

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        byte messageVersionNumberBytes = 0x2;

        byte[] currentTimeBytes = ByteCheck.getCurrentTime6Bytes();

        byte[] raceIDBytes = ByteCheck.intToByteArray(race.getId());

        byte raceStatusByte = race.getStatus();

        byte[] expectedStartTimeBytes = ByteCheck.getCurrentTime6Bytes(); // TODO: Use a reasonable starting time

        byte[] raceWindDirectionBytes = ByteCheck.shortToByteArray((short) 0x4000);
                // Currently set to east TODO: make this a field of race or boat?

        byte[] raceWindSpeedBytes = ByteCheck.shortToByteArray((short) 5000);
                // Currently 18 km/h TODO: make this a field of race or boat?

        byte numBoatsByte = (byte) race.getStartingList().size();

        byte raceTypeByte = 0x2; // Currently set to fleet race TODO: add this to race xml parser + race class

        outputSteam.write(messageVersionNumberBytes);
        outputSteam.write(currentTimeBytes);
        outputSteam.write(raceIDBytes);
        outputSteam.write(raceStatusByte);
        outputSteam.write(expectedStartTimeBytes);
        outputSteam.write(raceWindDirectionBytes);
        outputSteam.write(raceWindSpeedBytes);
        outputSteam.write(numBoatsByte);
        outputSteam.write(raceTypeByte);

        for (Boat boat : race.getStartingList()) {
            byte[] sourceIDBytes = ByteCheck.intToByteArray(boat.getId());
            byte statusByte = 0x2; // TODO: Currently always "racing" need to add this to boat class, update as race goes on
            byte legNumberByte = (byte) boat.getLeg().getLegNumber(); // TODO: Update leg numbers so that 0 is prestart, 1 is first leg and so on
            byte numPenaltiesAwardedByte = 7; // TODO: Add this field to boat
            byte numPenaltiesServedByte = 4; // TODO: Add this field to boat
            byte[] estTimeAtNextMark = ByteCheck.convertLongTo6ByteArray(11111111111L); // TODO: calculate this value
            byte[] estTimeAtFinish = ByteCheck.convertLongTo6ByteArray(6666666666L); // TODO: calculate this value

            outputSteam.write(sourceIDBytes);
            outputSteam.write(statusByte);
            outputSteam.write(legNumberByte);
            outputSteam.write(numPenaltiesAwardedByte);
            outputSteam.write(numPenaltiesServedByte);
            outputSteam.write(estTimeAtNextMark);
            outputSteam.write(estTimeAtFinish);
        }

        return outputSteam.toByteArray();
    }

    private String fixLength(String s, int len) { //TODO move to super class so all subclasses can use this
        StringBuilder str = new StringBuilder(s);
        str.setLength(len);
        return String.valueOf(str);
    }
}