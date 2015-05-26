package zoo.imaginary.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import zoo.imaginary.util.FileUtils;
import zoo.imaginary.util.Row;

public class TableModel extends DefaultTableModel {

  public TableModel() {
    super();
  }

  public TableModel(File file, String extension) {
    // Retreive data from XML file.
    if (extension.equals(FileUtils.XML)) {
      List<Row> rows = FileUtils.parseXmlFileBySax(file);
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

  public void deleteColumns(String[] deletableColumnNames) {
    List<String> colNamesList = new ArrayList<>();
    for (String colName : deletableColumnNames) {
      colNamesList.add(colName);
    }
    columnIdentifiers.removeAll(colNamesList);
    fireTableStructureChanged();
  }

  public void deleteRows(int[] deletableRowIndexes) {
    // Put rows into a List to make it easier to delete
    List<Object> deletetableRows = new ArrayList<>();
    for (int i = 0; i < deletableRowIndexes.length; i++) {
      deletetableRows.add(getDataVector().get(deletableRowIndexes[i]));
    }
    getDataVector().removeAll(deletetableRows);
    fireTableStructureChanged();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public Vector getColumnNames() {
    return columnIdentifiers;
  }
}
