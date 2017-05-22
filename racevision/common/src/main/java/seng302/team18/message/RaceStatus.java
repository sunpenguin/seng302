package seng302.team18.message;

import seng302.team18.model.Regatta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhl25 on 10/05/17.
 */
public enum RaceStatus {
    NOT_ACTIVE(0), WARNING(1), PREPARATORY(2), STARTED(3), FINISHED(4), RETIRED(5),
    ABANDONED(6), POSTPONED(7), TERMINATED(8), RACE_START_NOT_SET(9), PRESTART(10);

    private int code;

    private RaceStatus(int code) {
        this.code = code;
    }

    public static List<Integer> preRaceCodes() {
        List<Integer> codes = new ArrayList<>();
        codes.add(WARNING.getCode());
        codes.add(PRESTART.getCode());
        return codes;
    }

    public int getCode() {
        return this.code;
    }
}
