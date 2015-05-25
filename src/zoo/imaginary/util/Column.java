package zoo.imaginary.util;

/**
 * Represent a tag from the xml, or a column from the cvs file.
 */
public class Column {
  /**
   * Tag name or attribute name from the xml or column name from the csv.
   */
  private String name;
  /**
   * Atribute name or content from the xml or column data from the csv.
   */
  private String data;

  public String getName() {
    return name;
  }

  public void setNames(String header) {
    this.name = header;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Column(String name, String data) {
    this.name = name;
    this.data = data;
  }

  @Override
  public String toString() {
    return name + " : " + data;
  }
}
