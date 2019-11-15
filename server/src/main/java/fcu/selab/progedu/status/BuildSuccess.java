package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import javax.json.Json;
import java.util.ArrayList;

public class BuildSuccess implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    return "Success";
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    return null;
  }

  @Override
  public String toJson(ArrayList<FeedBack> arrayList) {
    return "Success";
  }

}
