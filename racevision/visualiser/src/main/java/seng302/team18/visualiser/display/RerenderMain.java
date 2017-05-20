package seng302.team18.visualiser.display;

import seng302.team18.model.Race;

/**
 * Created by hqi19 on 12/05/17.
 */
public class RerenderMain {

    private Race race;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;

    public RerenderMain(CourseRenderer courseRenderer, RaceRenderer raceRenderer, Race race) {
        this.courseRenderer = courseRenderer;
        this.raceRenderer = raceRenderer;
        this.race = race;
    }

    /**
     * To call when course features need redrawing.
     * (For example, when zoom in, the course features are required to change)
     */
    public void redrawFeatures() {
        courseRenderer.renderCourse();
        raceRenderer.renderBoats();
        raceRenderer.reDrawTrails(race.getStartingList());
    }
}
