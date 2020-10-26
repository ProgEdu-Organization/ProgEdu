package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.setting.AssignmentSettings;

public class GetAssignmentSettingService {

  /**
   * Setting making Assignment setting
   * 
   * @param as AssignmentSettings
   * @param order order list
   * @param name name
   */
  public void getSetting(AssignmentSettings as, List<String> order,String name) {
    //-------unZip
    as.unZipAssignmenToTmp();
    //------------get settings
    as.createAssignmentSetting(order, name);
    //--------------pack up the zip
    as.packUpAssignment();
  }
}