package zoo.imaginary.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class FileUtils {

  public static void main(String[] args) {
    List<Row> rows = parseXmlFileBySax(new File("database.xml"));
    for (Row row : rows) {
      System.out.println(row);
    }
    List<String> columns = findAllColumnNames(rows);
    System.out.println(columns);
  }

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

  public final static String xml = "xml";
  public final static String csv = "csv";

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
