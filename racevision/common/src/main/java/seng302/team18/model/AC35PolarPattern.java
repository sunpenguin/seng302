package seng302.team18.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold polar patterns for an AC35 yatch
 */
public class AC35PolarPattern extends PolarPattern {


    public AC35PolarPattern() {
        super();
    }

    @Override
    protected Map<Double, Polar> createMap() {
        Map<Double, Polar> polarMap = new HashMap<>();

        //Create Polar where windSpeed is 4
        Polar polarAt4 = new Polar(4, 45,8,155,10);
        polarAt4.addToMap(0,0);
        polarAt4.addToMap(30,4);
        polarAt4.addToMap(60,9);
        polarAt4.addToMap(75,10);
        polarAt4.addToMap(90,10);
        polarAt4.addToMap(115,10);
        polarAt4.addToMap(145,10);
        polarAt4.addToMap(175,4);

        //Create Polar where windSpeed is 8
        Polar polarAt8 = new Polar(8, 43,10, 153, 12);
        polarAt8.addToMap(0,0);
        polarAt8.addToMap(30,7);
        polarAt8.addToMap(60,11);
        polarAt8.addToMap(75,11);
        polarAt8.addToMap(90,11);
        polarAt8.addToMap(115,12);
        polarAt8.addToMap(145,12);
        polarAt8.addToMap(175,10);

        //Create Polar where windSpeed is 12
        Polar polarAt12 = new Polar(12, 43, 14.4, 153,21.6);
        polarAt12.addToMap(0,	0);
        polarAt12.addToMap(30,11);
        polarAt12.addToMap(60,16);
        polarAt12.addToMap(75,20);
        polarAt12.addToMap(90,23);
        polarAt12.addToMap(115,24);
        polarAt12.addToMap(145,23);
        polarAt12.addToMap(175,14);

        //Create Polar where windSpeed is 16
        Polar polarAt16 = new Polar (16,42,19.2,153,28.8);
        polarAt16.addToMap(0,0);
        polarAt16.addToMap(30,12);
        polarAt16.addToMap(60,25);
        polarAt16.addToMap(75,27);
        polarAt16.addToMap(90,31);
        polarAt16.addToMap(115,32);
        polarAt16.addToMap(145,30);
        polarAt16.addToMap(175,20);

        //Create Polar where windspeed is 20
        Polar polarAt20 = new Polar(20, 42, 24, 153, 36);
        polarAt20.addToMap(0, 0);
        polarAt20.addToMap(30, 13);
        polarAt20.addToMap(60, 29);
        polarAt20.addToMap(75, 37);
        polarAt20.addToMap(90, 39);
        polarAt20.addToMap(115, 40);
        polarAt20.addToMap(145, 38);
        polarAt20.addToMap(175, 24);

        //Create Polar where windSpeed is 25
        Polar polarAt25 = new Polar(25, 40,30, 151,47);
        polarAt25.addToMap(0,0);
        polarAt25.addToMap(30,15);
        polarAt25.addToMap(60,38);
        polarAt25.addToMap(75,44);
        polarAt25.addToMap(90,49);
        polarAt25.addToMap(115,50);
        polarAt25.addToMap(145,49);
        polarAt25.addToMap(175,30);

        //Create Polar where windSpeed is 30
        Polar polarAt30 = new Polar(30,42,30,150,46);
        polarAt30.addToMap(0,0);
        polarAt30.addToMap(30,15);
        polarAt30.addToMap(60,37);
        polarAt30.addToMap(75,42);
        polarAt30.addToMap(90,48);
        polarAt30.addToMap(115,49);
        polarAt30.addToMap(145,48);
        polarAt30.addToMap(175,32);

        polarMap.put(polarAt4.getUpWindSpeed(), polarAt4);
        polarMap.put(polarAt8.getUpWindSpeed(), polarAt8);
        polarMap.put(polarAt12.getUpWindSpeed(), polarAt12);
        polarMap.put(polarAt16.getUpWindSpeed(), polarAt16);
        polarMap.put(polarAt20.getUpWindSpeed(), polarAt20);
        polarMap.put(polarAt25.getUpWindSpeed(), polarAt25);
        polarMap.put(polarAt30.getUpWindSpeed(), polarAt30);

        return polarMap;
    }
}
