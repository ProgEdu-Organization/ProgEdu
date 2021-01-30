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
   * if file exist, then will overwrite that file.
   * if not success store, return ""
   *
   * @param file        (to do)
   * @param projectName (to do)
   * @return target
   */
  public String storeFileToUploadsFolder(InputStream file, String projectName) {
    String uploadsDir = System.getProperty("java.io.tmpdir") + "/" +  "uploads";
    File uploadsFolder = new File(uploadsDir);
    if (!uploadsFolder.exists()) {
      uploadsFolder.mkdirs();
    }

    String target = uploadsDir + "/" + projectName;
    try {
      storeFile(file, target);
    } catch (SecurityException | IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "";
    }

    return target;
  }

  /**
   * (to do)
   *
   * @param file     (to do)
   * @param fileName (to do)
   * @return target
   */
  public String storeDescriptionImage(String uploadsDir, String fileName, InputStream file) {
    File uploadsFolder = new File(uploadsDir);
    if (!uploadsFolder.exists()) {
      uploadsFolder.mkdirs();
    }
    String target = uploadsDir + fileName;
    try {
      storeFile(file, target);
    } catch (SecurityException | IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
    return target;
  }

  /**
   * @param originalFilePath originalFilePath
   * @param target           targetPath
   */
  public void copyFileToTarget(String originalFilePath, String target) {
    Linux linux = new Linux();
    String removeFileCommand = "cp " + originalFilePath + " " + target;
    linux.execLinuxCommand(removeFileCommand);
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
   * @return target (to do)
   */
  public String storeWebFileToServer() {

    String fileName = "WebQuickStart.zip";
    String filePath = this.getClass().getResource("/sample/" + fileName).getFile();
    File sample = new File(filePath);
    InputStream file;
    try {
      file = new FileInputStream(sample);
      return storeFileToUploadsFolder(file, fileName);
    } catch (FileNotFoundException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    return "";
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
   * It will be deprecated.
   * @param path (to do)
   */
  public void removeFile(String path) {
    Linux linux = new Linux();
    String removeFileCommand = "rm -rf " + path;
    linux.execLinuxCommand(removeFileCommand);
  }

  /**
   * delete a file.
   *
   * @param file (to do)
   */
  public boolean deleteFile(File file) {
    return file.delete();
  }

  /**
   * Remove a directory
   *
   * @param directory (to do)
   */
  public boolean deleteDirectory(File directory) {
    if (directory.isDirectory() && directory.exists()) {
      String[] fileList = directory.list();

      for (String s : fileList) {
        String subFileStr = directory.getPath() + File.separator + s;
        File subFile = new File(subFileStr);
        if (subFile.isFile()) {
          subFile.delete();
        }
        if (subFile.isDirectory()) {
          deleteDirectory(subFile);
        }
      }

      directory.delete();
    } else {
      return false;
    }
    return true;
  }

  /**
   * Remove file in this directory but not itself.
   * like rm -rf test_directory/*
   *
   * @param directory (to do)
   */
  public boolean deleteFileInDirectory(File directory) {
    boolean isDeleteDirSuccess = deleteDirectory(directory);
    boolean isCreateNewDirectorySuccess = createNewDirectory(directory);
    return (isDeleteDirSuccess && isCreateNewDirectorySuccess);
  }

  /**
   * create a new empty file
   *
   * @param file (to do)
   */
  public boolean createNewFile(File file) {
    try {
      if (file.createNewFile()) {
        return true;
      }
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

    return false;
  }

  /**
   * create a new directory
   *
   * @param directory (to do)
   */
  public boolean createNewDirectory(File directory) {
    return directory.mkdir();
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
