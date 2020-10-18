package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.setting.AssignmentSettings;

public class GetAssignmentSettingService {

  public void getSetting(AssignmentSettings as, List<String> order,String name) {
    as.createAssignmentSetting(order, name);
  }
}