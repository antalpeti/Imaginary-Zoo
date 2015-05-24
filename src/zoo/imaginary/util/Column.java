package zoo.imaginary.util;

public class Column {
  private String column;
  private String value;

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Column(String column, String value) {
    this.column = column;
    this.value = value;
  }

  @Override
  public String toString() {
    return column + " : " + value;
  }
}
