package zoo.imaginary.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Row {
  private List<Column> columns;

  public Row() {
    columns = new ArrayList<Column>();
  }

  public List<Column> getColumns() {
    return columns;
  }

  @Override
  public String toString() {
    return Arrays.toString(columns.toArray());
  }
}
