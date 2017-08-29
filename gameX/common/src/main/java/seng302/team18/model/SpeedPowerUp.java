package seng302.team18.model;

/**
 * Created by dhl25 on 29/08/17.
 */
public class SpeedPowerUp extends UpdateStrategy {

    private BoatUpdate boatUpdate;


    public SpeedPowerUp(Boat boat) {
        super();
        this.boatUpdate = new BoatUpdate(boat);
    }


    @Override
    public void update(double time) {
        super.update(time);
        boatUpdate.update(time * 3);
    }
}
