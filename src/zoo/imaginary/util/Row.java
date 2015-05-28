package zoo.imaginary.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the content of between the opening a closing family tag in case of xml file.
 * Represents a line from the file in case of cvs file.
 */
public class Row {
  private List<Column> columns;

  /**
   * Create a row and initialize it field.
   */
  public Row() {
    columns = new ArrayList<Column>();
  }

  /**
   * Retrieve the column elements of the row.
   *
   * @return the actual column elements of the row
   */
  public List<Column> getColumns() {
    return columns;
  }

  /**
   * Add the end of the list a new column element.
   *
   * @param column the new column element
   */
  public void add(Column column) {
    columns.add(column);
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
