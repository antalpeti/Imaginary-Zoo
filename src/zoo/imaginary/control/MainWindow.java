package zoo.imaginary.control;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

import zoo.imaginary.util.ContentFilter;
import zoo.imaginary.util.FileUtils;

public class MainWindow {

  private JFrame frame;
  private JTable table;
  private JTextArea log;
  JFileChooser fc;
  private JButton btnOpen;
  private JButton btnAddProperty;
  private JPanel controlPanel;
  private JCheckBox chckbxRowSelection;
  private JCheckBox chckbxColumnSelection;
  private JPanel searchPanel;
  private JTextField txtSearch;
  private JComboBox searchComboBox;
  private JButton btnSearch;
  private JPanel logPanel;
  private JButton btnDeleteProperty;
  private JButton btnDeleteEntity;
  private JButton btnAddEntity;

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
    frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
    searchPanel.setLayout(new GridLayout(0, 3, 0, 0));

    // Search text
    txtSearch = new JTextField();
    txtSearch.setToolTipText("");
    searchPanel.add(txtSearch);

    // Search checkbox
    searchComboBox = new JComboBox();
    searchPanel.add(searchComboBox);

    // Search button
    btnSearch = new JButton("Search");
    searchPanel.add(btnSearch);
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
        String columnName = JOptionPane.showInputDialog("Please enter the column name: ");
        if (columnName != null && !columnName.isEmpty()) {
          ((TableModel) table.getModel()).addColumn(columnName);
        }
        enableDisableControls();
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
          enableDisableControls();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select a column.", "Deletion warning",
              JOptionPane.WARNING_MESSAGE);
        }
      }
    });

    // Add Row button
    btnAddEntity = new JButton("Add Entity");
    btnAddEntity.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.getColumnCount() > 0) {
          ((TableModel) table.getModel()).addRow(new Object[table.getColumnCount()]);
          enableDisableControls();
        } else {
          JOptionPane.showMessageDialog(frame, "Please add column first.", "Row creation warning",
              JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    btnAddEntity.setEnabled(false);
    controlPanel.add(btnAddEntity);
    btnDeleteProperty.setEnabled(false);
    controlPanel.add(btnDeleteProperty);

    // Delete Row button
    btnDeleteEntity = new JButton("Delete Entity");
    btnDeleteEntity.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println();
        if (table.getSelectedRow() != -1) {
          int[] selectedRows = table.getSelectedRows();
          ((TableModel) table.getModel()).deleteRows(selectedRows);
          enableDisableControls();
        } else {
          JOptionPane.showMessageDialog(frame, "Please select a row.", "Deletion warning",
              JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    btnDeleteEntity.setEnabled(false);
    controlPanel.add(btnDeleteEntity);

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
    buttonPanel.add(btnOpen);
    btnOpen.setIcon(new ImageIcon(MainWindow.class
        .getResource("/zoo/imaginary/control/images/Open16.gif")));

    btnOpen.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TableModel model = null;
        if (fc == null) {
          fc = new JFileChooser();
          fc.addChoosableFileFilter(new ContentFilter());
          fc.setAcceptAllFileFilterUsed(true);
          File currentDirectory = new File(".");
          fc.setCurrentDirectory(currentDirectory);
          fc.setDialogTitle("Open Database");
        }

        int returnVal = fc.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          // This is where a real application would open the file.
          log.append("Opening: " + file.getName() + "." + "\n");
          model = new TableModel(file, FileUtils.getExtension(file));
          table.setModel(model);

          enableDisableControls();
        } else {
          log.append("Open command cancelled by user." + "\n");
        }
        log.setCaretPosition(log.getDocument().getLength());

      }

    });
  }

  /**
   * Create bottom panel with log.
   */
  private void createLogPanel() {
    JPanel logPanel_1 = new JPanel();
    frame.getContentPane().add(logPanel_1, BorderLayout.SOUTH);
    logPanel_1.setLayout(new GridLayout(0, 1, 0, 0));

    // Log scroll pane
    log = new JTextArea();
    log.setEditable(false);
    JScrollPane taScrollPane = new JScrollPane(log);
    logPanel_1.add(taScrollPane);
  }

  /**
   * Enable or disable the control widgets according to the content of column and row of the table.
   */
  private void enableDisableControls() {
    // Enable/Disable widgets of the control panel
    boolean hasColumn = table.getColumnCount() > 0;
    boolean hasRow = table.getRowCount() > 0;
    btnAddEntity.setEnabled(hasColumn);
    btnDeleteProperty.setEnabled(hasRow);
    btnDeleteEntity.setEnabled(hasRow);
    chckbxRowSelection.setEnabled(hasRow);
    chckbxColumnSelection.setEnabled(hasRow);
  }

}
