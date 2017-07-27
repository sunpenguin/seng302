package seng302.team18.racemodel.connection;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.Boat;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.SpeedConverter;

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

    /**
     * Constructs a new instance of BoatMessageGenerator.
     *
     * @param boat a boat to generate messages for.
     */
    public BoatMessageGenerator(Boat boat) {
        super(30, AC35MessageType.BOAT_LOCATION.getCode());
        this.boat = boat;

    }

    @Override
    public byte[] getPayload() throws IOException {

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
        int speedOverGround = setSpeed();
        byte[] sog = ByteCheck.intToUShort(speedOverGround);
        byte[] windInfo = ByteBuffer.allocate(10).array();

        // Drift is now defined as sail in / out
        byte[] sails = setSails();
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
        outStream.write(sails);
        outStream.write(set);
        outStream.write(rudderAng);

        return outStream.toByteArray();
    }

    /**
     * Sets the speed depending on if the sail is in or out.
     *
     * @return the altered speed depending on the sails.
     */
    private int setSpeed() {
        if (boat.isSailOut()) {
            return 0;
        } else {
            return new SpeedConverter().knotsToMms(boat.getSpeed()).intValue();
        }
    }

    /**
     * Encodes the sail in / out feature
     *
     * @return the byte array for if the boat has sails in / out
     */
    private byte[] setSails() {
        int sailOut = 0;
        if (boat.isSailOut()) {
            sailOut = 1;
        }
        return ByteCheck.intToUShort(sailOut);
    }

    public Boat getBoat() {
        return boat;
    }

}

