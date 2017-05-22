package seng302.team18.message;

/**
 * Created by dhl25 on 11/04/17.
 */
public class AC35XMLRegattaMessage implements MessageBody {
    private double centralLat;
    private double centralLong;
    private String utcOffset;
    private int id;
    private String name;

    public AC35XMLRegattaMessage(int id, String name, double centralLat, double centralLong, String utcOffset) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
