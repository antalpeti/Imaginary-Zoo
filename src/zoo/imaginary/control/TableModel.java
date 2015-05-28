package zoo.imaginary.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import zoo.imaginary.util.FileUtils;
import zoo.imaginary.util.Row;

public class TableModel extends DefaultTableModel {

  private static final long serialVersionUID = 1552922271895981362L;

  public TableModel() {
    super();
  }

  public TableModel(File file, String extension, JFrame frame) {
    List<Row> rows = null;
    switch (extension) {
      case FileUtils.XML:
        // Retrieve data from XML file.
        rows = FileUtils.parseXmlFileBySax(file, frame);
        break;
      case FileUtils.CSV:
        // Retrieve data from CSV file.
        rows = FileUtils.parseCsvFile(file, frame);
        break;
      default:
        break;
    }
    if (rows != null) {
      for (String columnName : FileUtils.findAllColumnNames(rows)) {
        this.addColumn(columnName);
      }
      for (int i = 0; i < rows.size(); i++) {
        String[] row = new String[this.getColumnCount()];
        String[] cNames = rows.get(i).getColumnNames();
        String[] cDatas = rows.get(i).getColumnDatas();
        for (int j = 0; j < this.getColumnCount(); j++) {
          for (int k = 0; k < cNames.length; k++) {
            if (cNames[k].equals(this.getColumnName(j))) {
              row[j] = cDatas[k];
            }
          }
        }
        this.addRow(row);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void deleteRows(int[] deletableRowIndexes) {
    // Put rows into a List to make it easier to delete
    List<Object> deletetableRows = new ArrayList<>();
    for (int i = 0; i < deletableRowIndexes.length; i++) {
      deletetableRows.add(getDataVector().get(deletableRowIndexes[i]));
    }
    getDataVector().removeAll(deletetableRows);
    fireTableDataChanged();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public Vector<?> getColumnIdentifiers() {
    return columnIdentifiers;
  }
}
