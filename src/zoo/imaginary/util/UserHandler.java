package zoo.imaginary.util;


import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import zoo.imaginary.model.Column;
import zoo.imaginary.model.Row;

/**
 * Responsible to read the structured xml files.
 */
public class UserHandler extends DefaultHandler {

  private boolean prop = false;
  private String family;
  private String subfamily;
  private String genus;
  private String entity;
  private String property;
  private Row row;
  private List<Row> rows;

  /**
   * Create new UserHandler and initialize the its row field.
   */
  public UserHandler() {
    rows = new ArrayList<Row>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if (qName.equalsIgnoreCase(XmlTagsAttritubes.FAMILY_STR.getValue())) {
      family = attributes.getValue(XmlTagsAttritubes.NAME_STR.getValue());
    } else if (qName.equalsIgnoreCase(XmlTagsAttritubes.SUBFAMILY_STR.getValue())) {
      subfamily = attributes.getValue(XmlTagsAttritubes.NAME_STR.getValue());
    } else if (qName.equalsIgnoreCase(XmlTagsAttritubes.GENUS_STR.getValue())) {
      genus = attributes.getValue(XmlTagsAttritubes.NAME_STR.getValue());
    } else if (qName.equalsIgnoreCase(XmlTagsAttritubes.ENTITY_STR.getValue())) {
      entity = attributes.getValue(XmlTagsAttritubes.NAME_STR.getValue());
      row = new Row();
      row.add(new Column(XmlTagsAttritubes.FAMILY_STR.getValue(), family));
      row.add(new Column(XmlTagsAttritubes.SUBFAMILY_STR.getValue(), subfamily));
      row.add(new Column(XmlTagsAttritubes.GENUS_STR.getValue(), genus));
      row.add(new Column(XmlTagsAttritubes.ENTITY_STR.getValue(), entity));
    } else if (qName.equalsIgnoreCase(XmlTagsAttritubes.PROPERTY_STR.getValue())) {
      property = attributes.getValue(XmlTagsAttritubes.NAME_STR.getValue());
      prop = true;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName.equalsIgnoreCase(XmlTagsAttritubes.ENTITY_STR.getValue())) {
      getRows().add(row);
    }
  }

  @Override
  public void characters(char ch[], int start, int length) throws SAXException {
    if (prop) {
      String value = new String(ch, start, length);
      row.add(new Column(property, value));
      prop = false;
    }
  }

  public List<Row> getRows() {
    return rows;
  }
}
