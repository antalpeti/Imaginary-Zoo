package zoo.imaginary.control;

import java.io.File;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import zoo.imaginary.util.FileUtils;
import zoo.imaginary.util.Row;

public class TableModel extends AbstractTableModel {
  List<String> columnNames;
  String[][] tableData;

  public TableModel(File file, String extension) {
    if (extension.equals(FileUtils.XML)) {
      List<Row> rows = FileUtils.parseXmlFileBySax(file);
      columnNames = FileUtils.findAllColumnNames(rows);
      tableData = new String[rows.size()][columnNames.size()];
      for (int i = 0; i < rows.size(); i++) {
        String[] cHeaders = rows.get(i).getColumnNames();
        String[] cContents = rows.get(i).getColumnDatas();
        for (int j = 0; j < columnNames.size(); j++) {
          for (int k = 0; k < cHeaders.length; k++) {
            if (cHeaders[k].equals(columnNames.get(j))) {
              tableData[i][j] = cContents[k];
            }
          }
        }
      }
    }
  }

  public void addColumn(String columnName) {
    columnNames.add(columnName);
  }

  @Override
  public String getColumnName(int col) {
    return columnNames.get(col);
  }

  @Override
  public int getColumnCount() {
    return columnNames.size();
  }

  @Override
  public int getRowCount() {
    return tableData.length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return tableData[rowIndex][columnIndex];
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return true;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    tableData[rowIndex][columnIndex] = (String) aValue;
    fireTableCellUpdated(rowIndex, columnIndex);
  }
}
