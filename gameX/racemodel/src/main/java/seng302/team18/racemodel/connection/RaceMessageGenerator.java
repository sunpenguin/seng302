package seng302.team18.racemodel.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.SpeedConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates race messages in the AC35 protocol.
 */
public class RaceMessageGenerator extends ScheduledMessageGenerator {

    private Race race;

    /**
     * Constructs a new instance of RaceMessageGenerator.
     *
     * @param race Race to generate messages from.
     */
    public RaceMessageGenerator(Race race) {
        super(2, AC35MessageType.RACE_STATUS.getCode());
        this.race = race;
    }

    @Override
    public byte[] getPayload() throws IOException {

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        byte messageVersionNumberBytes = 0x2;

        byte[] currentTimeBytes = ByteCheck.getCurrentTime6Bytes();

        byte[] raceIDBytes = ByteCheck.intToByteArray(race.getId());

        byte raceStatusByte = (byte) race.getStatus().getCode();

        long expectedStartTime = race.getStartTime().toInstant().toEpochMilli();

        byte[] expectedStartTimeBytes = ByteCheck.longTo6ByteArray(expectedStartTime); // TODO: Use a reasonable starting time

        double WIND_DIRECTION_CONVERTER = 65536.0 / 360.0;
        int windDirection = (int) (race.getCourse().getWindDirection() * WIND_DIRECTION_CONVERTER);
        byte[] raceWindDirectionBytes = ByteCheck.intToUShort(windDirection);
                // Currently set to east TODO: make this a field of race or boat?

        double windSpeed = race.getCourse().getWindSpeed();
        byte[] raceWindSpeedBytes = ByteCheck.shortToByteArray(new SpeedConverter().knotsToMms(windSpeed).shortValue());

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
            byte statusByte = (byte) boat.getStatus().code();

            byte legNumberByte = (byte) boat.getLegNumber();
            byte numPenaltiesAwardedByte = 0; // TODO: Add this field to boat
            byte numPenaltiesServedByte = 0; // TODO: Add this field to boat
            byte[] estTimeAtNextMark = ByteCheck.longTo6ByteArray(11111111111L); // TODO: calculate this value
            byte[] estTimeAtFinish = ByteCheck.longTo6ByteArray(6666666666L); // TODO: calculate this value

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
}