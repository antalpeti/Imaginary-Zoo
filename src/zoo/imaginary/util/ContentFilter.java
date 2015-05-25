package zoo.imaginary.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * This file responsible to see only xml or csv files in the FileChooser window.
 */
public class ContentFilter extends FileFilter {

  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = FileUtils.getExtension(f);
    if (extension != null) {
      if (extension.equals(FileUtils.XML) || extension.equals(FileUtils.CSV)) {
        return true;
      } else {
        return false;
      }
    }

    return false;
  }

  // The description of this filter
  @Override
  public String getDescription() {
    return "CSV, XML files";
  }
}
