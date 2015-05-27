package zoo.imaginary.util;


import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Responsible to read the structured xml files.
 */
public class UserHandler extends DefaultHandler {

  private boolean prop = false;
  private String upgroup;
  private String midgroup;
  private String subgroup;
  private String property;
  private Row row;
  private List<Row> rows;

  public UserHandler() {
    rows = new ArrayList<Row>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if (qName.equalsIgnoreCase("family")) {
      upgroup = attributes.getValue("name");
    } else if (qName.equalsIgnoreCase("subfamily")) {
      midgroup = attributes.getValue("name");
    } else if (qName.equalsIgnoreCase("genus")) {
      subgroup = attributes.getValue("name");
    } else if (qName.equalsIgnoreCase("entity")) {
      String entity = attributes.getValue("name");
      row = new Row();
      row.getColumns().add(new Column("family", upgroup));
      row.getColumns().add(new Column("subfamily", midgroup));
      row.getColumns().add(new Column("genus", subgroup));
      row.getColumns().add(new Column("entity", entity));
    } else if (qName.equalsIgnoreCase("property")) {
      property = attributes.getValue("name");
      prop = true;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName.equalsIgnoreCase("entity")) {
      getRows().add(row);
    }
  }

  @Override
  public void characters(char ch[], int start, int length) throws SAXException {
    if (prop) {
      String value = new String(ch, start, length);
      row.getColumns().add(new Column(property, value));
      prop = false;
    }
  }

  public List<Row> getRows() {
    return rows;
  }
}
