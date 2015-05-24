package zoo.imaginary.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ContentFilter extends FileFilter {

  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = XmlUtils.getExtension(f);
    if (extension != null) {
      if (extension.equals(XmlUtils.xml)) {
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
