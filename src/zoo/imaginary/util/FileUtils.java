package zoo.imaginary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import zoo.imaginary.model.Column;
import zoo.imaginary.model.Row;
import zoo.imaginary.model.TableModel;

/**
 * Helper functions for xml, csv file processing.
 */
public class FileUtils {

  public final static String XML = "xml";
  public final static String CSV = "csv";

  /**
   * Parse the content of the xml file and arrange into rows.
   *
   * @param file the xml file
   * @param frame the actual frame to show the error dialogs
   * @return the content of the xml file arranged into rows
   */
  public static List<Row> parseXmlFileBySax(File file, JFrame frame) {
    UserHandler userhandler = null;
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      userhandler = new UserHandler();
      saxParser.parse(file, userhandler);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
    return userhandler.getRows();
  }

  /**
   * Create a new xml file based on the actually selected rows of the table.
   *
   * @param file this file will store the xml data
   * @param table this provide the data
   */
  public static void createXmlFileByStAX(File file, JTable table) {
    try {
      TableModel model = (TableModel) table.getModel();

      StringWriter stringWriter = new StringWriter();

      XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
      XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

      xMLStreamWriter.writeStartDocument();
      xMLStreamWriter.writeStartElement("zoo");

      int indexOfFamily =
          model.getColumnIdentifiers().indexOf(XmlTagsAttritubes.FAMILY_STR.getValue());
      int indexOfSubFamily =
          model.getColumnIdentifiers().indexOf(XmlTagsAttritubes.SUBFAMILY_STR.getValue());
      int indexOfGenus =
          model.getColumnIdentifiers().indexOf(XmlTagsAttritubes.GENUS_STR.getValue());
      int indexOfEntity =
          model.getColumnIdentifiers().indexOf(XmlTagsAttritubes.ENTITY_STR.getValue());

      int[] rowIndexes;
      if (table.getSelectedRows().length > 0) {
        rowIndexes = table.getSelectedRows();
      } else {
        rowIndexes = new int[model.getRowCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
          rowIndexes[i] = i;
        }
      }

      for (int rowIdx : rowIndexes) {
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.FAMILY_STR.getValue());
        xMLStreamWriter.writeAttribute(
            XmlTagsAttritubes.NAME_STR.getValue(),
            (String) (model.getValueAt(rowIdx, indexOfFamily) == null ? "" : model.getValueAt(
                rowIdx, indexOfFamily)));
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.SUBFAMILY_STR.getValue());
        xMLStreamWriter.writeAttribute(
            XmlTagsAttritubes.NAME_STR.getValue(),
            (String) (model.getValueAt(rowIdx, indexOfSubFamily) == null ? "" : model.getValueAt(
                rowIdx, indexOfSubFamily)));
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.GENUS_STR.getValue());
        xMLStreamWriter.writeAttribute(
            XmlTagsAttritubes.NAME_STR.getValue(),
            (String) (model.getValueAt(rowIdx, indexOfGenus) == null ? "" : model.getValueAt(
                rowIdx, indexOfGenus)));
        xMLStreamWriter.writeStartElement(XmlTagsAttritubes.ENTITY_STR.getValue());
        xMLStreamWriter.writeAttribute(
            XmlTagsAttritubes.NAME_STR.getValue(),
            (String) (model.getValueAt(rowIdx, indexOfEntity) == null ? "" : model.getValueAt(
                rowIdx, indexOfEntity)));

        for (int colIdx = 0; colIdx < model.getColumnCount(); colIdx++) {
          if (colIdx != indexOfFamily || colIdx != indexOfSubFamily || colIdx != indexOfGenus
              || colIdx != indexOfEntity) {
            if (model.getValueAt(rowIdx, colIdx) != null) {
              xMLStreamWriter.writeStartElement(XmlTagsAttritubes.PROPERTY_STR.getValue());
              xMLStreamWriter.writeAttribute(XmlTagsAttritubes.NAME_STR.getValue(),
                  model.getColumnName(colIdx));
              xMLStreamWriter.writeCharacters((String) model.getValueAt(rowIdx, colIdx));
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

      OutputStreamWriter outputStreamWriter =
          new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8").newEncoder());
      outputStreamWriter.write(xmlString);
      outputStreamWriter.close();

    } catch (XMLStreamException e) {
      JOptionPane.showMessageDialog(table, e.getMessage(), "XML error", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(table, e.getMessage(), "Input/Output error",
          JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

  /**
   * Parse the content of the csv file and arrange into rows.
   *
   * @param file the csv file
   * @param frame the actual frame to show the error dialogs
   * @return the content of the csv file arranged into rows
   */
  public static List<Row> parseCsvFile(File file, JFrame frame) {
    List<Row> rows = new ArrayList<>();
    try {
      // Open the file
      FileInputStream fileInputStream = new FileInputStream(file);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
      String strLine;
      String[] columnHeaders = null;
      // Read File Line By Line
      while ((strLine = bufferedReader.readLine()) != null) {
        // Print the content on the console
        if (strLine.length() > 1) {
          Row row = new Row();
          String subStrLine = strLine.substring(1, strLine.length() - 1);
          String[] columnDatas = subStrLine.split("\"\\s*,\\s*\"");
          if (columnHeaders == null) {
            columnHeaders = columnDatas;
          } else {
            for (int i = 0; i < columnDatas.length; i++) {
              row.add(new Column(columnHeaders[i], columnDatas[i]));
            }
            rows.add(row);
          }
        }
      }
      // Close the file
      bufferedReader.close();
      fileInputStream.close();
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(frame, e.getMessage(), "File error", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(frame, e.getMessage(), "Input/Output error",
          JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }

    return rows;
  }

  /**
   * Create a new csv file based on the actually selected rows of the table.
   *
   * @param file this file will store the csv data
   * @param table this provide the data
   */
  public static void createCsvFile(File file, JTable table) {
    TableModel model = (TableModel) table.getModel();

    int[] rowIndexes;
    if (table.getSelectedRows().length > 0) {
      rowIndexes = table.getSelectedRows();
    } else {
      rowIndexes = new int[model.getRowCount()];
      for (int i = 0; i < model.getRowCount(); i++) {
        rowIndexes[i] = i;
      }
    }

    StringBuilder stringBuilder = new StringBuilder();
    String separator = ",";
    String newLine = "\r\n";
    String doubleQuote = "\"";

    // headers of columns
    for (int i = 0; i < model.getColumnCount(); i++) {
      stringBuilder.append(doubleQuote);
      stringBuilder.append(model.getColumnName(i));
      stringBuilder.append(doubleQuote);
      stringBuilder.append(separator);
    }
    // remove the last separator
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    stringBuilder.append(newLine);

    // datas of columns
    for (int rowIdx : rowIndexes) {
      for (int colIdx = 0; colIdx < model.getColumnCount(); colIdx++) {
        stringBuilder.append(doubleQuote);
        stringBuilder.append(model.getValueAt(rowIdx, colIdx) == null ? "" : model.getValueAt(
            rowIdx, colIdx));
        stringBuilder.append(doubleQuote);
        stringBuilder.append(separator);
      }
      // remove the last separator
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      stringBuilder.append(newLine);
    }

    String csvSting = stringBuilder.toString();

    OutputStreamWriter outputStreamWriter = null;
    try {
      outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
      outputStreamWriter.write(csvSting);
      outputStreamWriter.close();
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(table, e.getMessage(), "File error", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(table, e.getMessage(), "Input/Output error",
          JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    } finally {
    }
  }

  /**
   * Get the extension of a file.
   *
   * @param f the examined file
   * @return the extension of the examined file
   */
  public static String getExtension(File f) {
    String ext = "";
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
