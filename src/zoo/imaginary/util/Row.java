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

  /**
   * Retrieve all column names of the actual row.
   *
   * @return column names in a String array
   */
  public String[] getColumnNames() {
    String[] names = new String[columns.size()];
    for (int i = 0; i < columns.size(); i++) {
      names[i] = columns.get(i).getName();
    }
    return names;
  }

  /**
   * Retrieve all column data of the actual row.
   *
   * @return column data in a String array
   */
  public String[] getColumnDatas() {
    String[] datas = new String[columns.size()];
    for (int i = 0; i < columns.size(); i++) {
      datas[i] = columns.get(i).getData();
    }
    return datas;
  }

  @Override
  public String toString() {
    return Arrays.toString(columns.toArray());
  }
}
