package fcu.selab.progedu.service;

public class StatusService {
  private static StatusService instance = new StatusService();

  public static StatusService getInstance() {
    return instance;
  }

  /**
   * Check is Initialization
   * 
   * @param num num
   * @return boolean
   */
  public boolean isInitialization(int num) {
    boolean isInitialization = false;
    if (num == 1) {
      isInitialization = true;
    }
    return isInitialization;
  }

  /**
   * Check is build success
   * 
   * @param result result
   * @return boolean
   */
  public boolean isBuildSuccess(String console) {
    boolean isBuildSuccess = false;
    if (console.contains("Finished: SUCCESS")) {
      isBuildSuccess = true;
    }
    return isBuildSuccess;
  }

  /**
   * Check is checkstyle error
   * 
   * @param consoleText jenkins job console text
   * @return boolean
   */
  public boolean isMavenCheckstyleFailure(String consoleText) {
    boolean isCheckstyleError = false;
    String checkstyleErrorMessage = "Failed during checkstyle execution";
    isCheckstyleError = consoleText.contains(checkstyleErrorMessage);
    return isCheckstyleError;
  }

  /**
   * Check is JUnit error
   *
   * @param console jenkins job build console
   * @return boolean
   */
  public boolean isMavenUnitTestFailure(String console) {
    boolean isUnitTestError = false;
    if (console.contains("T E S T S") && console.contains("Failed tests")) {
      isUnitTestError = true;
    }
    return isUnitTestError;
  }
}
