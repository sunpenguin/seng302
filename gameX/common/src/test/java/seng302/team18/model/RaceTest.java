package seng302.team18.model;

/**
 * Test class for the Race class
 */
public class RaceTest {

//
//    /**
//     * A test to ensure that the boats, when updated are travelling to the correct position.
//     * @throws IOException
//     * @throws SAXException
//     * @throws ParserConfigurationException
//     */
//    @Test
//    public void updateBoatsTest() throws IOException, SAXException, ParserConfigurationException {
//        int time = 5;
//        InputStream file = new BufferedInputStream(new BufferedInputStream(getClass().getResourceAsStream("/course.xml")));
//        Course course = XMLCourseParser.parseCourse(file);
////        CompoundMark start = course.getCompoundMarks().get(0);
////        Boat boat1 = new Boat("Emirates", "NZL", 45);
////        ArrayList<Boat> boats = new ArrayList<>();
////        boats.add(boat1);
////        Race race = new Race(boats, course);
////        race.updateBoats(time);
////        double distance = (boat1.getSpeed()/3.6)*time;
////        Coordinate testCoord = GPSCalculations.toCoordinate(start.getCoordinate(),
////                boat1.getHeading(), distance);
////        assertEquals(testCoord.getLatitude(), boat1.getCoordinate().getLatitude(), 0.01);
////        assertEquals(testCoord.getLongitude(), boat1.getCoordinate().getLongitude(), 0.01);
//    }
//
//
//    /**
//     * A test to ensure the course is correctly set for the boat(This method is called inside the race constructor
//     * @throws IOException
//     * @throws SAXException
//     * @throws ParserConfigurationException
//     */
//    @Test
//    public void setCourseForBoatsTest() throws IOException, SAXException, ParserConfigurationException {
//        InputStream file = new BufferedInputStream(new BufferedInputStream(getClass().getResourceAsStream("/course.xml")));
//        Course course = XMLCourseParser.parseCourse(file);
//        Boat boat1 = new Boat("Emirates", "NZL", 45);
//        ArrayList<Boat> boats = new ArrayList<>();
//        boats.add(boat1);
////        Race testRace = new Race(boats, course);
////        CompoundMark expectedMark = course.getCompoundMarks().get(1);
////        CompoundMark actualMark = boat1.getLeg().getDestination();
////        assertEquals(expectedMark.getCoordinate().getLatitude(), actualMark.getCoordinate().getLatitude(), 0.01);
////        assertEquals(expectedMark.getCoordinate().getLongitude(), actualMark.getCoordinate().getLongitude(), 0.01);
//    }
//
//    /**
//     * Created by david on 3/21/17.
//     */
//    public static class CoordinateTest {
//
//        private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
//
//        @Before
//        public void setUpStreams() {
//            System.setErr(new PrintStream(errContent));
//        }
//
//        @After
//        public void cleanUpStreams() {
//            System.setErr(null);
//        }
//
//        @Test
//        public void constructorTest() {
//            String expected = "";
//            Coordinate testCoordinate1 = new Coordinate(1, 1);
//            assertEquals(expected, errContent.toString());
//
//            expected += "Latitude must be between -90 and 90\n";
//            Coordinate testCoordinate2 = new Coordinate(100, 1);
//            assertEquals(expected, errContent.toString());
//
//            expected += "Longitude must be between -180 and 180\n";
//            Coordinate testCoordinate3 = new Coordinate(1, 200);
//            assertEquals(expected, errContent.toString());
//        }
//
//        @Test
//        public void latitudeTest() {
//            double expected = 12;
//            Coordinate actual = new Coordinate(12, 13);
//            assertEquals(expected, actual.getLatitude(), 0.000001);
//
//            expected = 10;
//            actual.setLatitude(10);
//            assertEquals(expected, actual.getLatitude(), 0.000001);
//
//            actual.setLatitude(100000);
//            assertEquals(expected, actual.getLatitude(), 0.000001);
//        }
//
//        @Test
//        public void longitudeTest() {
//            double expected = 13;
//            Coordinate actual = new Coordinate(12, 13);
//            assertEquals(expected, actual.getLongitude(), 0.000001);
//
//            expected = 2;
//            actual.setLongitude(2);
//            assertEquals(expected, actual.getLongitude(), 0.000001);
//
//            actual.setLongitude(10000000);
//            assertEquals(expected, actual.getLongitude(), 0.000001);
//        }
//    }
}
