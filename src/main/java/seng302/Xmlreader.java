package seng302;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import seng302.Mark;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by csl62 on 15/03/17.
 */
public class Xmlreader {


//    public static Course constructCourse(File file) {
//        ArrayList<CompoundMark> compoundsMarks = new ArrayList<>();
//        Course course = new Course(compoundsMarks);
//        try {
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(file);
//            doc.getDocumentElement().normalize();
//            NodeList compoundNodes = doc.getElementsByTagName("compoundMark");
//            for(int i = 0; i < compoundNodes.getLength(); i++){
//                Node nNode = compoundNodes.item(i);
//                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                    ArrayList<Mark> marks = new ArrayList<>();
//                    Element compoundElement = (Element) nNode;
//                    String compoundMarkName = compoundElement.getElementsByTagName("name").item(0).getTextContent();
//                    NodeList markList = compoundElement.getElementsByTagName("mark");
//                    for (int j = 0; j < markList.getLength(); j++){
//                            Element markElement = (Element) nNode;
//                            String markName = markElement.getElementsByTagName("markName").item(0).getTextContent();
//                            double markLatitude = Double.parseDouble(markElement.getElementsByTagName("markLatitude").item(0).getTextContent());
//                            double markLongitude = Double.parseDouble(markElement.getElementsByTagName("markLongitude").item(0).getTextContent());
//                            Mark mark = new Mark(markName, new Coordinate(markLongitude, markLatitude));
//                            marks.add(mark);
//                    }
//                    CompoundMark compoundMark = new CompoundMark(compoundMarkName, marks);
//                    compoundsMarks.add(compoundMark);
//                    System.out.println(compoundMark.getName() + compoundMark.getMarks());
//                }
//            }
//            course.setCompoundMarks(compoundsMarks);
//            return course;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return course;
//    }
//    }

    public static Course constructCourse(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList compoundNodes = doc.getElementsByTagName("compoundMark");
        ArrayList<CompoundMark> compoundMarks = new ArrayList<>();
        for(int i = 0; i < compoundNodes.getLength(); i++) {
            Node compoundMarkNode = compoundNodes.item(i);
            if (compoundMarkNode.getNodeType() == Node.ELEMENT_NODE) {
                Element compoundMarkElement = (Element) compoundMarkNode;
                compoundMarks.add(parseCompoundMark(compoundMarkElement));
            }
        }
        return new Course(compoundMarks);
    }

    private static CompoundMark parseCompoundMark(Element compoundMarkElement) {
        String name = compoundMarkElement.getElementsByTagName("name").item(0).getTextContent();
        NodeList markList = compoundMarkElement.getElementsByTagName("mark");
        ArrayList<Mark> marks = new ArrayList<>();
        for (int i = 0; i < markList.getLength(); i++) {
            marks = new ArrayList<>();
            Node markNode = markList.item(i);
            Element markElement = (Element) markNode;
            String markName = markElement.getElementsByTagName("markName").item(0).getTextContent();
            double markLatitude = Double.parseDouble(markElement.getElementsByTagName("markLatitude").item(0).getTextContent());
            double markLongitude = Double.parseDouble(markElement.getElementsByTagName("markLongitude").item(0).getTextContent());
            Mark mark = new Mark(markName, new Coordinate(markLongitude, markLatitude));
            marks.add(mark);
        }
        return new CompoundMark(name, marks);
    }

}
