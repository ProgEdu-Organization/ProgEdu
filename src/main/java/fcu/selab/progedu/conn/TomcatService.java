package fcu.selab.progedu.conn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fcu.selab.progedu.service.AssignmentTypeSelector;

public class TomcatService {
  public String storeFileToUploadsFolder(InputStream file, String target) {
    try {
      createFolderIfNotExists(target);
      storeFile(file, target);
    } catch (SecurityException | IOException e) {
      e.printStackTrace();
    }
    return target;
  }

  /**
   * Creates a folder to desired location if it not already exists
   * 
   * @param dirName - full path to the folder
   * @throws SecurityException - in case you don't have permission to create the
   *                           folder
   */
  private void createFolderIfNotExists(String dirName) {
    File theDir = new File(dirName);
    if (!theDir.exists()) {
      theDir.mkdir();
    }
  }

  /**
   * Utility method to save InputStream data to target location/file
   * 
   * @param file   - InputStream to be saved
   * @param target - full path to destination file
   */
  private void storeFile(InputStream file, String target) throws IOException {
    int read = 0;
    byte[] bytes = new byte[1024];
    try (OutputStream out = new FileOutputStream(new File(target));) {
      while ((read = file.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String storeFile(InputStream file, String folderName, String uploadDir,
      AssignmentTypeSelector assignmentTypeSelector) {
    String target;
    if (hasTemplate(folderName)) {
      target = uploadDir + folderName;
      // store to C://User/AppData/Temp/uploads/
      storeFileToUploadsFolder(file, target);
    } else {
      target = this.getClass().getResource(assignmentTypeSelector.getSampleZip()).getFile();
    }
    return target;

  }

  public boolean hasTemplate(String folderName) {
    boolean hasTemplate = false;
    if (!folderName.isEmpty()) {
      hasTemplate = true;
    }
    return hasTemplate;
  }
}
