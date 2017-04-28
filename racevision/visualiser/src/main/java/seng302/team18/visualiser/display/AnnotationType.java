package seng302.team18.visualiser.display;

/**
 * Created by david on 4/8/17.
 */
public enum AnnotationType {
    NAME(1), SPEED(2), ESTIMATED_TIME_NEXT_MARK(3), TIME_SINCE_LAST_MARK(4);

    private int code;

    private AnnotationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return  code;
    }
}
