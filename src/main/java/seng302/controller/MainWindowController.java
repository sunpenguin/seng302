package seng302.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    @FXML
    private Group group;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        Circle c = new Circle(10, Color.RED);
        c.setCenterX(0);
        c.setCenterY(0);
        group.getChildren().add(c);
    }

    public void closeProgram() {
        System.exit(0);
    }

//    public void start() {
//        graphicsContext.setFill(Color.BEIGE);
//        graphicsContext.fillOval(20, 50, 20,20);
//    }

}
