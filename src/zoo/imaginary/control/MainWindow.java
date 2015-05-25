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
import javax.swing.table.TableColumn;

import zoo.imaginary.util.ContentFilter;
import zoo.imaginary.util.FileUtils;

public class MainWindow {

  private JFrame frame;
  private JTable table;
  private JTextArea log;
  JFileChooser fc;
  private JButton btnOpen;
  private JButton btnAddColumn;
  private JPanel controlPanel;
  private JCheckBox chckbxRowSelection;
  private JCheckBox chckbxColumnSelection;
  private JPanel searchPanel;
  private JTextField txtSearch;
  private JComboBox searchComboBox;
  private JButton btnSearch;
  private JPanel logPanel;

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

    createBottomPanel();

    createControlPanel();

  }

  /**
   * Create table with scroll pane.
   */
  protected void createTable() {
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
    btnAddColumn = new JButton("Add Column");
    btnAddColumn.setEnabled(false);
    btnAddColumn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.getModel().getColumnCount() != 0) {
          String columnName = JOptionPane.showInputDialog("Please enter the column name: ");
          TableColumn tableColumn = new TableColumn();
          tableColumn.setHeaderValue(columnName);
          table.getColumnModel().addColumn(tableColumn);
        }
      }
    });
    controlPanel.add(btnAddColumn);

    // Row Selection checkbox
    chckbxRowSelection = new JCheckBox("Row Selection");
    chckbxRowSelection.setSelected(true);
    chckbxRowSelection.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        table.setRowSelectionAllowed(chckbxRowSelection.isSelected());
      }
    });
    chckbxRowSelection.setEnabled(false);
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
   * Create bottom panel with buttons and log.
   */
  protected void createBottomPanel() {
    JPanel bottomPanel = new JPanel();
    frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.setLayout(new GridLayout(2, 1, 0, 0));


    JPanel buttonPanel = new JPanel();
    bottomPanel.add(buttonPanel);
    buttonPanel.setLayout(new GridLayout(0, 1, 0, 0));

    // Open button
    btnOpen = new JButton("Open");
    buttonPanel.add(btnOpen);
    btnOpen.setIcon(new ImageIcon(MainWindow.class
        .getResource("/zoo/imaginary/control/images/Open16.gif")));

    // Log scroll pane
    log = new JTextArea();
    log.setEditable(false);
    JScrollPane taScrollPane = new JScrollPane(log);
    bottomPanel.add(taScrollPane);

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

          // Enable/Disable widgets of the control panel
          boolean tableIsEmpty = table.getColumnCount() == 0;
          chckbxRowSelection.setEnabled(!tableIsEmpty);
          chckbxColumnSelection.setEnabled(!tableIsEmpty);
          btnAddColumn.setEnabled(!tableIsEmpty);
        } else {
          log.append("Open command cancelled by user." + "\n");
        }
        log.setCaretPosition(log.getDocument().getLength());

      }
    });
  }

}
