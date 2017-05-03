package seng302.team18.test_mock.parsers;

import seng302.team18.model.Polar;
import seng302.team18.util.PolarCalculator;

import java.io.InputStream;
import java.util.*;

/**
 * Created by dhl25 on 1/05/17.
 */
public class PolarParser {

    /**
     * A method to parse a boat polars file.
     * Return a list of Polars.
     * @param stream
     * @return List<Polar>
     */
    public PolarCalculator parse(InputStream stream) {
        //set up scanner
        Scanner scanner = new Scanner(stream);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        //Setup Polar list to be returned
        List<Polar> polarList = new ArrayList<>();

        //Go through file making polars
        while(scanner.hasNext()) {
            //Parse A line into a polar
            Polar newPolar = parseLine(scanner.nextLine());

            //Add polar to polar list
            polarList.add(newPolar);
        }
        return new PolarCalculator(polarList);
    }

    private Polar parseLine(String line){
        //indices for values
        final int WIND_SPEED_INDEX = 0;
        final int TWA0_INDEX = 1;
        final int BSP0_INDEX = 2;
        final int TWA1_INDEX = 3;
        final int BSP1_INDEX = 4;
        final int UP_ANGLE_INDEX = 5; // true wind angle
        final int UP_BOAT_SPEED_INDEX = 6;
        final int TWA2_INDEX = 7;
        final int BSP2_INDEX = 8;
        final int TWA3_INDEX = 9;
        final int BSP3_INDEX = 10;
        final int TWA4_INDEX = 11;
        final int BSP4_INDEX = 12;
        final int TWA5_INDEX = 13;
        final int BSP5_INDEX = 14;
        final int TWA6_INDEX = 15;
        final int BSP6_INDEX = 16;
        final int DOWN_ANGLE_INDEX = 17; // true wind angle
        final int DOWN_BOAT_SPEED_INDEX = 18;
        final int TWA7_INDEX = 19;
        final int BSP7_INDEX = 20;

        //split line into values
        String[] values = line.split(",");

        //Create new Polar to hold data initialize it with wind speeds
        double windSpeed = Double.parseDouble(values[WIND_SPEED_INDEX]);
        double upWindAngle = Double.parseDouble(values[UP_ANGLE_INDEX]);
        double upWindSpeed = Double.parseDouble(values[UP_BOAT_SPEED_INDEX]);
        double downWindAngle = Double.parseDouble(values[DOWN_ANGLE_INDEX]);
        double downWindSpeed = Double.parseDouble(values[DOWN_BOAT_SPEED_INDEX]);

        //Add keys and values to the polarMap
        Polar newPolar = new Polar(windSpeed, upWindAngle, upWindSpeed, downWindAngle, downWindSpeed);
        newPolar.addToMap(Double.parseDouble(values[TWA0_INDEX]), Double.parseDouble(values[BSP0_INDEX]));
        newPolar.addToMap(Double.parseDouble(values[TWA1_INDEX]), Double.parseDouble(values[BSP1_INDEX]));
        newPolar.addToMap(Double.parseDouble(values[TWA2_INDEX]), Double.parseDouble(values[BSP2_INDEX]));
        newPolar.addToMap(Double.parseDouble(values[TWA3_INDEX]), Double.parseDouble(values[BSP3_INDEX]));
        newPolar.addToMap(Double.parseDouble(values[TWA4_INDEX]), Double.parseDouble(values[BSP4_INDEX]));
        newPolar.addToMap(Double.parseDouble(values[TWA5_INDEX]), Double.parseDouble(values[BSP5_INDEX]));
        newPolar.addToMap(Double.parseDouble(values[TWA6_INDEX]), Double.parseDouble(values[BSP6_INDEX]));
        newPolar.addToMap(Double.parseDouble(values[TWA7_INDEX]), Double.parseDouble(values[BSP7_INDEX]));

        return newPolar;
    }
}
