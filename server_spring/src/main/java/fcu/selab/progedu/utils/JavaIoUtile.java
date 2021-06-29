package fcu.selab.progedu.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.io.FileInputStream;

////////////////////////////
import java.io.InputStream;
import java.io.OutputStream;

import java.io.FileOutputStream;
import java.io.BufferedWriter;

import java.io.Writer;


public class JavaIoUtile {

  private static final Logger LOGGER = LoggerFactory.getLogger(JavaIoUtile.class);


  /**
   * addFile2EmptyFolder
   *
   * @param rootFile (to do)
   * @param newFileName (to do)
   */
  public static void addFile2EmptyFolder(File rootFile, String newFileName) {
    File dir = rootFile;
    File[] files = dir.listFiles();

    if (dir.exists() && dir.isDirectory()) {

      if (files.length == 0) {
        File newFile = new File(rootFile.getPath() + "/" + newFileName);

        if (!newFile.exists()) {
          try {
            newFile.createNewFile();
          } catch (IOException e) {
            LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
            LOGGER.error(e.getMessage());
          }
        }

      } else {

        for (int i = 0; i < files.length; i++) {
          addFile2EmptyFolder(files[i], newFileName);
        }

      }

    }
  }

  /**
   * Creates a directory if it not already exists
   *
   * @param directory - directory
   */
  public static void createDirectoryIfNotExists(File directory) {
    if (!directory.exists()) {
      directory.mkdir();
    }
  }

  /**
   * Remove a directory
   *
   * @param directory (to do)
   */
  public static boolean deleteDirectory(File directory) {
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
  public static boolean deleteFileInDirectory(File directory) {
    boolean isDeleteDirSuccess = deleteDirectory(directory);
    boolean isCreateNewDirectorySuccess = directory.mkdir();
    return (isDeleteDirSuccess && isCreateNewDirectorySuccess);
  }

  /**
   * (to do)
   *
   * @param content (to do)
   * @param targetFile   (to do)
   */
  public static void createUtf8FileFromString(String content, File targetFile) {
    try (Writer writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(targetFile), "utf-8"));) {
      writer.write(content);
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      // report
    }
  }


  /**
   * (todo)
   * @param source (to do)
   * @param destination (to do)
   */
  public static void copyDirectoryCompatibilityMode(File source, File destination)
          throws IOException {
    if (source.isDirectory()) {
      copyDirectory(source, destination);
    } else {
      copyFile(source, destination);
    }
  }

  private static void copyDirectory(File sourceDirectory, File destinationDirectory)
          throws IOException {
    if (!destinationDirectory.exists()) {
      destinationDirectory.mkdir();
    }
    for (String filePath : sourceDirectory.list()) {
      copyDirectoryCompatibilityMode(new File(sourceDirectory, filePath),
              new File(destinationDirectory, filePath));
    }
  }

  private static void copyFile(File sourceFile, File destinationFile)
          throws IOException {
    try (InputStream in = new FileInputStream(sourceFile);
         OutputStream out = new FileOutputStream(destinationFile)) {
      byte[] buf = new byte[1024];
      int length;
      while ((length = in.read(buf)) > 0) {
        out.write(buf, 0, length);
      }
    }
  }

  public static String readFileToString(File file) throws Exception {
    return FileUtils.readFileToString(file, "UTF-8");
  }

}
