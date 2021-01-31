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
   * @param path (to do)
   * @param newFileName (to do)
   */
  public static void addFile2EmptyFolder(String path, String newFileName) {
    File dir = new File(path);
    File[] files = dir.listFiles();

    if (dir.exists() && dir.isDirectory()) {

      if (files.length == 0) {
        File newFile = new File(path + "/" + newFileName);

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
          addFile2EmptyFolder(files[i].getPath(), newFileName);
        }

      }

    }
  }



}
