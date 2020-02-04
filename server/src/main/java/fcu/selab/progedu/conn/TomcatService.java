package fcu.selab.progedu.conn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.project.ProjectType;
import fcu.selab.progedu.utils.Linux;
import fcu.selab.progedu.utils.ExceptionUtil;

public class TomcatService {

  private static TomcatService instance = new TomcatService();

  private static final Logger LOGGER = LoggerFactory.getLogger(TomcatService.class);

  public static TomcatService getInstance() {
    return instance;
  }

  /**
   * (to do)
   * 
   * @param file        (to do)
   * @param projectName (to do)
   * @return target
   */
  public String storeFileToUploadsFolder(InputStream file, String projectName) {
    String uploadsDir = System.getProperty("java.io.tmpdir") + "/uploads/";
    File uploadsFolder = new File(uploadsDir);
    if (!uploadsFolder.exists()) {
      uploadsFolder.mkdirs();
    }
    String target = uploadsDir + projectName;
    try {
      storeFile(file, target);
    } catch (SecurityException | IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
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
   * @param input  - InputStream to be saved
   * @param target - full path to destination file
   */
  private void storeFile(InputStream input, String target) throws IOException {
    OutputStream output = new FileOutputStream(target);
    IOUtils.copy(input, output);
  }

  /**
   * (to do)
   * 
   * @param file       (to do)
   * @param fileDetail (to do)
   * @param project    (to do)
   * @return target (to do)
   */
  public String storeFileToServer(InputStream file, FormDataContentDisposition fileDetail,
      ProjectType project) {
    String fileName;
    if (!hasTemplate(fileDetail)) {
      fileName = project.getSampleTemplate();
      String filePath = this.getClass().getResource("/sample/" + fileName).getFile();
      File sample = new File(filePath);
      try {
        file = new FileInputStream(sample);
      } catch (FileNotFoundException e) {
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
        LOGGER.error(e.getMessage());
      }
    } else {
      fileName = fileDetail.getFileName();
    }
    return storeFileToUploadsFolder(file, fileName);

  }

  /**
   * (to do)
   * 
   * @param fileDetail (to do)
   * @return hasTemplate (to do)
   */
  public boolean hasTemplate(FormDataContentDisposition fileDetail) {
    boolean hasTemplate = false;
    if (fileDetail != null && StringUtils.isNotEmpty(fileDetail.getFileName())) {
      hasTemplate = true;
    }
    return hasTemplate;
  }

  /**
   * (to do)
   * 
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
        if (!gitkeep.createNewFile()) {
          LOGGER.debug("gitkeep had existed in path : " + path);
          LOGGER.error("gitkeep had existed in path : " + path);
        }
      } catch (IOException e) {
        LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
        LOGGER.error(e.getMessage());
      }
    }
  }

  /**
   * (to do)
   * 
   * @param path (to do)
   */
  public void removeFile(String path) {
    Linux linux = new Linux();
    String removeFileCommand = "rm -rf " + path;
    linux.execLinuxCommand(removeFileCommand);
  }

  /**
   * (to do)
   * 
   * @param readMe (to do)
   * @param path   (to do)
   */
  public void createReadmeFile(String readMe, String path) {
    try (Writer writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(path + "/README.md"), "utf-8"));) {
      writer.write(readMe);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      // report
    }
  }

  /**
   * get server current time
   * 
   * @return current time
   */
  public Date getCurrentTime() {
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    format.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
    try {
      date = format.parse(format.format(date));
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return date;

  }
}
