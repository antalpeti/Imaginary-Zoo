package zoo.imaginary.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class XmlUtils {

  public static void main(String[] args) {
    parseXmlFileBySax("database.xml");
    // parseXmlFileByDom("database.xml");
  }

  public static void parseXmlFileBySax(String filePath) {
    try {
      File inputFile = new File(filePath);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      UserHandler userhandler = new UserHandler();
      saxParser.parse(inputFile, userhandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void parseXmlFileByDom(String filePath) {
    try {
      File inputFile = new File(filePath);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();

      System.out.print("Root element: ");
      System.out.println(doc.getDocumentElement().getNodeName());
      // upgroup
      NodeList upgroupList = doc.getElementsByTagName("upgroup");
      for (int upgroupIndex = 0; upgroupIndex < upgroupList.getLength(); upgroupIndex++) {
        Node upgroupNode = upgroupList.item(upgroupIndex);
        System.out.print(upgroupNode.getNodeName());
        if (upgroupNode.getNodeType() == Node.ELEMENT_NODE) {
          Element upgroupElement = (Element) upgroupNode;
          System.out.print(" name : ");
          System.out.println(upgroupElement.getAttribute("name"));
          // midgroup
          NodeList midgroupList = doc.getElementsByTagName("midgroup");
          for (int midgroupIndex = 0; midgroupIndex < midgroupList.getLength(); midgroupIndex++) {
            Node midgroupNode = midgroupList.item(midgroupIndex);
            System.out.print(midgroupNode.getNodeName());
            if (midgroupNode.getNodeType() == Node.ELEMENT_NODE) {
              Element midgroupElement = (Element) midgroupNode;
              System.out.print(" name : ");
              System.out.println(midgroupElement.getAttribute("name"));
              // subgroup
              NodeList subgroupList = doc.getElementsByTagName("subgroup");
              for (int subgroupIndex = 0; subgroupIndex < subgroupList.getLength(); subgroupIndex++) {
                System.out.println("-------------" + subgroupIndex + "-------------");
                Node subgroupNode = subgroupList.item(subgroupIndex);
                System.out.print(subgroupNode.getNodeName());
                if (subgroupNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element subgroupElement = (Element) subgroupNode;
                  System.out.print(" name : ");
                  System.out.println(subgroupElement.getAttribute("name"));
                  // entity
                  NodeList entityList = doc.getElementsByTagName("entity");
                  for (int entityIndex = 0; entityIndex < entityList.getLength(); entityIndex++) {
                    Node entityNode = entityList.item(entityIndex);
                    System.out.print(entityNode.getNodeName());
                    if (entityNode.getNodeType() == Node.ELEMENT_NODE) {
                      Element entityElement = (Element) entityNode;
                      System.out.print(" name : ");
                      System.out.println(entityElement.getAttribute("name"));
                      // property
                      NodeList propertyList = entityElement.getElementsByTagName("property");
                      for (int propertyIndex = 0; propertyIndex < propertyList.getLength(); propertyIndex++) {
                        Node property = propertyList.item(propertyIndex);
                        if (property.getNodeType() == Node.ELEMENT_NODE) {
                          Element propertyElement = (Element) property;
                          System.out.print(propertyElement.getAttribute("name") + " : ");
                          System.out.println(propertyElement.getTextContent());
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
