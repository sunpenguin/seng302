//package seng302;
//
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;
//
//import com.sun.org.apache.xpath.internal.SourceTree;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//import org.w3c.dom.Node;
//import org.w3c.dom.Element;
//import seng302.Mark;
//
//import java.io.Externalizable;
//import java.io.File;
//import java.util.ArrayList;
//
///**
// * Created by csl62 on 15/03/17.
// */
//public class Xmlreader {
//
//
//    public void constructCourse(File file) {
//
//        try {
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(file);
//
//
//            doc.getDocumentElement().normalize();
//
//            NodeList compoundNodes = doc.getElementsByTagName("compoundMark");
//
//            for(int i = 0; i < compoundNodes.getLength(); i++){
//                Node nNode = compoundNodes.item(i);
//                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                    ArrayList<Mark> marks = new ArrayList<>();
//                    Element compoundElement = (Element) nNode;
//                    String compoundMarkName = compoundElement.getElementsByTagName("name").item(0).getTextContent();
//                    NodeList markList = compoundElement.getElementsByTagName("mark");
//                    for (int j = 0; j < markList.getLength(); j++){
//
//                            Element markElement = (Element) nNode;
//                            String markName = markElement.getElementsByTagName("markName").item(0).getTextContent();
//
//                            double markLatitude = Double.parseDouble(markElement.getElementsByTagName("markLatitude").item(0).getTextContent());
//                            double markLongitude = Double.parseDouble(markElement.getElementsByTagName("markLongitude").item(0).getTextContent());
//                            Mark mark = new Mark(markName, markLongitude, markLatitude);
//                            marks.add(mark);
//
//                    }
//                    CompoundMark compoundMark = new CompoundMark(compoundMarkName, marks);
//                    System.out.println(compoundMark.getName() + compoundMark.getMarks());
//                }
//
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    }
