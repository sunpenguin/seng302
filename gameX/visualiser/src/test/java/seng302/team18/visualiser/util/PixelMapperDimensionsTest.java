package seng302.team18.visualiser.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.After;
import org.junit.Test;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class PixelMapperDimensionsTest {

    private static final GPSCalculations GPS = new GPSCalculations();
    private PixelMapper pixelMapper;
    private DoubleProperty paneWidthProp;
    private DoubleProperty paneHeightProp;
    private Coordinate centre;
    private Coordinate north;
    private Coordinate south;
    private Coordinate west;
    private Coordinate east;


    public void setUp(double paneWidth, double paneHeight,
                      double latitude, double longitude,
                      double dNorth, double dEast, double dSouth, double dWest) {

        paneWidthProp = new SimpleDoubleProperty(paneWidth);
        paneHeightProp = new SimpleDoubleProperty(paneHeight);

        centre = new Coordinate(latitude, longitude);
        north = GPS.toCoordinate(centre, 0, dNorth);
        east = GPS.toCoordinate(centre, 90, dEast);
        south = GPS.toCoordinate(centre, 180, dSouth);
        west = GPS.toCoordinate(centre, 270, dWest);

        pixelMapper = new PixelMapper(
                new Coordinate(north.getLatitude(), west.getLongitude()),
                new Coordinate(south.getLatitude(), east.getLongitude()),
                centre,
                paneHeightProp, paneWidthProp
        );
    }


    @After
    public void tearDown() throws Exception {
        pixelMapper = null;
        centre = null;
        north = null;
        south = null;
        west = null;
        east = null;
        paneWidthProp = null;
        paneHeightProp = null;
    }


    private double getMetresPerPixel(Coordinate coordinate1, Coordinate coordinate2) {
        double dDistance = GPS.distance(coordinate1, coordinate2);
        double dPixels = pixelMapper.mapToPane(coordinate1).calculateDistance(pixelMapper.mapToPane(coordinate2));
        return dDistance / dPixels;
    }


    private void checkRatios(String testCase) {
        double ratioNorth = getMetresPerPixel(centre, north);
        double ratioEast = getMetresPerPixel(centre, east);
        double ratioSouth = getMetresPerPixel(centre, south);
        double ratioWest = getMetresPerPixel(centre, west);

        checkRatios(testCase + ": m/px for north not same as south", ratioNorth, ratioSouth);
        checkRatios(testCase + ": m/px for  west not same as  east", ratioWest, ratioEast);
        checkRatios(testCase + ": m/px for north not same as  east", ratioNorth, ratioEast);
    }


    private void checkRatios(String message, double ratio1, double ratio2) {
        final double delta = Math.min(ratio1, ratio2) * 0.03;
        assertEquals(message, ratio1, ratio2, delta);
    }


    private void setMediumSquarePaneMediumSquareEquatorialCourse() throws Exception {
        setUp(750, 750,
                0, 0,
                500, 500, 500, 500);
    }


    // ============================= Small Courses =========================================================

    @Test
    public void smallSquarePaneSmallSquareEquatorialCourse() throws Exception {
        setUp(450, 450,
                0, 0,
                100, 100, 100, 100);
        checkRatios("Small square pane, small square equatorial course");
    }


    @Test
    public void mediumSquarePaneSmallSquareEquatorialCourse() throws Exception {
        setUp(750, 750,
                0, 0,
                100, 100, 100, 100);
        checkRatios("Medium square pane, small square equatorial course");
    }


    @Test
    public void largeSquarePaneSmallSquareEquatorialCourse() throws Exception {
        setUp(1200, 1200,
                0, 0,
                100, 100, 100, 100);
        checkRatios("Large square pane, small square equatorial course");
    }


    // ============================= Medium Courses =========================================================

    @Test
    public void smallSquarePaneMediumSquareEquatorialCourse() throws Exception {
        setUp(450, 450,
                0, 0,
                500, 500, 500, 500);
        checkRatios("Small square pane, medium square equatorial course");
    }


    @Test
    public void mediumSquarePaneMediumSquareEquatorialCourse() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();
        checkRatios("Medium square pane, medium square equatorial course");
    }


    @Test
    public void largeSquarePaneMediumSquareEquatorialCourse() throws Exception {
        setUp(1200, 1200,
                0, 0,
                500, 500, 500, 500);
        checkRatios("Large square pane, medium square equatorial course");
    }


    // ============================= Large Courses =========================================================

    @Test
    public void smallSquarePaneLargeSquareEquatorialCourse() throws Exception {
        setUp(450, 450,
                0, 0,
                2000, 2000, 2000, 2000);
        checkRatios("Small square pane, large square equatorial course");
    }


    @Test
    public void mediumSquarePaneLargeSquareEquatorialCourse() throws Exception {
        setUp(750, 750,
                0, 0,
                2000, 2000, 2000, 2000);
        checkRatios("Medium square pane, large square equatorial course");
    }


    @Test
    public void largeSquarePaneLargeSquareEquatorialCourse() throws Exception {
        setUp(1200, 1200,
                0, 0,
                2000, 2000, 2000, 2000);
        checkRatios("Large square pane, large square equatorial course");
    }


    // ============================= Changing latitudes =========================================================

    @Test
    public void mediumSquarePaneMediumSquareSubtropicalCourse() throws Exception {
        setUp(750, 750,
                18, 0,
                500, 500, 500, 500);
        checkRatios("Medium square pane, medium square sub-tropical course");
    }


    @Test
    public void mediumSquarePaneMediumSquareTemperateCourse() throws Exception {
        setUp(750, 750,
                45, 0,
                500, 500, 500, 500);
        checkRatios("Medium square pane, medium square temperate course");
    }


    @Test
    public void mediumSquarePaneMediumSquareArcticCourse() throws Exception {
        setUp(750, 750,
                70, 0,
                500, 500, 500, 500);
        checkRatios("Medium square pane, medium square arctic course");
    }


    // ========================== Rectangular Pane ===================================================

    @Test
    public void tallPaneMediumSquareEquatorialCourse() throws Exception {
        setUp(600, 1200,
                0, 0,
                500, 500, 500, 500);
        checkRatios("Tall pane, medium square equatorial course");
    }


    @Test
    public void widePaneMediumSquareEquatorialCourse() throws Exception {
        setUp(1200, 600,
                0, 0,
                500, 500, 500, 500);
        checkRatios("Wide pane, medium square equatorial course");
    }

    // ========================== Rectangular Course ===================================================

    @Test
    public void mediumSquarePaneTallEquatorialCourse() throws Exception {
        setUp(750, 750,
                0, 0,
                1000, 250, 500, 250);
        checkRatios("Medium square pane, tall equatorial course");
    }


    @Test
    public void mediumSquarePaneWideEquatorialCourse() throws Exception {
        setUp(750, 750,
                0, 0,
                250, 1000, 250, 500);
        checkRatios("Medium square pane, wide equatorial course");
    }


    // ========================== Rectangular Pane & Course ===================================================

    @Test
    public void tallPaneTallEquatorialCourse() throws Exception {
        setUp(600, 1200,
                0, 0,
                1000, 250, 500, 250);
        checkRatios("Tall pane, tall equatorial course");
    }


    @Test
    public void widePaneTallEquatorialCourse() throws Exception {
        setUp(1200, 600,
                0, 0,
                1000, 250, 500, 250);
        checkRatios("Wide pane, tall equatorial course");
    }


    @Test
    public void tallPaneWideEquatorialCourse() throws Exception {
        setUp(600, 1200,
                0, 0,
                250, 1000, 250, 500);
        checkRatios("Tall pane, wide equatorial course");
    }


    @Test
    public void widePaneWideEquatorialCourse() throws Exception {
        setUp(1200, 600,
                0, 0,
                250, 1000, 250, 500);
        checkRatios("Wide pane, wide equatorial course");
    }


    // ============================= Sanity check pixel values ==================================================
    // Catches reflection and rotation

    @Test
    public void centreHorizontalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();
        XYPair point = pixelMapper.mapToPane(centre);
        assertEquals("centre point not centred horizontally", paneWidthProp.get() / 2, point.getX(), 1);
    }


    @Test
    public void centreVerticalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();
        XYPair point = pixelMapper.mapToPane(centre);
        assertEquals("centre point not centred vertically", paneHeightProp.get() / 2, point.getY(), 1);
    }


    @Test
    public void northHorizontalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(north);
        assertEquals("northern point horizontal position wrong", (paneWidthProp.get() / 2), point.getX(), 1);
    }


    @Test
    public void northVerticalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(north);
        assertTrue("northern point vertical position wrong", point.getY() < (paneHeightProp.get() / 2));
    }


    @Test
    public void southHorizontalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(south);
        assertEquals("southern point horizontal position wrong", (paneWidthProp.get() / 2), point.getX(), 1);
    }


    @Test
    public void southVerticalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(south);
        assertTrue("southern point vertical position wrong", point.getY() > (paneHeightProp.get() / 2));
    }


    @Test
    public void eastHorizontalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(east);
        assertTrue("eastern point horizontal position wrong", point.getX() > (paneWidthProp.get() / 2));
    }


    @Test
    public void eastVerticalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(east);
        assertEquals("eastern point vertical position wrong", (paneHeightProp.get() / 2), point.getY(), 1);
    }


    @Test
    public void westHorizontalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(west);
        assertTrue("western point horizontal position wrong", point.getX() < (paneWidthProp.get() / 2));
    }


    @Test
    public void westVerticalPositionTest() throws Exception {
        setMediumSquarePaneMediumSquareEquatorialCourse();

        XYPair point = pixelMapper.mapToPane(west);
        assertEquals("western point vertical position wrong", (paneHeightProp.get() / 2), point.getY(), 1);
    }
}
