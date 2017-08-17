package seng302.team18.message;

import javafx.scene.paint.Color;

/**
 * Class to hold colour request message data
 */
public class ColourMessage implements MessageBody {


    private Color colour;
    private int sourceID;

    public ColourMessage(Color color, int sourceID) {
        this.colour = color;
        this.sourceID = sourceID;
    }


    public Color getColour() {
        return colour;
    }

    public int getSourceID(){
        return sourceID;
    }

    @Override
    public int getType() {
        return AC35MessageType.COLOUR.getCode();
    }


}
