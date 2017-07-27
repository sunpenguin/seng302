package seng302.team18.racemodel.connection;

import org.junit.Test;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.racemodel.model.*;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.SpeedConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for BoatMessageGenerator.
 */
public class BoatMessageGeneratorTest {

    final double BYTE_COORDINATE_TO_DOUBLE = 180.0 / 2147483648.0;
    final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;

    private final int VERSIONNUM_I = 0;
    private final int VERSIONNUM_L = 1;
    private final int TIME_I = 1;
    private final int TIME_L = 6;
    private final int SOURCE_ID_I = 7;
    private final int SOURCE_ID_L = 4;
    private final int SEQUENCE_NUM_I = 11;
    private final int SEQUENCE_NUM_L = 4;
    private final int DEVICE_TYPE_I = 15;
    private final int DEVICE_TYPE_L = 1;
    private final int LATITUDE_I = 16;
    private final int LATITUDE_L = 4;
    private final int LONGITUDE_I = 20;
    private final int LONGITUDE_L = 4;
    private final int ALTITUDE_I = 24;
    private final int ALTITUDE_L = 4;
    private final int HEADING_I = 28;
    private final int HEADING_L = 2;
    private final int PITCH_I = 30;
    private final int PITCH_L = 2;
    private final int ROLL_I = 32;
    private final int ROLL_L = 2;
    private final int SPEED_I = 34;
    private final int SPEED_L = 2;
    private final int COG_I = 36;
    private final int COG_L = 2;
    private final int SOG_I = 38;
    private final int SOG_L = 2;
    private final int WIND_I = 40;
    private final int WIND_L = 10;
    private final int DRIFT_I = 50;
    private final int DRIFT_L = 2;
    private final int SET_I = 52;
    private final int SET_L = 2;
    private final int RUDDER_I = 54;
    private final int RUDDER_L = 2;


    @Test
    public void boatMessageGeneratorTest() throws IOException {
        AbstractRaceBuilder raceBuilder = new RaceBuilder1();
        AbstractRegattaBuilder regattaBuilder = new RegattaBuilder1();
        AbstractCourseBuilder courseBuilder = new CourseBuilder1();

        final Race testRace = raceBuilder.buildRace(regattaBuilder.buildRegatta(), courseBuilder.buildCourse());
        byte[] generatedBytes;
        List<BoatMessageGenerator> messages = new ArrayList<>();
        for (Boat boat : testRace.getStartingList()) {
            messages.add(new BoatMessageGenerator(boat));
        }
        for (BoatMessageGenerator generator : messages) {
            generatedBytes = generator.getPayload();
            int expectedVersionNum = 1;
            int expectedSourceID = generator.getBoat().getId();
            double expectedLat = (generator.getBoat().getCoordinate().getLatitude());
            double expectedLong = (generator.getBoat().getCoordinate().getLongitude());
            double expectedHeading = generator.getBoat().getHeading();
            double expectedSpeed = generator.getBoat().getSpeed();

            int actualVersionNum = ByteCheck.byteToInt(generatedBytes,
                    VERSIONNUM_I, VERSIONNUM_L);
            int actualSourceID = ByteCheck.byteToInt(generatedBytes,
                    SOURCE_ID_I, SOURCE_ID_L);
            double actualLat = ByteCheck.byteToInt(generatedBytes,
                    LATITUDE_I, LATITUDE_L) * BYTE_COORDINATE_TO_DOUBLE;
            double actualLong = ByteCheck.byteToInt(generatedBytes,
                    LONGITUDE_I, LONGITUDE_L) * BYTE_COORDINATE_TO_DOUBLE;
            double actualHeading = ByteCheck.byteToInt(generatedBytes,
                    HEADING_I, HEADING_L) * BYTE_HEADING_TO_DOUBLE;
            double actualSpeed = new SpeedConverter().mmsToKnots(ByteCheck.byteToInt(generatedBytes,
                    SOG_I, SOG_L));

            assertEquals(expectedVersionNum, actualVersionNum);
            assertEquals(expectedSourceID, actualSourceID);
            assertEquals(expectedLat, actualLat, 0.01);
            assertEquals(expectedLong, actualLong, 0.01);
            assertEquals(expectedHeading, actualHeading, 0.01);
            assertEquals(expectedSpeed, actualSpeed, 0.01);
        }

    }
}
