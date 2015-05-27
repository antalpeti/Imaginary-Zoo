package zoo.imaginary.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import zoo.imaginary.control.TableModel;

public class FileUtils {

  public final static String XML = "xml";
  public final static String CSV = "csv";

  /**
   * Parse the content of the xml file and arrange into rows.
   *
   * @param file the file path of the xml file
   * @return the content of the xml file arranged into rows
   */
  public static List<Row> parseXmlFileBySax(File file) {
    UserHandler userhandler = null;
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      userhandler = new UserHandler();
      saxParser.parse(file, userhandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return userhandler.getRows();
  }

  public static void createXmlFileByStAX(File file, JTable table) {
    try {
      TableModel model = (TableModel) table.getModel();

      StringWriter stringWriter = new StringWriter();

      XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
      XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

      xMLStreamWriter.writeStartDocument();
      xMLStreamWriter.writeStartElement("zoo");

      int indexOfFamily = model.getColumnNames().indexOf(XmlTagsAttritubes.FAMILY_STR.getValue());
      int indexOfSubFamily =
          model.getColumnNames().indexOf(XmlTagsAttritubes.SUBFAMILY_STR.getValue());
      int indexOfGenus = model.getColumnNames().indexOf(XmlTagsAttritubes.GENUS_STR.getValue());
      int indexOfEntity = model.getColumnNames().indexOf(XmlTagsAttritubes.ENTITY_STR.getValue());

      int[] rowIndexes;
      if (table.getSelectedRows().length > 0) {
        rowIndexes = table.getSelectedRows();
      } else {
        rowIndexes = new int[model.getRowCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
          rowIndexes[i] = i;
        }
      }

      for (int r : rowIndexes) {
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.FAMILY_STR.getValue());
        xMLStreamWriter.writeAttribute(XmlTagsAttritubes.NAME_STR.getValue(),
            (String) model.getValueAt(r, indexOfFamily));
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.SUBFAMILY_STR.getValue());
        xMLStreamWriter.writeAttribute(XmlTagsAttritubes.NAME_STR.getValue(),
            (String) model.getValueAt(r, indexOfSubFamily));
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.GENUS_STR.getValue());
        xMLStreamWriter.writeAttribute(XmlTagsAttritubes.NAME_STR.getValue(),
            (String) model.getValueAt(r, indexOfGenus));
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.ENTITY_STR.getValue());
        xMLStreamWriter.writeAttribute(XmlTagsAttritubes.NAME_STR.getValue(),
            (String) model.getValueAt(r, indexOfEntity));

        for (int c = 0; c < model.getColumnCount(); c++) {
          if (c != indexOfFamily || c != indexOfSubFamily || c != indexOfGenus
              || c != indexOfEntity) {
            if (model.getValueAt(r, c) != null) {
              xMLStreamWriter.writeStartElement(XmlTagsAttritubes.PROPERTY_STR.getValue());
              xMLStreamWriter.writeCharacters((String) model.getValueAt(r, c));
              xMLStreamWriter.writeEndElement();
            }
          }
        }
        xMLStreamWriter.writeEndElement(); // close of entity
        xMLStreamWriter.writeEndElement(); // close of genus
        xMLStreamWriter.writeEndElement(); // close of subfamily
        xMLStreamWriter.writeEndElement(); // close of family
      }
      xMLStreamWriter.writeEndDocument(); // close of zoo

      xMLStreamWriter.flush();
      xMLStreamWriter.close();

      String xmlString = stringWriter.getBuffer().toString();
      stringWriter.close();

      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(xmlString);

      fileWriter.close();

    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the extension of a file.
   *
   * @param f the examined file
   * @return the extension of the examined file
   */
  public static String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
  }

  /**
   * Search through all the rows and collect all the header of the columns.
   *
   * @param rows all the rows of the table
   * @return all the header of the columns of the table
   */
  public static List<String> findAllColumnNames(List<Row> rows) {
    Set<String> columns = new HashSet<>();
    for (Row row : rows) {
      for (Column column : row.getColumns()) {
        if (!columns.contains(column.getName())) {
          columns.add(column.getName());
        }
      }
    }
    List<String> list = new ArrayList<String>();
    list.addAll(columns);
    return list;
  }
}
