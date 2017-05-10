package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng302.team18.model.Course;

/**
 * Created by afj19 on 10/05/17.
 */
public class BackgroundRenderer {
    //    private Group group;
    private Course course;
    private Image map;
    private ImageView imageView = new ImageView();

    public BackgroundRenderer(Group group, Course course) {
        this.course = course;
//        this.group = group;
        group.getChildren().add(0, imageView);
        imageView.toBack();

        renderBackground();
    }

    public void renderBackground() {
//        group.getChildren().remove(imageView);
        // get map
//        imageView.setImage(map);
//        group.getChildren().add(imageView);
//        imageView.toBack();
    }


}
