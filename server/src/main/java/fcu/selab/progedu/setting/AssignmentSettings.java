package fcu.selab.progedu.setting;

import java.util.List;
import org.apache.maven.model.Model;

public interface AssignmentSettings {

  // Todo 以下改成 void createAssignmentSetting(List<String> order, String name, String targetPomSavePath);
  // Todo targetPath 是即將要生成的 pom.xml檔 儲存路徑, 這只需要pom檔就好, 整份專案只有在下載時在去組合, 所以不用解壓所整份專案
  // Todo 到 assignmentSetting 資料夾
  public void createAssignmentSetting(List<String> order,String name);

  // Todo 刪掉以下這行
  public void unZipAssignmentToTmp();

  // Todo 刪掉以下這行
  public void packUpAssignment();

  // Todo 刪掉以下這行
  public void writeAssignmentSettingFile();
}