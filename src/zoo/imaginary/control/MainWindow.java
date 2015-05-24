package zoo.imaginary.control;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

import zoo.imaginary.util.ContentFilter;

public class MainWindow {

  private JFrame frame;
  private JTable table;
  private JTextArea log;
  static private final String newline = "\n";
  JFileChooser fc;

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

    JPanel tablePanel = new JPanel();
    frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
    tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    table = new JTable();
    tablePanel.add(table);

    JPanel bottomPanel = new JPanel();
    frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.setLayout(new GridLayout(0, 1, 0, 0));

    JPanel buttonPanel = new JPanel();
    bottomPanel.add(buttonPanel);
    buttonPanel.setLayout(new GridLayout(0, 1, 0, 0));

    JButton btnOpen = new JButton("Open");
    buttonPanel.add(btnOpen);
    btnOpen.setIcon(new ImageIcon(MainWindow.class
        .getResource("/zoo/imaginary/control/images/Open16.gif")));

    log = new JTextArea();
    log.setEnabled(false);
    bottomPanel.add(log);


    btnOpen.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
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
        } else {
          log.append("Open command cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());
      }
    });

  }
}
