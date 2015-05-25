package zoo.imaginary.control;

import java.io.File;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import zoo.imaginary.util.FileUtils;
import zoo.imaginary.util.Row;

public class TableModel extends AbstractTableModel {
  String[] columnNames;
  String[][] tableData;

  public TableModel(File file) {
    List<Row> rows = FileUtils.parseXmlFileBySax(file);
    columnNames = FileUtils.findAllColumnNames(rows);
    tableData = new String[rows.size()][columnNames.length];
    for (int i = 0; i < rows.size(); i++) {
      String[] cHeaders = rows.get(i).getColumnNames();
      String[] cContents = rows.get(i).getColumnDatas();
      for (int j = 0; j < columnNames.length; j++) {
        for (int k = 0; k < cHeaders.length; k++) {
          if (cHeaders[k].equals(columnNames[j])) {
            tableData[i][j] = cContents[k];
          }
        }
      }
    }
  }

  @Override
  public String getColumnName(int col) {
    return columnNames[col];
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
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
