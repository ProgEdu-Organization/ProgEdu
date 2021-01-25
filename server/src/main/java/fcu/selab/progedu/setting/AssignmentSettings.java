package fcu.selab.progedu.setting;

import java.util.List;
import org.apache.maven.model.Model;

public interface AssignmentSettings {

  public Model createAssignmentSetting(List<String> order,String name);

  public void unZipAssignmentToTmp();

  public void packUpAssignment();

  public void writeAssignmentSettingFile(Model model);
}