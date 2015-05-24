package zoo.imaginary.util;


import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
    if (qName.equalsIgnoreCase("upgroup")) {
      upgroup = attributes.getValue("name");
      // System.out.println("upgroup : " + attributes.getValue("name"));
    } else if (qName.equalsIgnoreCase("midgroup")) {
      midgroup = attributes.getValue("name");
      // System.out.println("midgroup : " + attributes.getValue("name"));
    } else if (qName.equalsIgnoreCase("subgroup")) {
      subgroup = attributes.getValue("name");
      // System.out.println("subgroup : " + attributes.getValue("name"));
    } else if (qName.equalsIgnoreCase("entity")) {
      String entity = attributes.getValue("name");
      row = new Row();
      row.getColumns().add(new Column("upgroup", upgroup));
      row.getColumns().add(new Column("midgroup", midgroup));
      row.getColumns().add(new Column("subgroup", subgroup));
      row.getColumns().add(new Column("entity", entity));
      // System.out.println("entity : " + attributes.getValue("name"));
    } else if (qName.equalsIgnoreCase("property")) {
      property = attributes.getValue("name");
      // System.out.print("property : " + attributes.getValue("name"));
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
      // System.out.println(" : " + value);
      prop = false;
    }
  }

  public List<Row> getRows() {
    return rows;
  }
}
