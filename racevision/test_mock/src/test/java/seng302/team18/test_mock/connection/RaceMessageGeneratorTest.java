package seng302.team18.test_mock.connection;

import org.junit.Test;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.test_mock.TestMock;
import seng302.team18.util.ByteCheck;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by jth102 on 25/04/17.
 */
public class RaceMessageGeneratorTest {
    private final int VERSION_L = 1;
    private final int VERSION_P = 0;
    private final int CURRENT_TIME_L = 6;
    private final int CURRENT_TIME_P = 1;
    private final int RACE_ID_L = 4;
    private final int RACE_ID_P = 7;
    private final int RACE_STATUS_L = 1;
    private final int RACE_STATUS_P = 11;
    private final int EXP_START_L = 6;
    private final int EXP_START_P = 12;
    private final int WIND_DIR_L = 2;
    private final int WIND_DIR_P = 18;
    private final int WIND_SPEED_L = 2;
    private final int WIND_SPEED_P = 20;
    private final int NUM_BOATS_L = 1;
    private final int NUM_BOATS_P = 22;
    private final int RACE_TYPE_L = 1;
    private final int RACE_TYPE_P = 23;
    private final int SOURCE_ID_L = 4;
    private final int SOURCE_ID_P = 24;
    private final int BOAT_STATUS_L = 1;
    private final int BOAT_STATUS_P = 28;
    private final int LEG_NUM_L = 1;
    private final int LEG_NUM_P = 29;
    private final int NUM_PEN_AWARDED_L = 1;
    private final int NUM_PEN_AWARDED_P = 30;
    private final int NUM_PEN_SERVED_L = 1;
    private final int NUM_PEN_SERVED_P = 31;
    private final int EST_TIME_NEXT_MARK_L = 6;
    private final int EST_TIME_NEXT_MARK_P = 32;
    private final int EST_TIME_FINISH_L = 6;
    private final int EST_TIME_FINISH_P = 38;


    @Test
    public void raceStatusMessageTest() {
        TestMock testMock = new TestMock();
        Race testRace = testMock.testRun();
        byte[] generatedBytes;

        RaceMessageGenerator generator = new RaceMessageGenerator(testRace);
        try {
            generatedBytes = generator.getPayload();

            // NOTE: If tests are failing here, it may be because things that are hard coded and not received from testRace
            // may be able to be received from testRace now. Eg windDirection will be a part of the race in the future.

            int expectedMsgVersion = 2;
            long expectedCurrentTime = System.currentTimeMillis();
            int expectedRaceID = testRace.getId();
            int expectedRaceStatus = testRace.getStatus();
            long expectedStartTime = System.currentTimeMillis();
            int expectedWindDirection = 0x4000;
            int expectedWindSpeed = 5000;
            int expectedNumBoats = testRace.getStartingList().size();
            int expectedRaceType = 2;

            int actualMsgVersion = ByteCheck.byteToIntConverter(generatedBytes, VERSION_P, VERSION_L);
            long actualCurrentTime = ByteCheck.byteToLongConverter(generatedBytes, CURRENT_TIME_P, CURRENT_TIME_L);
            int actualRaceID = ByteCheck.byteToIntConverter(generatedBytes, RACE_ID_P, RACE_ID_L);
            int actualRaceStatus = ByteCheck.byteToIntConverter(generatedBytes, RACE_STATUS_P, RACE_STATUS_L);
            long actualStartTime = ByteCheck.byteToLongConverter(generatedBytes, EXP_START_P, EXP_START_L);
            int actualWindDirection = ByteCheck.byteToIntConverter(generatedBytes, WIND_DIR_P, WIND_DIR_L);
            int actualWindSpeed = ByteCheck.byteToIntConverter(generatedBytes, WIND_SPEED_P, WIND_SPEED_L);
            int actualNumBoats = ByteCheck.byteToIntConverter(generatedBytes, NUM_BOATS_P, NUM_BOATS_L);
            int actualRaceType = ByteCheck.byteToIntConverter(generatedBytes, RACE_TYPE_P, RACE_TYPE_L);

            assertEquals(expectedMsgVersion, actualMsgVersion);
            assertEquals(expectedCurrentTime, actualCurrentTime, 10);
            assertEquals(expectedRaceID, actualRaceID);
            assertEquals(expectedRaceStatus, actualRaceStatus);
            assertEquals(expectedStartTime, actualStartTime, 10);
            assertEquals(expectedWindDirection, actualWindDirection);
            assertEquals(expectedWindSpeed, actualWindSpeed);
            assertEquals(expectedNumBoats, actualNumBoats);
            assertEquals(expectedRaceType, actualRaceType);

            int loopCount = 0;
            final int LOOP_OFFSET = 20; // move 20 bytes forward for each boat being read.

            for (Boat boat : testRace.getStartingList()) {
                int expectedBoatID = boat.getId();
                int expectedBoatStatus = 2;
                int expectedLegNum = boat.getBoatLegNumber();
                int expectedPenAwarded = 7;
                int expectedPenServed = 4;
                long expectedTimeMark = 11111111111L;
                long expectedTimeFinish = 6666666666L;

                int actualBoatID = ByteCheck.byteToIntConverter(generatedBytes,
                        SOURCE_ID_P + (LOOP_OFFSET * loopCount), SOURCE_ID_L);
                int actualBoatStatus = ByteCheck.byteToIntConverter(generatedBytes,
                        BOAT_STATUS_P+ (LOOP_OFFSET * loopCount), BOAT_STATUS_L);
                int actualLegNum = ByteCheck.byteToIntConverter(generatedBytes,
                        LEG_NUM_P+ (LOOP_OFFSET * loopCount), LEG_NUM_L);
                int actualPenAwarded = ByteCheck.byteToIntConverter(generatedBytes,
                        NUM_PEN_AWARDED_P + (LOOP_OFFSET * loopCount), NUM_PEN_AWARDED_L);
                int actualPenServed = ByteCheck.byteToIntConverter(generatedBytes,
                        NUM_PEN_SERVED_P + (LOOP_OFFSET * loopCount), NUM_PEN_SERVED_L);
                long actualTimeMark = ByteCheck.byteToLongConverter(generatedBytes,
                        EST_TIME_NEXT_MARK_P + (LOOP_OFFSET * loopCount), EST_TIME_NEXT_MARK_L);
                long actualTimeFinish = ByteCheck.byteToLongConverter(generatedBytes,
                        EST_TIME_FINISH_P + (LOOP_OFFSET * loopCount), EST_TIME_FINISH_L);

                assertEquals(expectedBoatID, actualBoatID);
                assertEquals(expectedBoatStatus, actualBoatStatus);
                assertEquals(expectedLegNum, actualLegNum);
                assertEquals(expectedPenAwarded, actualPenAwarded);
                assertEquals(expectedPenServed, actualPenServed);
                assertEquals(expectedTimeMark, actualTimeMark);
                assertEquals(expectedTimeFinish, actualTimeFinish);

                loopCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
