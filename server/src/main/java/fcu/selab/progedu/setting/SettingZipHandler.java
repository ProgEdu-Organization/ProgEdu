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
  private static String ResourcesZipPath;
  private static String AssignmentName;
  private static String AssignmentPath;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(SettingZipHandler.class);

  /**
   * init assignment zip setting
   * @param resources resources
   * @param name name
   */
  public SettingZipHandler(String resources, String name) {
    try {
      setAssignmentName(name);
      setResourcesZipPath(resources);
      setAssignmentPath();
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
  public void setResourcesZipPath(String resources) {
    if (resources.equals("maven")) {
      this.ResourcesZipPath = "/usr/local/tomcat/webapps/ROOT/resources/MvnQuickStart.zip";
    }
  }

  public String getResourcesZipPath() {
    return this.ResourcesZipPath;
  }
  
  public void setAssignmentName(String name) {
    this.AssignmentName = name;
  }
  
  public String getAssignmentName() {
    return this.AssignmentName;
  }

  public void setAssignmentPath() {
    this.AssignmentPath = this.settingDir + getAssignmentName();
  }
  
  public String getAssignmentPath() {
    return this.AssignmentPath;
  }
  
  public void unZipAssignmenToTmp() {
    zipHandler.unzipFile(this.getResourcesZipPath(), this.getAssignmentPath());
  }
  
  public void packUpAssignment() {
    zipHandler.zipTestFolder(getAssignmentPath());
  }
}