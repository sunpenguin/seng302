package seng302.team18.test_mock.connection;

import seng302.team18.model.*;
import seng302.team18.test_mock.ActiveRace;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hqi19 on 21/04/17.
 */
public class RaceMessageGenerator extends ScheduledMessage {

    private ActiveRace race;
    private String message;

    public RaceMessageGenerator(ActiveRace race) {
        super(2);
        this.race = race;
    }

    @Override
    public byte[] getMessage() throws IOException {

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        int messageLength = 0;

        byte messageVersionNumberBytes = 0x2;

        byte[] currentTimeBytes = ByteCheck.getCurrentTime6Bytes();

        byte[] raceIDBytes = ByteCheck.intToByteArray(race.getRaceID());

        byte raceStatusByte = race.getRaceStatusNumber();

        byte[] expectedStartTimeBytes = ByteCheck.getCurrentTime6Bytes(); // TODO: Use a reasonable starting time

        byte[] raceWindDirectionBytes = ByteCheck.shortToByteArray((short) 0x4000); // Currently set to east

        byte[] raceWindSpeedBytes = ByteCheck.shortToByteArray((short) 5000); // Currently 18 km/h

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
        messageLength += 24;

        for (Boat boat : race.getStartingList()) {
            byte[] sourceIDBytes = ByteCheck.intToByteArray(boat.getId());
            byte statusByte = 0x2; // TODO: Currently always "racing" need to add this to boat class, update as race goes on
            byte legNumberByte = (byte) boat.getLeg().getLegNumber(); // TODO: Update leg numbers so that 0 is prestart, 1 is first leg and so on
            byte numPenaltiesAwardedByte = 0;
            byte numPenaltiesServedByte = 0;
            byte[] estTimeAtNextMark = ByteCheck.convertLongTo6ByteArray(0); // TODO: calculate this value
            byte[] estTimeAtFinish = ByteCheck.convertLongTo6ByteArray(0); // TODO: calculate this value

            outputSteam.write(sourceIDBytes);
            outputSteam.write(statusByte);
            outputSteam.write(legNumberByte);
            outputSteam.write(numPenaltiesAwardedByte);
            outputSteam.write(numPenaltiesServedByte);
            outputSteam.write(estTimeAtNextMark);
            outputSteam.write(estTimeAtFinish);
            messageLength += 20;
        }


        byte header[] = outputSteam.toByteArray();

        return ByteCheck.convertToLittleEndian(header,0);
    }


    public String getMessage2() {

        List<Boat> boats = race.getStartingList();

//        List<BoundaryMark> boundaries = race.getCourse().getBoundaries();
//        List<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();
//        List<MarkRounding> markRoundings = race.getCourse().getMarkRoundings();
//        List<Integer> boatsSourceIDs = new ArrayList<>();

//        DateTime dateTime = new DateTime(DateTime.UTC);
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        utc.format(DateTimeFormatter.ofPattern("hh:mm"));
        String currentTime = fixLength(String.valueOf(utc), 6);
//        System.out.println(currentTime);
        String raceID = fixLength("11080703", 4); //TODO store raceId as variable and use it here
//        System.out.println(raceID);
        String raceStatus = "0"; //it may switch to other value in the future.
//        System.out.println(raceStatus);
        String startTime = fixLength(String.valueOf(Instant.now().toEpochMilli()), 6); //it is milllisecs from 1.1.1997 until now
//        System.out.println(startTime);
        String windDirection = fixLength(String.valueOf(race.getCourse().getWindDirection()), 2);
//        System.out.println(windDirection);
        String windSpeed = fixLength("3100", 2); //it may change in the future
//        System.out.println(windSpeed);
        String numOfBoats = String.valueOf(boats.size());
//        System.out.println(numOfBoats);
        String raceType = "1";

        message = " " + currentTime + raceID + raceStatus + startTime + windDirection + windSpeed + numOfBoats + raceType;

        for (Boat b: boats) {
            String sourceID = fixLength(String.valueOf(b.getId()), 4);
//            System.out.println(sourceID);
            message += sourceID;
            String boatStatus = "0"; // it may change later
//            System.out.println(boatStatus);
            message += boatStatus;
            String legNo = "0"; // it may change later to 1 or 2
//            System.out.println(legNo);
            message += legNo;
            String numOfPenaltiesAwarded = " ";
//            System.out.println(numOfPenaltiesAwarded);
            message += numOfPenaltiesAwarded;
            String numOfPenaltiesServed = " ";
//            System.out.println(numOfPenaltiesServed);
            message += numOfPenaltiesServed;

            Coordinate boatCurrentPosition = b.getCoordinate();
            Coordinate boatNextMark = b.getDestination();
            double distance = boatCurrentPosition.distance(boatNextMark) / 1000; // km
            double time = distance / b.getSpeed() * 3.6e+6; // millisecond
            String timeToNextMark = fixLength(String.valueOf(time), 6);
            message += timeToNextMark;

            String timeToFinish = fixLength("60000", 6); // assume the race will end in 1 minute
//            System.out.println(timeToFinish);
            message += timeToFinish;
//            boatsSourceIDs.add(b.getId());
        }
//        System.out.println(message);
//        System.out.println(message.length());
        System.out.println(message);
        return message;
    }

    private String fixLength(String s, int len) { //TODO move to super class so all subclasses can use this
        StringBuilder str = new StringBuilder(s);
        str.setLength(len);
        return String.valueOf(str);
    }
}