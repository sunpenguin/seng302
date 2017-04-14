package seng302.team18.test_mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Class to parse race data from a CSV.
 * Data is to be used to create Regatta.xml.
 * CSV should have lines in the format of:
 * Regatta ID, Regatta Name, Course Name, UTC, Magnetic Variation, Race ID, Race Type, Race Start Time, top left Lat, top left long, top right Lat, top right long, bottom right Lat, bottom right long, bottom left Lat, bottom left long
 */
public class LocationCSVParser {

    MockData mockData;

    public LocationCSVParser(MockData mockData){
        this.mockData = mockData;
    }

    /**
     * Reads CSV file and uses information to create Reggata.xml
     * @param file CSV file
     */
    public void ParserCSV(File file) {
        //get number of lines
        int numRaces = 0;
        try {
            numRaces = getNumRaces(file);
        }catch (Exception e){
        }

        //chose a random line
        Random rand = new Random();
        int  lineNum = rand.nextInt(numRaces);

        //get chosen line
        String location = "oh no";
        try {
            location = getLine(lineNum, file);
            makeRegatta(location);
        }catch (Exception e){
            System.out.println(location);
        }
    }

    //gets the number of lines in a file
    private int getNumRaces(File file) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String input;
        int count = 0;
        while((input = bufferedReader.readLine()) != null)
        {
            count++;
        }
        return count;
    }

    //gets a specific line from a file
    private String getLine(int lineNum, File file)throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        int count = 0;
        while(count < lineNum)
        {
            line = bufferedReader.readLine();
            count++;
        }
        return line;
    }

    //creates the regatta.xml file
    private void makeRegatta(String location){
        //split the line into its items
        List<String> locationsInfoList = Arrays.asList(location.split(", "));
        String regattaID = locationsInfoList.get(0);
        String regattaName = locationsInfoList.get(1);
        String courseName = locationsInfoList.get(2);
        String UTC = locationsInfoList.get(3);
        String magneticVariation = locationsInfoList.get(4);
        double raceID = new Double(locationsInfoList.get(5));
        String raceType = locationsInfoList.get(6);
        String raceStartTime = locationsInfoList.get(7);
        double latTL = new Double(locationsInfoList.get(8));
        double longTL = new Double(locationsInfoList.get(9));
        double latTR = new Double(locationsInfoList.get(10));
        double longTR = new Double(locationsInfoList.get(11));
        double latBR = new Double(locationsInfoList.get(12));
        double longBR = new Double(locationsInfoList.get(13));
        double latBL = new Double(locationsInfoList.get(14));
        double longBL = new Double(locationsInfoList.get(15));

        //Create Co-ordinates
        Coordinate topleft = new Coordinate(latTL, longTL);
        Coordinate topRight = new Coordinate(latTR, longTR);
        Coordinate bottomRight = new Coordinate(latBR, longBR);
        Coordinate bottomLeft = new Coordinate(latBL, longBL);

        //Create Co-ordinates list
        List<Coordinate> locationsCoordinates = new ArrayList();
        locationsCoordinates.add(topleft);
        locationsCoordinates.add(topRight);
        locationsCoordinates.add(bottomRight);
        locationsCoordinates.add(bottomLeft);

        //get centre co-ordinate for XML
        Coordinate centre = GPSCalculations.getCentralCoordinate(locationsCoordinates);
        String centralLatitude = String.valueOf(centre.getLatitude());
        String centralLongitude = String.valueOf(centre.getLongitude());

        //set variables for Mock Data
        mockData.centreCoordinate = centre;
        mockData.boundaries = locationsCoordinates;
        mockData.raceID = raceID;
        mockData.raceStartTime = raceStartTime;
        mockData.raceType = raceType;

        System.out.println(mockData);
        //make XML
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root element, RegattaConfig
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("RegattaConfig");
            doc.appendChild(rootElement);

            //RegattaID element
            Element regattaIDElement = doc.createElement("RegattaID");
            regattaIDElement.appendChild(doc.createTextNode(regattaID));
            rootElement.appendChild(regattaIDElement);

            //RegattaName element
            Element regattaNameElement= doc.createElement("RegattaName");
            regattaNameElement.appendChild(doc.createTextNode(regattaName));
            rootElement.appendChild(regattaNameElement);

            //CourseName element
            Element courseNameElement = doc.createElement("CourseName");
            courseNameElement.appendChild(doc.createTextNode(courseName));
            rootElement.appendChild(courseNameElement);

            //CentralLatitude element
            Element centralLatitudeElement= doc.createElement("CentralLatitude");
            centralLatitudeElement.appendChild(doc.createTextNode(centralLatitude));
            rootElement.appendChild(centralLatitudeElement);

            //CentralLongitude element
            Element centralLongitudeElement= doc.createElement("CentralLongitude");
            centralLongitudeElement.appendChild(doc.createTextNode(centralLongitude));
            rootElement.appendChild(centralLongitudeElement);

            //CentralAltitude element
            Element centralAltitudeElement= doc.createElement("CentralAltitude");
            centralAltitudeElement.appendChild(doc.createTextNode("0.00"));
            rootElement.appendChild(centralAltitudeElement);

            //UtcOffset element
            Element utcOffsetElement= doc.createElement("UtcOffset");
            utcOffsetElement.appendChild(doc.createTextNode(UTC));
            rootElement.appendChild(utcOffsetElement);

            //MagneticVariation element
            Element magneticVariationElement= doc.createElement("MagneticVariation");
            magneticVariationElement.appendChild(doc.createTextNode(magneticVariation));
            rootElement.appendChild(magneticVariationElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("racevision/test_mock/src/main/resources/Regatta.xml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

}
