package zoo.imaginary.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlUtils {

  public static void main(String[] args) {
    List<Row> rows = parseXmlFileBySax("database.xml");
    for (Row row : rows) {
      System.out.println(row);
    }
    String[] columns = findAllColumns(rows);
    System.out.println(Arrays.toString(columns));
  }

  public static List<Row> parseXmlFileBySax(String filePath) {
    UserHandler userhandler = null;
    try {
      File inputFile = new File(filePath);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      userhandler = new UserHandler();
      saxParser.parse(inputFile, userhandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return userhandler.getRows();
  }

  public final static String xml = "xml";
  public final static String csv = "csv";

  /*
   * Get the extension of a file.
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


  public static String[] findAllColumns(List<Row> rows) {
    Set<String> columns = new HashSet<>();
    for (Row row : rows) {
      for (Column column : row.getColumns()) {
        if (!columns.contains(column.getColumn())) {
          columns.add(column.getColumn());
        }
      }
    }
    return columns.toArray(new String[columns.size()]);
  }
}
