package seng302.team18.message;

/**
 * Class to hold name request message data
 */
public class NameMessage implements MessageBody{

    private String name;
    private String miniName;
    private int sourceID;

    public NameMessage(String name, String miniName, int sourceID) {
        this.name = name;
        this.miniName = miniName;
        this.sourceID = sourceID;
    }

    public String getName() {
        return name;
    }

    public String getMiniName() {
        return miniName;
    }

    public int getSourceID(){
        return sourceID;
    }

    @Override
    public int getType() {
        return AC35MessageType.NAME.getCode();
    }
}
