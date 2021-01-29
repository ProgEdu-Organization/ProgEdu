package fcu.selab.progedu.setting;

import fcu.selab.progedu.utils.ZipHandler;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.exception.LoadConfigFailureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingZipHandler {
  
  private ZipHandler zipHandler;
  private final String tempDir = System.getProperty("java.io.tmpdir");
  private final String settingDir = tempDir + "/assignmentSetting/";
  private String resourcesZipPath;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(SettingZipHandler.class);

  /**
   * init assignment zip setting
   */
  public SettingZipHandler() {
    try {
      zipHandler = new ZipHandler();
    } catch (LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * set assignment zip path
   * @param resources which resources
   */
  private void setResourcesZipPath(String resources) {
    if (resources.equals("maven")) {
      this.resourcesZipPath = "/usr/local/tomcat/webapps/ROOT/resources/MvnQuickStart.zip";
    } else if (resources.equals("web")) {
      this.resourcesZipPath = "/usr/local/tomcat/webapps/ROOT/resources/WebQuickStart.zip";
    }
  }

  /**
   * get assignment path
   * @param assignmentName assignment
   */
  public String getAssignmentPath(String assignmentName) {
    String assignmentPath = settingDir + assignmentName;
    return assignmentPath;
  }

  /**
   * unzip assignment to temp
   * @param resources      which resources
   * @param assignmentName Assignment Name
   */
  public void unZipAssignmentToTmp(String resources, String assignmentName) {
    setResourcesZipPath(resources);
    zipHandler.unzipFile(resourcesZipPath, getAssignmentPath(assignmentName));
  }

  /**
   * pack up assignment
   * @param assignmentName Assignment Name
   */
  public void packUpAssignment(String assignmentName) {
    zipHandler.zipTestFolder(getAssignmentPath(assignmentName));
  }
}