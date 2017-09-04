package seng302.team18.racemodel.connection;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.model.RaceMode;
import seng302.team18.racemodel.model.*;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.SpeedConverter;

import java.io.IOException;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Test for RaceMessageGenerator.
 */
public class RaceMessageGeneratorTest {
    private final int LOOP_OFFSET = 20; // move 20 bytes forward for each boat being read.

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

    private byte[] generatedBytes;
    private Race testRace;
    private long currentTime;

    @Before
    public void setUp() throws IOException {
        AbstractRaceBuilder raceBuilder = new RegularRaceBuilder();
        AbstractRegattaBuilder regattaBuilder = new RegattaBuilder1();
        AbstractCourseBuilder courseBuilder = new CourseBuilder1();
        ZonedDateTime now = ZonedDateTime.now();
        currentTime = now.toInstant().toEpochMilli();
        testRace = raceBuilder.buildRace(new Race(), regattaBuilder.buildRegatta(), courseBuilder.buildCourse());
        testRace.setStartTime(now);
        RaceMessageGenerator raceMessageGenerator = new RaceMessageGenerator(testRace);
        generatedBytes = raceMessageGenerator.getPayload();
    }


    @Test
    public void messageVersionTest() {
        int expectedMsgVersion = 2;
        int actualMsgVersion = ByteCheck.byteToInt(generatedBytes, VERSION_P, VERSION_L);
        assertEquals(expectedMsgVersion, actualMsgVersion);
    }


    @Test
    public void currentTimeTest() {
        long expectedCurrentTime = currentTime;
        long actualCurrentTime = ByteCheck.byteToLong(generatedBytes, CURRENT_TIME_P, CURRENT_TIME_L);
        assertEquals(expectedCurrentTime, actualCurrentTime, 10);
    }


    @Test
    public void raceIdTest() {
        int expectedRaceID = testRace.getId();
        int actualRaceID = ByteCheck.byteToInt(generatedBytes, RACE_ID_P, RACE_ID_L);
        assertEquals(expectedRaceID, actualRaceID);
    }


    @Test
    public void raceStatusTest() {
        int expectedRaceStatus = testRace.getStatus().getCode();
        int actualRaceStatus = ByteCheck.byteToInt(generatedBytes, RACE_STATUS_P, RACE_STATUS_L);
        assertEquals(expectedRaceStatus, actualRaceStatus);
    }


    @Test
    public void startTimeTest() {
        long expectedStartTime = currentTime;
        long actualStartTime = ByteCheck.byteToLong(generatedBytes, EXP_START_P, EXP_START_L);
        assertEquals(expectedStartTime, actualStartTime, 10);
    }


    @Test
    public void windDirectionTest() {
        double WIND_DIRECTION_CONVERTER = 65536.0 / 360.0;
        double expectedWindDirection = testRace.getCourse().getWindDirection();
        double actualWindDirection = ByteCheck.byteToInt(generatedBytes, WIND_DIR_P, WIND_DIR_L) / WIND_DIRECTION_CONVERTER;
        assertEquals(expectedWindDirection, actualWindDirection, 1e-6);
    }


    @Test
    public void windSpeedTest() {
        double expectedWindSpeed = testRace.getCourse().getWindSpeed(); // 10
        double windSpeed = ByteCheck.byteToInt(generatedBytes, WIND_SPEED_P, WIND_SPEED_L);
        double actualWindSpeed = new SpeedConverter().mmsToKnots(windSpeed);
        assertEquals(expectedWindSpeed, actualWindSpeed, 1e-2);
    }


    @Test
    public void raceTypeTest() {
        int expectedRaceType = ((int) testRace.getRaceType().getCode());
        int actualRaceType = ByteCheck.byteToInt(generatedBytes, RACE_TYPE_P, RACE_TYPE_L);
        assertEquals(expectedRaceType, actualRaceType);
    }


    @Test
    public void numberOfBoatsTest() {
        int expectedNumBoats = testRace.getStartingList().size();
        int actualNumBoats = ByteCheck.byteToInt(generatedBytes, NUM_BOATS_P, NUM_BOATS_L);
        assertEquals(expectedNumBoats, actualNumBoats);
    }


    @Test
    public void boatIdTest() {
        for (int i = 0; i < testRace.getStartingList().size(); i++) {
            Boat boat = testRace.getStartingList().get(i);
            int expectedBoatId = boat.getId();
            int actualBoatID = ByteCheck.byteToInt(generatedBytes, SOURCE_ID_P + (LOOP_OFFSET * i), SOURCE_ID_L);
            assertEquals(expectedBoatId, actualBoatID);
        }
    }


    @Test
    public void boatStatusTest() {
        int expectedBoatStatus = 2;
        for (int i = 0; i < testRace.getStartingList().size(); i++) {
            int actualBoatStatus = ByteCheck.byteToInt(generatedBytes, BOAT_STATUS_P + (LOOP_OFFSET * i), BOAT_STATUS_L);
            assertEquals(expectedBoatStatus, actualBoatStatus);
        }
    }


    @Test
    public void boatLegNumberTest() {
        for (int i = 0; i < testRace.getStartingList().size(); i++) {
            Boat boat = testRace.getStartingList().get(i);
            int expectedLegNum = boat.getLegNumber();
            int actualLegNum = ByteCheck.byteToInt(generatedBytes, LEG_NUM_P + (LOOP_OFFSET * i), LEG_NUM_L);
            assertEquals(expectedLegNum, actualLegNum);
        }
    }


    @Test
    public void boatPenaltiesAwardedTest() {
        int expectedPenAwarded = 7;
        for (int i = 0; i < testRace.getStartingList().size(); i++) {
            int actualPenAwarded = ByteCheck.byteToInt(generatedBytes, NUM_PEN_AWARDED_P + (LOOP_OFFSET * i), NUM_PEN_AWARDED_L);
            assertEquals(expectedPenAwarded, actualPenAwarded);
        }
    }


    @Test
    public void boatPenaltiesServedTest() {
        int expectedPenServed = 4;
        for (int i = 0; i < testRace.getStartingList().size(); i++) {
            int actualPenServed = ByteCheck.byteToInt(generatedBytes, NUM_PEN_SERVED_P + (LOOP_OFFSET * i), NUM_PEN_SERVED_L);
            assertEquals(expectedPenServed, actualPenServed);
        }
    }


    @Test
    public void boatTimeMarkTest() {
        long expectedTimeMark = 11111111111L;
        for (int i = 0; i < testRace.getStartingList().size(); i++) {
            long actualTimeMark = ByteCheck.byteToLong(generatedBytes, EST_TIME_NEXT_MARK_P + (LOOP_OFFSET * i), EST_TIME_NEXT_MARK_L);
            assertEquals(expectedTimeMark, actualTimeMark);
        }
    }


    @Test
    public void boatTimeFinishTest() {
        long expectedTimeFinish = 6666666666L;
        for (int i = 0; i < testRace.getStartingList().size(); i++) {
            long actualTimeFinish = ByteCheck.byteToLong(generatedBytes, EST_TIME_FINISH_P + (LOOP_OFFSET * i), EST_TIME_FINISH_L);
            assertEquals(expectedTimeFinish, actualTimeFinish);
        }
    }
}
