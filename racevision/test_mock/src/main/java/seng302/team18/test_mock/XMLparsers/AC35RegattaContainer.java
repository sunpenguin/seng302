package seng302.team18.test_mock.XMLparsers;

/**
 * Created by jth102 on 20/04/17.
 */
public class AC35RegattaContainer {

    private int regattaID;
    private double centralLatitude;
    private double getCentralLongtitude;
    private String uTcOffset;

    public int getRegattaID() {
        return regattaID;
    }

    public void setRegattaID(int regattaID) {
        this.regattaID = regattaID;
    }

    public double getCentralLatitude() {
        return centralLatitude;
    }

    public void setCentralLatitude(double centralLatitude) {
        this.centralLatitude = centralLatitude;
    }

    public double getGetCentralLongtitude() {
        return getCentralLongtitude;
    }

    public void setGetCentralLongtitude(double getCentralLongtitude) {
        this.getCentralLongtitude = getCentralLongtitude;
    }

    public String getuTcOffset() {
        return uTcOffset;
    }

    public void setuTcOffset(String uTcOffset) {
        this.uTcOffset = uTcOffset;
    }
}
