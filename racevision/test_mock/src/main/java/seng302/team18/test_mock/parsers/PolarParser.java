package seng302.team18.test_mock.parsers;

import seng302.team18.model.Polar;

import java.io.InputStream;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by dhl25 on 1/05/17.
 */
public class PolarParser {

    public Polar parse(InputStream stream) {
        final int POLAR_INDEX = 0;
        final int UP_TWA_INDEX = 3; // true wind angle
        final int UP_BOAT_SPEED_INDEX = 4;
        final int DOWN_TWA_INDEX = 13; // true wind angle
        final int DOWN_BOAT_SPEED_INDEX = 14;
        Scanner scanner = new Scanner(stream);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        String[] values;
        Map<Double, Double> polarUpAngle = new HashMap<>();
        Map<Double, Double> polarDownAngle = new HashMap<>();
        Map<Double, Double> polarUpSpeed = new HashMap<>();
        Map<Double, Double> polarDownSpeed = new HashMap<>();
        while(scanner.hasNext()) {
            values = scanner.nextLine().split("\\s+");
            Double polar = Double.parseDouble(values[POLAR_INDEX]);
            polarUpAngle.put(polar, Double.parseDouble(values[UP_TWA_INDEX]));
            polarUpSpeed.put(polar, Double.parseDouble(values[UP_BOAT_SPEED_INDEX]));
            polarDownAngle.put(polar, Double.parseDouble(values[DOWN_TWA_INDEX]));
            polarDownSpeed.put(polar, Double.parseDouble(values[DOWN_BOAT_SPEED_INDEX]));
        }
        return new Polar(polarUpAngle, polarUpSpeed, polarDownAngle, polarDownSpeed);
    }
}
