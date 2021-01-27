package fcu.selab.progedu.setting;

import fcu.selab.progedu.utils.ZipHandler;
import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.exception.LoadConfigFailureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingZipHandler {
  
  private ZipHandler zipHandler;
  private final String tempDir = System.getProperty("java.io.tmpdir");
  private final String assignmentSettingDir = tempDir + "/assignmentSetting/";
  private String resourcesZipPath;
  private String assignmentPath;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(SettingZipHandler.class);

  /**
   * init assignment zip setting
   * @param resources resources
   * @param name name
   * // Todo 這類別為什麼要傳入作業類別(resources) 跟 作業名子(name), 這跟他的名子意圖沒有關聯
   * // Todo (設定壓縮檔處理程序), 這應該是一個工具, 跟不是他依賴於高層類別, 是高層類別依賴它, 請它做事情而已
   */

  public SettingZipHandler(String resources, String name) {
    try {

      if (resources.equals("maven")) {
        this.resourcesZipPath = "/usr/local/tomcat/webapps/ROOT/resources/MvnQuickStart.zip";
      } else if (resources.equals("web")) {
        this.resourcesZipPath = "/usr/local/tomcat/webapps/ROOT/resources/WebQuickStart.zip";
      }

      assignmentPath = assignmentSettingDir + name;
      zipHandler = new ZipHandler();
    } catch (LoadConfigFailureException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }


  public String getAssignmentPath() {
    return this.assignmentPath;
  }
  
  public void unZipAssignmentToTmp() {
    zipHandler.unzipFile(resourcesZipPath, this.assignmentPath);
  }
  
  public void packUpAssignment() {
    zipHandler.zipTestFolder(this.assignmentPath);
  }
}