package seng302.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import org.xml.sax.SAXException;
import seng302.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    private static DecimalFormat df2 = new DecimalFormat(".##");
    @FXML
    private Group group;
    @FXML
    private TableView tableView;
    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;

    @FXML
    private Polygon arrow;
    @FXML
    private TextField showWindDegree;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
//        Circle c = new Circle(10, Color.RED);
//        c.setCenterX(0);
//        c.setCenterY(0);
//        group.getChildren().add(c);


        try {
            // TODO remember to change the path of the xml files
            Course course = XMLParser.parseCourse(new File("src/main/resources/course.xml"));
            ArrayList<Boat> boats = XMLParser.parseBoats(new File("src/main/resources/boats.xml"));
            race = new Race(boats, course);
            race.setStartingCoordintes(); // sets intial coordinates of boats
            RaceRenderer rr = new RaceRenderer(race, group);
            rr.renderCourse();
            rr.renderArrow();
//          rr.renderBoats();
            RaceLoop rl = new RaceLoop(race, rr);
            rl.start();
//            rl.handle(System.nanoTime()); // first time its called it does nothing
//            rl.handle(System.nanoTime()); // this is when the magic happens
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        ObservableList<Boat> boats = FXCollections.observableArrayList();
        boats.addAll(race.getStartingList());
        tableView.setItems(boats);
        TableColumn<Boat, String> boatPos = new TableColumn("Position");
        boatPos.setCellValueFactory(new PropertyValueFactory<Boat, String>("boatName"));
        TableColumn<Boat, String> boatName = new TableColumn("Name");
        boatName.setCellValueFactory(new PropertyValueFactory<Boat, String>("boatName"));
        tableView.getColumns().setAll(boatName);

        Random ran = new Random();
        double windDegree = ran.nextDouble() * 360;
        arrow.setRotate(windDegree);
        showWindDegree.setText(df2.format(windDegree)+" degree");

        System.out.println(arrow.getLayoutX());
//        Polygon poly = new Polygon();
//        poly.getPoints().addAll(new Double[]{
//                0.0, 0.0,
//                20.0, 10.0,
//                10.0, 20.0 });
//        group.getChildren().add(poly);
    }

    public void closeProgram() {
        System.exit(0);
    }

//    public void start() {
//        graphicsContext.setFill(Color.BEIGE);
//        graphicsContext.fillOval(20, 50, 20,20);
//    }

}
