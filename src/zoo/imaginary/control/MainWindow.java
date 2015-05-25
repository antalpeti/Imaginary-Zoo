package zoo.imaginary.control;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableColumn;

import zoo.imaginary.util.ContentFilter;

public class MainWindow {

  private JFrame frame;
  private JTable table;
  private JTextArea log;
  static private final String newline = "\n";
  JFileChooser fc;
  private JButton btnOpen;
  private JButton btnAddColumn;

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
    frame = new JFrame();
    frame.setBounds(100, 100, 698, 512);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    table = new JTable();
    JScrollPane tScrollPane = new JScrollPane(table);
    frame.getContentPane().add(tScrollPane, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel();
    frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.setLayout(new GridLayout(0, 1, 0, 0));

    JPanel buttonPanel = new JPanel();
    bottomPanel.add(buttonPanel);
    buttonPanel.setLayout(new GridLayout(0, 2, 0, 0));

    btnOpen = new JButton("Open");
    buttonPanel.add(btnOpen);
    btnOpen.setIcon(new ImageIcon(MainWindow.class
        .getResource("/zoo/imaginary/control/images/Open16.gif")));

    btnAddColumn = new JButton("Add Column");
    buttonPanel.add(btnAddColumn);

    log = new JTextArea();
    log.setEditable(false);
    JScrollPane taScrollPane = new JScrollPane(log);
    bottomPanel.add(taScrollPane);

    ActionListener buttonsActionListener = new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        TableModel model = null;

        if (e.getSource() == btnOpen) {
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
            log.append("Opening: " + file.getName() + "." + newline);
            model = new TableModel(file);
            table.setModel(model);
          } else {
            log.append("Open command cancelled by user." + newline);
          }
          log.setCaretPosition(log.getDocument().getLength());
        } else if (e.getSource() == btnAddColumn) {
          if (table.getModel().getColumnCount() != 0) {
            String columnName = JOptionPane.showInputDialog("Please enter the column name: ");
            TableColumn tableColumn = new TableColumn();
            tableColumn.setHeaderValue(columnName);
            table.getColumnModel().addColumn(tableColumn);
          } else {
            JOptionPane.showMessageDialog(frame, "First load the table content from a file.",
                "Content error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    };
    btnOpen.addActionListener(buttonsActionListener);
    btnAddColumn.addActionListener(buttonsActionListener);

  }
}
