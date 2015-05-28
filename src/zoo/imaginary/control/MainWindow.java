package zoo.imaginary.control;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import zoo.imaginary.util.ContentFilter;
import zoo.imaginary.util.FileUtils;
import zoo.imaginary.util.TableColumnAdjuster;
import zoo.imaginary.util.XmlTagsAttritubes;

public class MainWindow {

  private JFrame frame;
  private JTable table;
  private JTextArea log;
  private JButton btnOpen;
  private JButton btnAddColumn;
  private JPanel controlPanel;
  private JCheckBox chckbxRowSelection;
  private JCheckBox chckbxColumnSelection;
  private JPanel searchPanel;
  private JTextField txtSearch;
  private JComboBox<String> searchComboBox;
  private JPanel logPanel;
  private JButton btnRemoveColumn;
  private JButton btnRemoveRows;
  private JButton btnAddRows;
  private TableRowSorter<TableModel> sorter;
  private JButton btnSave;
  private JCheckBox chckbxListAutoResize;
  private File currentDirectory;
  private JPanel mainPanel;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          MainWindow window = new MainWindow();
          window.frame.pack();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public MainWindow() {
    initialize();
  }


  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {

    createFrame();

    createMainPanel();

    createSearchPanel();

    createTable();

    createControlPanel();

    createButtonPanel();

    createLogPanel();

    createSplitPanel();
  }

  /**
   * Create table with scroll pane.
   */
  private void createTable() {
    table = new JTable();
    JScrollPane tScrollPane = new JScrollPane(table);
    mainPanel.add(tScrollPane, BorderLayout.CENTER);
  }

  /**
   * Create frame.
   */
  private void createFrame() {
    frame = new JFrame();
    frame.setTitle("Imaginary Zoo Database");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Create split pane.
   */
  private void createSplitPanel() {
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, logPanel);
    splitPane.setResizeWeight(0.8);
    splitPane.setOneTouchExpandable(true);

    frame.setContentPane(splitPane);
  }

  /**
   * Create the main panel.
   */
  private void createMainPanel() {
    mainPanel = new JPanel(new BorderLayout());
  }

  /**
   * Create the search panel and its widgets.
   */
  private void createSearchPanel() {
    searchPanel = new JPanel();

    mainPanel.add(searchPanel, BorderLayout.NORTH);
    searchPanel.setLayout(new GridLayout(0, 2, 0, 0));

    // Search text
    txtSearch = new JTextField();
    txtSearch.setToolTipText("");
    txtSearch.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        newFilter();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        newFilter();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        newFilter();
      }
    });
    searchPanel.add(txtSearch);

    // Search checkbox
    searchComboBox = new JComboBox<String>();
    searchPanel.add(searchComboBox);
  }

  /**
   * Fill up the combo box with the actual column names.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void fillSearchCombo() {
    final TableModel tModel = (TableModel) table.getModel();
    ComboBoxModel cbModel = new DefaultComboBoxModel<>(tModel.getColumnIdentifiers());
    searchComboBox.setModel(cbModel);
    sorter = new TableRowSorter<TableModel>(tModel);
    table.setRowSorter(sorter);
  }

  /**
   * Filter the table content according to the selected combobox element.
   */
  private void newFilter() {
    RowFilter<TableModel, Integer> rf =
        RowFilter.regexFilter(".*" + txtSearch.getText() + ".*", searchComboBox.getSelectedIndex());
    sorter.setRowFilter(rf);
  }

  /**
   * Create the control panel and its widgets.
   */
  private void createControlPanel() {
    controlPanel = new JPanel();
    mainPanel.add(controlPanel, BorderLayout.EAST);
    controlPanel.setLayout(new GridLayout(0, 1, 0, 0));

    // Add Column button
    btnAddColumn = new JButton("Add Column");
    btnAddColumn.setEnabled(true);
    btnAddColumn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        createNewTableModel();
        String columnName = JOptionPane.showInputDialog("Please enter the column name: ");
        // create the family, subfamily, genus, entity columns if there are not exist
        TableModel model = (TableModel) table.getModel();
        if (!model.getColumnIdentifiers().contains(XmlTagsAttritubes.FAMILY_STR.getValue())) {
          createColumn(XmlTagsAttritubes.FAMILY_STR.getValue());
        }
        if (!model.getColumnIdentifiers().contains(XmlTagsAttritubes.SUBFAMILY_STR.getValue())) {
          createColumn(XmlTagsAttritubes.SUBFAMILY_STR.getValue());
        }
        if (!model.getColumnIdentifiers().contains(XmlTagsAttritubes.GENUS_STR.getValue())) {
          createColumn(XmlTagsAttritubes.GENUS_STR.getValue());
        }
        if (!model.getColumnIdentifiers().contains(XmlTagsAttritubes.ENTITY_STR.getValue())) {
          createColumn(XmlTagsAttritubes.ENTITY_STR.getValue());
        }
        if (columnName != null && !columnName.isEmpty()) {
          if (!model.getColumnIdentifiers().contains(columnName)) {
            ((TableModel) table.getModel()).addColumn(columnName);
          } else {
            JOptionPane.showMessageDialog(frame, "The given name already exists.",
                "Creation error", JOptionPane.ERROR_MESSAGE);
          }
        } else {
          JOptionPane.showMessageDialog(frame, "The given name can not be blank.",
              "Creation error", JOptionPane.ERROR_MESSAGE);
        }
        updateWidgets();
      }
    });
    controlPanel.add(btnAddColumn);

    // Remove Column(s) button
    btnRemoveColumn = new JButton("Remove Column");
    btnRemoveColumn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.getSelectedColumns().length == 1) {
          int[] selectedColumns = table.getSelectedColumns();
          TableModel model = (TableModel) table.getModel();
          TableColumnModel columnModel = table.getColumnModel();

          TableColumn column = columnModel.getColumn(table.getSelectedColumn());
          int columnModelIndex = column.getModelIndex();
          Vector data = model.getDataVector();
          Vector colIds = model.getColumnIdentifiers();

          // Remove the column from the table
          table.removeColumn(column);

          // Remove the column header from the table model
          colIds.removeElementAt(columnModelIndex);

          // Remove the column data
          for (int r = 0; r < data.size(); r++) {
            Vector row = (Vector) data.get(r);
            row.removeElementAt(columnModelIndex);
          }
          model.setDataVector(data, colIds);

          // Correct the model indices in the TableColumn objects
          // by decrementing those indices that follow the deleted column
          // Enumeration<TableColumn> enum1 = table.getColumnModel().getColumns();
          // for (; enum1.hasMoreElements();) {
          // TableColumn c = enum1.nextElement();
          // if (c.getModelIndex() >= columnModelIndex) {
          // c.setModelIndex(c.getModelIndex() - 1);
          // }
          // }
          model.fireTableStructureChanged();

          // for (int i = 0; i < selectedColumns.length; i++) {
          // String columnName = table.getColumnName(selectedColumns[i]);
          // TableColumn tableColumn =
          // columnModel.getColumn(columnModel.getColumnIndex(columnName));
          // // hide the given column in the table
          // columnModel.removeColumn(tableColumn);
          // // remove from the given columnName from the columnName list
          // // model.removeColumnName(columnName);
          // // remove the underlying data of the given column from all rows
          // for (int j = 0; j < model.getRowCount(); j++) {
          // model.setValueAt(null, j, selectedColumns[i]);
          // }
          // model.printTableData();
          // }
          updateWidgets();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select only one column.",
              "Deletion warning", JOptionPane.WARNING_MESSAGE);
        }
      }
    });

    // Add Row button
    btnAddRows = new JButton("Add Row");
    btnAddRows.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.getColumnCount() > 0) {
          ((TableModel) table.getModel()).addRow(new Object[table.getColumnCount()]);
          enableDisableControls();
        } else {
          JOptionPane.showMessageDialog(frame, "Please add a column first.",
              "Entity creation warning", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    btnAddRows.setEnabled(false);
    controlPanel.add(btnAddRows);
    btnRemoveColumn.setEnabled(false);
    controlPanel.add(btnRemoveColumn);

    // Remove Row(s) button
    btnRemoveRows = new JButton("Remove Row(s)");
    btnRemoveRows.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println();
        if (table.getSelectedRow() != -1) {
          int[] selectedRows = table.getSelectedRows();
          ((TableModel) table.getModel()).deleteRows(selectedRows);
          enableDisableControls();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select at least a row.", "Deletion warning",
              JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    btnRemoveRows.setEnabled(false);
    controlPanel.add(btnRemoveRows);

    // Row Selection checkbox
    chckbxRowSelection = new JCheckBox("Row Selection");
    chckbxRowSelection.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        table.setRowSelectionAllowed(chckbxRowSelection.isSelected());
      }
    });

    chckbxRowSelection.setEnabled(false);
    chckbxRowSelection.setSelected(true);
    controlPanel.add(chckbxRowSelection);

    // Column Selection checkbox
    chckbxColumnSelection = new JCheckBox("Column Selection");
    chckbxColumnSelection.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        table.setColumnSelectionAllowed(chckbxColumnSelection.isSelected());
      }
    });
    chckbxColumnSelection.setEnabled(false);
    controlPanel.add(chckbxColumnSelection);

    // List Autoresize checkbox
    chckbxListAutoResize = new JCheckBox("List Auto Resize");
    chckbxListAutoResize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (chckbxListAutoResize.isSelected()) {
          table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        } else {
          table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
          TableColumnAdjuster tca = new TableColumnAdjuster(table);
          tca.adjustColumns();
        }
      }
    });
    chckbxListAutoResize.setEnabled(false);
    chckbxListAutoResize.setSelected(true);
    controlPanel.add(chckbxListAutoResize);
  }

  /**
   * Create new table model if there is no one. Typically when is blank the table.
   */
  private void createNewTableModel() {
    if (!(table.getModel() instanceof TableModel)) {
      table.setModel(new TableModel());
    }
  }

  /**
   * Create a column int the table with the given name.
   */
  private void createColumn(String columnName) {
    TableModel model = (TableModel) table.getModel();
    if (!model.getColumnIdentifiers().contains(columnName)) {
      model.addColumn(columnName);
      log.append("The " + columnName + " is created." + "\n");
    }
  }

  /**
   * Update the state of the widgets.
   */
  private void updateWidgets() {
    enableDisableControls();
    fillSearchCombo();
  }

  /**
   * Create the button panel and its buttons.
   */
  private void createButtonPanel() {
    JPanel buttonPanel = new JPanel();
    mainPanel.add(buttonPanel, BorderLayout.WEST);
    buttonPanel.setLayout(new GridLayout(0, 1, 0, 0));

    // Open button
    btnOpen = new JButton("Open");
    btnOpen.setIcon(new ImageIcon(MainWindow.class
        .getResource("/zoo/imaginary/control/images/Open16.gif")));
    buttonPanel.add(btnOpen);


    btnOpen.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TableModel model = null;
        JFileChooser fc = createFileChooser();
        fc.setDialogTitle("Open Database");

        int returnVal = fc.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          currentDirectory = fc.getCurrentDirectory();

          // This is where a real application would open the file.
          log.append("Opening: " + file.getName() + "." + "\n");
          model = new TableModel(file, FileUtils.getExtension(file), frame);
          table.setModel(model);

          updateWidgets();
        } else {
          log.append("Open command cancelled by user." + "\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
      }
    });

    btnSave = new JButton("Save");
    btnSave.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        TableModel model = (TableModel) table.getModel();
        String familyValue = XmlTagsAttritubes.FAMILY_STR.getValue();
        String subfamilyValue = XmlTagsAttritubes.SUBFAMILY_STR.getValue();
        String genusValue = XmlTagsAttritubes.GENUS_STR.getValue();
        String entityValue = XmlTagsAttritubes.ENTITY_STR.getValue();

        if (!model.getColumnIdentifiers().contains(familyValue)
            || !model.getColumnIdentifiers().contains(subfamilyValue)
            || !model.getColumnIdentifiers().contains(genusValue)
            || !model.getColumnIdentifiers().contains(entityValue)) {
          JOptionPane.showMessageDialog(frame, "Some of the required columns missing: "
              + familyValue + ", " + subfamilyValue + ", " + genusValue + ", " + entityValue,
              "Save warning", JOptionPane.WARNING_MESSAGE);
        } else {
          JFileChooser fc = createFileChooser();
          fc.setDialogTitle("Save Database");

          int returnVal = fc.showSaveDialog(frame);
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentDirectory = fc.getCurrentDirectory();
            switch (FileUtils.getExtension(fc.getSelectedFile())) {
              case FileUtils.XML:
                FileUtils.createXmlFileByStAX(fc.getSelectedFile(), table);
                break;
              case FileUtils.CSV:
                FileUtils.createCsvFile(fc.getSelectedFile(), table);
                break;
              default:
                JOptionPane.showMessageDialog(frame, "Please give " + FileUtils.XML + " or "
                    + FileUtils.CSV + " extension to the file", "Save warning",
                    JOptionPane.WARNING_MESSAGE);
            }
            log.append("Saving: " + fc.getSelectedFile().getName() + "." + "\n");
          }
        }
      }

    });
    btnSave.setIcon(new ImageIcon(MainWindow.class
        .getResource("/zoo/imaginary/control/images/Save16.gif")));
    btnSave.setEnabled(false);
    buttonPanel.add(btnSave);
  }

  /**
   * Create a custom FileChooser with only xml and csv filter option.
   *
   * @return the created FileChooser
   */
  private JFileChooser createFileChooser() {
    JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new ContentFilter());
    fc.setAcceptAllFileFilterUsed(true);
    if (currentDirectory == null) {
      currentDirectory = new File(".");
    }
    fc.setCurrentDirectory(currentDirectory);
    return fc;
  }

  /**
   * Create bottom panel with log.
   */
  private void createLogPanel() {
    logPanel = new JPanel();
    logPanel.setLayout(new GridLayout(0, 1, 0, 0));

    // Log scroll pane
    log = new JTextArea();
    log.setEditable(false);
    JScrollPane taScrollPane = new JScrollPane(log);
    logPanel.add(taScrollPane);
  }

  /**
   * Enable or disable the control widgets according to the content of column and row of the table.
   */
  private void enableDisableControls() {
    // Enable/Disable widgets of the control panel
    boolean hasColumn = table.getColumnCount() > 0;
    boolean hasRow = table.getRowCount() > 0;
    btnAddRows.setEnabled(hasColumn);
    btnRemoveColumn.setEnabled(hasRow);
    btnRemoveRows.setEnabled(hasRow);
    chckbxRowSelection.setEnabled(hasRow);
    chckbxColumnSelection.setEnabled(hasRow);
    chckbxListAutoResize.setEnabled(hasRow);
    btnSave.setEnabled(hasColumn);
  }

}
