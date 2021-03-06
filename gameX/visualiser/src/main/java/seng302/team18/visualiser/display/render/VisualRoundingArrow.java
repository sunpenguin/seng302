package seng302.team18.visualiser.display.render;

import javafx.scene.Group;
import seng302.team18.model.BoatStatus;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.display.object.DisplayRoundingArrow;
import seng302.team18.visualiser.util.PixelMapper;


/**
 * Render manager for mark rounding guide arrows
 */
public class VisualRoundingArrow implements Renderable {

    private final ClientRace race;
    private final PixelMapper pixelMapper;
    private final Group group;

    private DisplayRoundingArrow arrow;


    /**
     * @param race        the race to render
     * @param pixelMapper the mapper used to map coordinates to the screen
     * @param group       the group to add elements to
     */
    public VisualRoundingArrow(ClientRace race, PixelMapper pixelMapper, Group group) {
        this.race = race;
        this.pixelMapper = pixelMapper;
        this.group = group;
    }


    @Override
    public void render() {
        removeArrow();

        race.getStartingList()
                .stream()
                .filter(boat -> boat.getId() == race.getPlayerId())
                .filter(boat -> !boat.getStatus().equals(BoatStatus.FINISHED))
                .forEach(boat -> {
                    arrow = new DisplayRoundingArrow(pixelMapper, race.getCourse().getMarkRounding(boat.getLegNumber()));
                    arrow.addToGroup(group);
                    arrow.sendToBack();
                });
    }


    @Override
    public void refresh() {
        render();
    }


    /**
     * Remove the previous rounding arrow once the boat has already passed it.
     */
    private void removeArrow() {
        if (arrow != null) {
            arrow.removeFromGroup(group);
            arrow = null;
        }
    }
}
