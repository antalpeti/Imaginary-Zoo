package zoo.imaginary.util;

/**
 * Store the xml tags and attributes.
 */
public enum XmlTagsAttritubes {
  FAMILY_STR("family"), SUBFAMILY_STR("subfamily"), GENUS_STR("genus"), ENTITY_STR("entity"), NAME_STR(
      "name"), PROPERTY_STR("property");

  private String value;

  private XmlTagsAttritubes(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
