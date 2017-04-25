import org.junit.Test;
import seng302.team18.test_mock.ActiveRace;
import seng302.team18.test_mock.TestMock;
import seng302.team18.test_mock.connection.RaceMessageGenerator;
import seng302.team18.util.ByteCheck;

import java.io.IOException;

import static org.junit.Assert.*;

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
        ActiveRace testRace = testMock.testRun();
        byte[] generatedBytes;

        RaceMessageGenerator generator = new RaceMessageGenerator(testRace);
        try {
            generatedBytes = generator.getMessage();

            int expectedMsgVersion = 2;
//            long expectedCurrentTime = System.currentTimeMillis();
            int expectedRaceID = testRace.getRaceID();


            int actualMsgVersion = ByteCheck.byteToIntConverter(generatedBytes, VERSION_P, VERSION_L);
//            long actualCurrentTime = ByteCheck.byteToLongConverter(generatedBytes, CURRENT_TIME_P, CURRENT_TIME_L);
            int actualRaceID = ByteCheck.byteToIntConverter(generatedBytes, RACE_ID_P, RACE_ID_L);

            assertEquals(expectedMsgVersion, actualMsgVersion);
//            assertEquals(expectedCurrentTime, actualCurrentTime, 10);
            assertEquals(expectedRaceID, actualRaceID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
