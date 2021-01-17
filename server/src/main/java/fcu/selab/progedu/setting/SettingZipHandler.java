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
  private String assignmentName;
  private String assignmentPath;
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
      this.resourcesZipPath = "/usr/local/tomcat/webapps/ROOT/resources/MvnQuickStart.zip";
    } else if (resources.equals("web")) {
      this.resourcesZipPath = "/usr/local/tomcat/webapps/ROOT/resources/WebQuickStart.zip";
    }
  }

  public String getResourcesZipPath() {
    return this.resourcesZipPath;
  }
  
  public void setAssignmentName(String name) {
    this.assignmentName = name;
  }
  
  public String getAssignmentName() {
    return this.assignmentName;
  }

  public void setAssignmentPath() {
    this.assignmentPath = this.settingDir + getAssignmentName();
  }
  
  public String getAssignmentPath() {
    return this.assignmentPath;
  }
  
  public void unZipAssignmenToTmp() {
    zipHandler.unzipFile(this.getResourcesZipPath(), this.getAssignmentPath());
  }
  
  public void packUpAssignment() {
    zipHandler.zipTestFolder(getAssignmentPath());
  }
}