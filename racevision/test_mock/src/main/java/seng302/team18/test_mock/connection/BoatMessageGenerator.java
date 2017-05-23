package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.Boat;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Generator for boat messages (see #4.9 in the AC35 Streaming protocol spec.)
 */
public class BoatMessageGenerator extends ScheduledMessageGenerator {
    private Boat boat;
    final double BYTE_COORDINATE_TO_DOUBLE = 180.0 / 2147483648.0;
    final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;
    final int MMPS_TO_KMPH = 36 / 10000;
    final double KMPH_TO_MMPS = 277.778;

    /**
     * Constructs a new instance of BoatMessageGenerator.
     *
     * @param boat a boat to generate messages for.
     */
    public BoatMessageGenerator(Boat boat) {
        super(5, AC35MessageType.BOAT_LOCATION.getCode());

    }

    @Override
    public byte[] getPayload() throws IOException {

        final int LENGTH = 56;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte versionNum = 0x1;
        byte[] timeBytes = ByteCheck.getCurrentTime6Bytes();
        byte[] sourceID = ByteCheck.intToByteArray(boat.getId());
        byte[] sequenceNum = ByteBuffer.allocate(4).array();
        byte[] deviceType = ByteBuffer.allocate(1).array();
        Double latitude = (boat.getCoordinate().getLatitude() / BYTE_COORDINATE_TO_DOUBLE);
        int latInt = latitude.intValue();
        Double longitude = (boat.getCoordinate().getLongitude() / BYTE_COORDINATE_TO_DOUBLE);
        int longInt = longitude.intValue();
        byte[] latitudeBytes = ByteCheck.intToByteArray(latInt);
        byte[] longitudeBytes = ByteCheck.intToByteArray(longInt);
        byte[] altitude = ByteBuffer.allocate(4).array();
        Double heading = (boat.getHeading() / BYTE_HEADING_TO_DOUBLE);
        short headingShort = heading.shortValue();
        byte[] headingBytes = ByteCheck.shortToByteArray(headingShort);
        byte[] pitch = ByteBuffer.allocate(2).array();
        byte[] roll = ByteBuffer.allocate(2).array();
        byte[] speedBytes = ByteBuffer.allocate(2).array();
        byte[] cog = ByteBuffer.allocate(2).array();
        Double speedOverGround = boat.getSpeed() * KMPH_TO_MMPS;
        short speedOverGroundShort = speedOverGround.shortValue();
        byte[] sog = ByteCheck.shortToByteArray(speedOverGroundShort);
        byte[] windInfo = ByteBuffer.allocate(10).array();
        byte[] drift = ByteBuffer.allocate(2).array();
        byte[] set = ByteBuffer.allocate(2).array();
        byte[] rudderAng = ByteBuffer.allocate(2).array();

        outStream.write(versionNum);
        outStream.write(timeBytes);
        outStream.write(sourceID);
        outStream.write(sequenceNum);
        outStream.write(deviceType);
        outStream.write(latitudeBytes);
        outStream.write(longitudeBytes);
        outStream.write(altitude);
        outStream.write(headingBytes);
        outStream.write(pitch);
        outStream.write(roll);
        outStream.write(speedBytes);
        outStream.write(cog);
        outStream.write(sog);
        outStream.write(windInfo);
        outStream.write(drift);
        outStream.write(set);
        outStream.write(rudderAng);

        return outStream.toByteArray();
    }

    public Boat getBoat() {
        return boat;
    }

}

