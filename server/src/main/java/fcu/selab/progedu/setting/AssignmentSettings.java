package fcu.selab.progedu.setting;

import java.util.List;
import org.apache.maven.model.Model;

public interface AssignmentSettings {

  public void createAssignmentSetting(List<String> order,String name);

  public void unZipAssignmentToTmp(String assignmentName);

  public void packUpAssignment(String assignmentName);

  public void writeAssignmentSettingFile(String assignmentName);
}