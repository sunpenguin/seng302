package seng302.team18.visualiser.display;

/**
 * Enum holding paths to images for use in the controls tutorial.
 */
public enum TutorialImage {
    SHIFT("/images/tutorial/shift.png"),
    SPACE("/images/tutorial/space.png"),
    UP("/images/tutorial/up.png"),
    DOWN("/images/tutorial/down.png"),
    ENTER("/images/tutorial/enter.png"),
    ESC("/images/tutorial/esc.png");

    private final String text;


    TutorialImage(final String text) {
        this.text = text;
    }


    public String toString() {
        return text;
    }
}
