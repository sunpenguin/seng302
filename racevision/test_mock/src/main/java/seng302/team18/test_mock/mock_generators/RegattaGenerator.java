package seng302.team18.test_mock.mock_generators;

import seng302.team18.model.Regatta;
import seng302.team18.test_mock.mock_generators.Generator;

import java.util.Random;

/**
 * Created by Justin on 18/04/2017.
 */
public class RegattaGenerator implements Generator {

    private Regatta regatta;

    /**
     * create new Regatta object with randomised attributes.
     * Randomised attributes:
     *  -utcOffset (random double between -12 and 12 in steps of 0.5)
     */
    public void generate() {
        Random random = new Random();
        regatta = new Regatta();

        regatta.setRegattaID(1);
        regatta.setRegattaName("Test Regatta");

        double utcOffset = (random.nextInt(37) - 12) / 2.0;
        regatta.setUTcOffset(utcOffset);
    }



    public Regatta getRegatta(){
        return regatta;
    }
}
