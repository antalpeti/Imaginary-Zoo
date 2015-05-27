package zoo.imaginary.control;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import zoo.imaginary.util.ContentFilter;
import zoo.imaginary.util.FileUtils;
import zoo.imaginary.util.TableColumnAdjuster;

public class MainWindow {

  private JFrame frame;
  private JTable table;
  private JTextArea log;
  private JButton btnOpen;
  private JButton btnAddProperty;
  private JPanel controlPanel;
  private JCheckBox chckbxRowSelection;
  private JCheckBox chckbxColumnSelection;
  private JPanel searchPanel;
  private JTextField txtSearch;
  private JComboBox<String> searchComboBox;
  private JPanel logPanel;
  private JButton btnDeleteProperty;
  private JButton btnDeleteAnimal;
  private JButton btnAddAnimal;
  private TableRowSorter<TableModel> sorter;
  private JButton btnSave;
  private JCheckBox chckbxListAutoResize;
  private File currentDirectory;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          MainWindow window = new MainWindow();
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

    createSearchPanel();

    createTable();

    createLogPanel();

    createControlPanel();

    createButtonPanel();

  }

  /**
   * Create table with scroll pane.
   */
  private void createTable() {
    table = new JTable();
    // {
    // @Override
    // public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    // Component component = super.prepareRenderer(renderer, row, column);
    // int rendererWidth = component.getPreferredSize().width;
    // TableColumn tableColumn = getColumnModel().getColumn(column);
    // tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width,
    // tableColumn.getPreferredWidth()));
    // return component;
    // }
    // };
    JScrollPane tScrollPane = new JScrollPane(table);
    frame.getContentPane().add(tScrollPane, BorderLayout.CENTER);
  }

  /**
   * Create frame.
   */
  private void createFrame() {
    frame = new JFrame();
    frame.setTitle("Imaginary Zoo Database");
    frame.setBounds(100, 100, 698, 512);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Create the search panel and its widgets.
   */
  private void createSearchPanel() {
    searchPanel = new JPanel();
    // TableModel model = (TableModel) table.getModel();
    // TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);

    frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
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
    ComboBoxModel cbModel = new DefaultComboBoxModel<>(tModel.getColumnNames());
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
    frame.getContentPane().add(controlPanel, BorderLayout.EAST);
    controlPanel.setLayout(new GridLayout(0, 1, 0, 0));

    // Add button
    btnAddProperty = new JButton("Add Property");
    btnAddProperty.setEnabled(true);
    btnAddProperty.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!(table.getModel() instanceof TableModel)) {
          table.setModel(new TableModel());
        }
        String columnName = JOptionPane.showInputDialog("Please enter the property name: ");
        if (columnName != null && !columnName.isEmpty()) {
          ((TableModel) table.getModel()).addColumn(columnName);
        }
        updateWidgets();
      }
    });
    controlPanel.add(btnAddProperty);

    // Delete Column button
    btnDeleteProperty = new JButton("Delete Property");
    btnDeleteProperty.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.getSelectedColumn() != -1) {
          int[] selectedColumns = table.getSelectedColumns();
          String[] deletableColumnNames = new String[selectedColumns.length];
          for (int i = 0; i < selectedColumns.length; i++) {
            deletableColumnNames[i] = table.getColumnName(selectedColumns[i]);
          }
          ((TableModel) table.getModel()).deleteColumns(deletableColumnNames);
          updateWidgets();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select a property.", "Deletion warning",
              JOptionPane.WARNING_MESSAGE);
        }
      }
    });

    // Add Row button
    btnAddAnimal = new JButton("Add Animal");
    btnAddAnimal.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.getColumnCount() > 0) {
          ((TableModel) table.getModel()).addRow(new Object[table.getColumnCount()]);
          enableDisableControls();
        } else {
          JOptionPane.showMessageDialog(frame, "Please add a property first.",
              "Entity creation warning", JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    btnAddAnimal.setEnabled(false);
    controlPanel.add(btnAddAnimal);
    btnDeleteProperty.setEnabled(false);
    controlPanel.add(btnDeleteProperty);

    // Delete Row button
    btnDeleteAnimal = new JButton("Delete Animal");
    btnDeleteAnimal.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println();
        if (table.getSelectedRow() != -1) {
          int[] selectedRows = table.getSelectedRows();
          ((TableModel) table.getModel()).deleteRows(selectedRows);
          enableDisableControls();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select an animal.", "Deletion warning",
              JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    btnDeleteAnimal.setEnabled(false);
    controlPanel.add(btnDeleteAnimal);

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
    frame.getContentPane().add(buttonPanel, BorderLayout.WEST);
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
          model = new TableModel(file, FileUtils.getExtension(file));
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
        JFileChooser fc = createFileChooser();
        fc.setDialogTitle("Save Database");

        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          currentDirectory = fc.getCurrentDirectory();

          FileUtils.createXmlFileByStAX(fc.getSelectedFile(), table);
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
    frame.getContentPane().add(logPanel, BorderLayout.SOUTH);
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
    btnAddAnimal.setEnabled(hasColumn);
    btnDeleteProperty.setEnabled(hasRow);
    btnDeleteAnimal.setEnabled(hasRow);
    chckbxRowSelection.setEnabled(hasRow);
    chckbxColumnSelection.setEnabled(hasRow);
    chckbxListAutoResize.setEnabled(hasRow);
    btnSave.setEnabled(hasColumn);
  }

}
