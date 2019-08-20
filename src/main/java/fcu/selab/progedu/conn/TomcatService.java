package fcu.selab.progedu.conn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import fcu.selab.progedu.project.ProjectType;
import fcu.selab.progedu.utils.Linux;

public class TomcatService {

  private static TomcatService instance = new TomcatService();

  public static TomcatService getInstance() {
    return instance;
  }

  /**
   * (to do)
   * @param file (to do)
   * @param target (to do)
   * @return target (to do)
   */
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

  /**
   * (to do)
   * @param file (to do)
   * @param folderName (to do)
   * @param uploadDir (to do)
   * @param project (to do)
   * @return target (to do)
   */
  public String storeFileToServer(InputStream file, String folderName, String uploadDir,
      ProjectType project) {
    String target;
    if (hasTemplate(folderName)) {
      target = uploadDir + folderName;
      // store to C://User/AppData/Temp/uploads/
      storeFileToUploadsFolder(file, target);
    } else {
      target = this.getClass().getResource(project.getSampleTemplate()).getFile();
    }
    return target;

  }

  /**
   * (to do)
   * @param folderName (to do)
   * @return hasTemplate (to do)
   */
  public boolean hasTemplate(String folderName) {
    boolean hasTemplate = false;
    if (!folderName.isEmpty()) {
      hasTemplate = true;
    }
    return hasTemplate;
  }

  /**
   * (to do)
   * @param path (to do)
   */
  public void findEmptyFolder(String path) {
    File dir = new File(path);
    File[] files = dir.listFiles();

    if (dir.exists() && dir.isDirectory()) {
      if (files.length == 0) {
        addGitkeep(path);
      } else {
        for (int i = 0; i < files.length; i++) {
          findEmptyFolder(files[i].getPath());
        }
      }
    }
  }

  private void addGitkeep(String path) {
    File gitkeep = new File(path + "/.gitkeep");
    if (!gitkeep.exists()) {
      try {
        gitkeep.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * (to do)
   * @param path (to do)
   */
  public void removeFile(String path) {
    Linux linux = new Linux();
    String removeFileCommand = "rm -rf " + path;
    linux.execLinuxCommand(removeFileCommand);
  }

  /**
   * (to do)
   * @param readMe (to do)
   * @param path (to do)
   */
  public void createReadmeFile(String readMe, String path) {
    try (Writer writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(path + "/README.md"), "utf-8"));) {
      writer.write(readMe);
    } catch (IOException e) {
      e.printStackTrace();
      // report
    }
  }

  /**
   * get server current time
   * 
   * @return current time
   */
  public String getCurrentTime() {
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    format.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
    return format.format(date);

  }
}
