package seng302.team18.visualiser.display;

/**
 * Enumerated types for annotations displayed on the GUI
 */
public enum AnnotationType {
    NAME(1),
    SPEED(2);

    private int code;

    AnnotationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return  code;
    }
}
