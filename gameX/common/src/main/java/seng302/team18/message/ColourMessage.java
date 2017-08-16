package seng302.team18.message;

/**
 * Class to hold colour request message data
 */
public class ColourMessage implements MessageBody {


    private byte[] colour;
    private int sourceID;

    public void ColourMessage(byte[] color, int sourceID) {
        this.colour = color;
        this.sourceID = sourceID;
    }


    public byte[] getColor() {
        return colour;
    }

    public int getSourceID(){
        return sourceID;
    }

    @Override
    public int getType() {
        return AC35MessageType.COLOR.getCode();
    }


}
