package fcu.selab.progedu.utils;

import fcu.selab.progedu.conn.TomcatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

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





}
