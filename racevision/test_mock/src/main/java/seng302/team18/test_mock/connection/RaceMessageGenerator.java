package seng302.team18.test_mock.connection;

import seng302.team18.model.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by hqi19 on 21/04/17.
 */
public class RaceMessageGenerator extends ScheduledMessage {

    private Race race;
    private String message;

    public RaceMessageGenerator(Race race) {
        super(2);
        this.race = race;
    }

    @Override
    public String getMessage() {

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
            System.out.println(timeToNextMark);
            message += timeToNextMark;

            String timeToFinish = fixLength("60000", 6); // assume the race will end in 1 minute
//            System.out.println(timeToFinish);
            message += timeToFinish;
//            boatsSourceIDs.add(b.getId());
        }
//        System.out.println(message);
//        System.out.println(message.length());
        return message;
    }

    private String fixLength(String s, int len) { //TODO move to super class so all subclasses can use this
        StringBuilder str = new StringBuilder(s);
        str.setLength(len);
        return String.valueOf(str);
    }
}