package zoo.imaginary.util;

import java.util.ArrayList;
import java.util.List;

public class Row {
  private List<Column> columns;

  public Row() {
    setColumns(new ArrayList<Column>());
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }
}
