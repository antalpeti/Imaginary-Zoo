package zoo.imaginary.model;

/**
 * Represents a tag from the xml, or a column from the cvs file.
 */
public class Column {
  /**
   * Tag name or attribute name from the xml or column name from the csv.
   */
  private String name;
  /**
   * Attribute name or content from the xml or column data from the csv.
   */
  private String data;


  /**
   * Create a new column and initialize its field.
   *
   * @param name the name of the column
   * @param data the data of the column
   */
  public Column(String name, String data) {
    this.name = name;
    this.data = data;
  }

  /**
   * Retrieve the name of the column.
   *
   * @return the actual name of the column
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the column.
   *
   * @param name the new name of the column
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieve the data of the column.
   *
   * @return data the new data of the column
   */
  public String getData() {
    return data;
  }

  /**
   * Set the data of the column.
   *
   * @return data the new data of the column
   */
  public void setData(String data) {
    this.data = data;
  }


  @Override
  public String toString() {
    return name + " : " + data;
  }
}
