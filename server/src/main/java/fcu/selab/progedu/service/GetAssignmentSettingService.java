  
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
    //-------1.unZip
    as.unZipAssignmenToTmp();
    //--------2.change settings
    as.createAssignmentSetting(order, name);
    //---------3.pack up the zip
    as.packUpAssignment();
  }
}