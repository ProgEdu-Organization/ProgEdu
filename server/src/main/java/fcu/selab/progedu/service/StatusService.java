package fcu.selab.progedu.service;

public class StatusService {
  private static StatusService instance = new StatusService();
  private static final String NPM_ERR = "npm ERR! Failed at";

  public static StatusService getInstance() {
    return instance;
  }

  /**
   * Check is Initialization
   * 
   * @param num
   *          num
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
   * @param console console
   * @return boolean
   */
  public boolean isBuildSuccess(String console) {
    boolean isBuildSuccess = false;
    if (console.contains("BUILD SUCCESS")) {
      isBuildSuccess = true;
    }
    return isBuildSuccess;
  }

  /**
   * Check is checkstyle error
   * 
   * @param console
   *          jenkins job console text
   * @return boolean
   */
  public boolean isMavenCheckstyleFailure(String console) {
    boolean isCheckstyleError = false;
    String checkstyleErrorMessage = "Failed during checkstyle execution";
    isCheckstyleError = console.contains(checkstyleErrorMessage);
    return isCheckstyleError;
  }

  /**
   * Check is JUnit error
   *
   * @param console
   *          jenkins job build console
   * @return boolean
   */
  public boolean isMavenUnitTestFailure(String console) {
    boolean isUnitTestError = false;
    if (console.contains("T E S T S") && console.contains("Failed tests")) {
      isUnitTestError = true;
    }
    return isUnitTestError;
  }

  /**
   * Check is JUnit error
   *
   * @param console
   *          jenkins job build console
   * @return boolean
   */
  public boolean isWebUnitTestFailure(String console) {
    boolean isUnitTestError = false;
    if (console.contains(NPM_ERR) && console.contains("test script.")) {
      isUnitTestError = true;
    }
    return isUnitTestError;
  }

//  /**
//   * Check is checkstyle error
//   *
//   * @param console
//   *          jenkins job console text
//   * @return boolean
//   */
//  //Need to Delete in issues#153
//  public boolean isWebCheckstyleFailure(String console) {
//    boolean isCheckstyleError = false;
//    if (console.contains(NPM_ERR) && console.contains("htmlhint script.")) {
//      isCheckstyleError = true;
//    } else if (console.contains(NPM_ERR) && console.contains("stylelint script.")) {
//      isCheckstyleError = true;
//    } else if (console.contains(NPM_ERR) && console.contains("eslint script.")) {
//      isCheckstyleError = true;
//    }
//    return isCheckstyleError;
//  }
//  //Need to Delete in issues#153

  /**
   * Check is html error
   *
   * @param console
   *          jenkins job console text
   * @return boolean
   */
  public boolean isWebHtmlFailure(String console) {
    boolean isHtmlError = false;
    if (console.contains(NPM_ERR) && console.contains("htmlhint script.")) {
      isHtmlError = true;
    }
    return isHtmlError;
  }

  /**
   * Check is css error
   *
   * @param console
   *          jenkins job console text
   * @return boolean
   */
  public boolean isWebStyleFailure(String console) {
    boolean isCssError = false;
    if (console.contains(NPM_ERR) && console.contains("stylelint script.")) {
      isCssError = true;
    }
    return isCssError;
  }

  /**
   * Check is es error
   *
   * @param console
   *          jenkins job console text
   * @return boolean
   */
  public boolean isWebEsFailure(String console) {
    boolean isEsError = false;
    if (console.contains(NPM_ERR) && console.contains("eslint script.")) {
      isEsError = true;
    }
    return isEsError;
  }
}
