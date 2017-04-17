package seng302.team18.visualiser.controller;

/**
 * Created by dhl25 on 17/04/17.
 */
//public class ControllerManager extends Application {
public class ControllerManager {
//    private MainWindowController mainController;
//    private String mainControllerPath;
//    private PreRaceController preRaceController;
//    private String preRacePath;
//
//    private SocketMessageReceiver socketMessageReader;
//    private Race race;
//    private RaceRenderer renderer;
//    private RaceLoop raceLoop;
//
//
//
//    public ControllerManager(String mainControllerPath, String preRacePath) {
//        this.mainControllerPath = mainControllerPath;
//        this.preRacePath = preRacePath;
//    }
//
////    public RaceLoop(Race race, RaceRenderer renderer, FPSReporter fpsReporter, MessageInterpreter interpreter, SocketMessageReceiver reader) {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        getPort();
//        race = new Race();
//        renderer = new RaceRenderer();
//        raceLoop = new RaceLoop(race, );
//
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(mainControllerPath));
//        Parent root = loader.load(); // throws IOException
//        MainWindowController mainWindowController = loader.getController();
//        primaryStage.setTitle("RaceVision");
//        Scene scene = new Scene(root, 1280, 720);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//
//    private void getPort() {
//        Scanner scanner = new Scanner(System.in);
//        String decision = "";
//        while (!decision.toUpperCase().equals("Y") && !decision.toUpperCase().equals("N")) {
//            System.out.println("Would you like a live race? (Y/N)");
//            if (scanner.hasNext()) {
//                decision = scanner.next().toUpperCase();
//            } else {
//                scanner.next();
//            }
//        }
//
//        if (decision.equals("Y")){
//            try {
//                socketMessageReader = new SocketMessageReceiver(4941, new AC35MessageParserFactory());
//            } catch (IOException e) {
//                System.out.println("try again noob");
//                getPort();
//            }
//        }
//    }
}
