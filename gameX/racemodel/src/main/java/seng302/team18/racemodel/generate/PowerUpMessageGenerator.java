package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35MessageType;
import seng302.team18.model.PickUp;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates powerup messages
 */
public class PowerUpMessageGenerator extends MessageGenerator {

    private PickUp pickUp;

    public PowerUpMessageGenerator(PickUp pickUp) {
        super(AC35MessageType.POWER_UP.getCode());
        this.pickUp = pickUp;
    }


    @Override
    protected byte[] getPayload() throws IOException {
        final double BYTE_COORDINATE_TO_DOUBLE = 180.0 / 2147483648.0;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] sourceID = ByteCheck.intToByteArray(pickUp.getId());
        Double latitude = (pickUp.getCoordinate().getLatitude() / BYTE_COORDINATE_TO_DOUBLE);
        int latInt = latitude.intValue();
        Double longitude = (pickUp.getCoordinate().getLongitude() / BYTE_COORDINATE_TO_DOUBLE);
        int longInt = longitude.intValue();
        byte[] latitudeBytes = ByteCheck.intToByteArray(latInt);
        byte[] longitudeBytes = ByteCheck.intToByteArray(longInt);
        short radius = (short) ( pickUp.getRadius() * 1000 );
        byte[] radiusBytes = ByteCheck.shortToByteArray(radius);
        byte[] timeout = ByteCheck.longTo6ByteArray((long) pickUp.getTimeout());
        byte[] type = { (byte) pickUp.getType().getCode() };
        byte[] duration = ByteCheck.intToByteArray((int) pickUp.getPowerDuration());

        outStream.write(sourceID);
        outStream.write(latitudeBytes);
        outStream.write(longitudeBytes);
        outStream.write(radiusBytes);
        outStream.write(timeout);
        outStream.write(type);
        outStream.write(duration);

        return outStream.toByteArray();
    }
}
