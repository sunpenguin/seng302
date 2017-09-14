package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * A class to display a circular highlight around a specific boat
 */
public class BoatHighlight extends DisplayBoatDecorator {

    private PixelMapper pixelMapper;
    private Circle highlight;
    private final static Color defaultColour = Color.YELLOW;
    private final static Color frozenColour = Color.RED;


    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat        the display boat being decorated
     * @param pixelMapper The PixelMapper used to find where to map the highlight
     */
    BoatHighlight(PixelMapper pixelMapper, DisplayBoat boat) {
        super(boat);

        this.pixelMapper = pixelMapper;
        highlight = new Circle(getBoatLength() * pixelMapper.mappingRatio() * 0.5);
        highlight.setFill(defaultColour);
        highlight.toBack();
        highlight.setOpacity(0.5);
    }


    @Override
    public void setCoordinate(Coordinate coordinate) {
        XYPair pixel = pixelMapper.mapToPane(coordinate);
        highlight.setLayoutX(pixel.getX());
        highlight.setLayoutY(pixel.getY());
        super.setCoordinate(coordinate);
    }


    @Override
    public void setScale(double scaleFactor) {
        highlight.setRadius(getBoatLength() * pixelMapper.mappingRatio() * 0.5);
        super.setScale(scaleFactor);
    }


    @Override
    public void addToGroup(Group group) {
        group.getChildren().add(highlight);
        super.addToGroup(group);
        highlight.toBack();
    }


    @Override
    public void removeFrom(Group group) {
        group.getChildren().remove(highlight);
        super.removeFrom(group);
    }


    @Override
    public void setBoatStatus(BoatStatus status) {
        if (!getStatus().equals(status)) {
            switch (status) {
                case OCS:
                    highlight.setFill(frozenColour);
                    break;
                case RACING:
                    highlight.setFill(defaultColour);
                    break;
            }
        }

        super.setBoatStatus(status);
    }
}
