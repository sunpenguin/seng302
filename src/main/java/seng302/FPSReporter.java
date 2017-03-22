package seng302;


import javafx.scene.control.Label;

/**
 * Created by dhl25 on 22/03/17.
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
