package seng302.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seng302.Boat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by dhl25 on 27/03/17.
 */
public class PreRaceController {

    @FXML
    private ListView<Boat> listView;
    private Stage stage;

    @FXML
    public void initialize() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(this::showLiveRaceView, 2, TimeUnit.SECONDS);
    }


    public void setBoats(ArrayList<Boat> boats) {
        listView.setItems(FXCollections.observableList(boats));
        listView.setCellFactory(param -> new ListCell<Boat>() {
            @Override
            protected void updateItem(Boat boat, boolean empty) {
                super.updateItem(boat, empty);
                if (empty || boat == null || boat.getBoatName() == null) {
                    setText(null);
                } else {
                    setText(boat.getBoatName());
                }
            }
        });
    }

    public void showLiveRaceView() {
        System.out.println(stage);
        System.out.println(1);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        System.out.println(2);

//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        try {
            Parent root = loader.load(); // throws IOException
            System.out.println(3);
            Stage stage = (Stage) listView.getScene().getWindow();
            MainWindowController mainWindowController = loader.getController();
            System.out.println(4);
            Scene scene = new Scene(root);
            System.out.println(5);
            stage.setScene(scene);
            System.out.println(6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
