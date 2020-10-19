package fcu.selab.progedu.setting;

import java.util.List;

public interface AssignmentSettings {
  public void setZipPath();
  public void setAssignmentPath();
  public String getZipPath();
  public String getAssignmentPath();
  public void unZipAssignmenToTmp();
  public void createAssignmentSetting(List<String> order,String name);
}