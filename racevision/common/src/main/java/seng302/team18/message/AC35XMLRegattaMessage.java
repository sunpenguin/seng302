package seng302.team18.message;

/**
 * Created by dhl25 on 11/04/17.
 */
public class AC35XMLRegattaMessage implements MessageBody {
    private double centralLat;
    private double centralLong;
    private String utcOffset;
    private int id;

    public AC35XMLRegattaMessage(int id, double centralLat, double centralLong, String utcOffset) {
        this.id = id;
        this.centralLat = centralLat;
        this.centralLong = centralLong;
        this.utcOffset = utcOffset;
    }


    @Override
    public int getType() {
        return AC35MessageType.XML_REGATTA.getCode();
    }


    public double getCentralLat() {
        return centralLat;
    }


    public double getCentralLong() {
        return centralLong;
    }


    public String getUtcOffset() {
        return utcOffset;
    }

    public int getId() {
        return id;
    }
}
