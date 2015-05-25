package zoo.imaginary.util;

public class Column {
  private String name;
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
