package fcu.selab.progedu.setting;

import java.util.List;
import org.apache.maven.model.Model;

public interface AssignmentSettings {

  public void createAssignmentSetting(
      List<String> order, String name, String targetPomSavePath);
}