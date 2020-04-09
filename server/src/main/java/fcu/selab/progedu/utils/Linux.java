package fcu.selab.progedu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fcu.selab.progedu.utils.ExceptionUtil;

public class Linux {

  private static Linux instance = new Linux();

  public static Linux getInstance() {
    return instance;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(Linux.class);

  /**
   * execLinuxCommand
   *
   * @param command Linux command
   */
  public void execLinuxCommand(String command) {
    Process process;

    try {
      process = Runtime.getRuntime().exec(command);
      BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while (true) {
        line = br.readLine();
        if (line == null) {
          break;
        }
      }
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }

  }

  /**
   * execLinuxCommandInFile
   *
   * @param command  Linux command
   * @param filePath path
   */
  public void execLinuxCommandInFile(String[] command, String filePath) {
    Process process;
    String line;
    try {
      process = Runtime.getRuntime().exec(command, null, new File(filePath));
      BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

      while (true) {
        // command output
        line = br.readLine();
        if (line == null) {
          break;
        }
      }
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }
}
