package seng302.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    @FXML
    private Canvas canvas;
    @FXML
    private GraphicsContext graphicsContext;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.LIGHTBLUE);
        final int TOP_LEFT_X = 0;
        final int TOP_LEFT_Y = 0;
        graphicsContext.fillRect(TOP_LEFT_X, TOP_LEFT_Y, canvas.getWidth(), canvas.getHeight());
        System.out.printf("a");
    }

    public void closeProgram() {
        System.exit(0);
    }

    public void start() {
        System.out.println("TTEMP");
        graphicsContext.setFill(Color.BEIGE);
        graphicsContext.fillOval(20, 50, 20,20);
    }

}
