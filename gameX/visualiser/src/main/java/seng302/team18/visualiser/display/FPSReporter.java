package seng302.team18.visualiser.display;


import javafx.scene.control.Label;

/**
 * Class to display FPS
 */
public class FPSReporter {

    private Label label;

    public FPSReporter(Label label) {
        this.label = label;
    }

    public void report(double fps) {
        label.setText("fps: " + Math.round(fps * 10) / 10.0);
    }
}
