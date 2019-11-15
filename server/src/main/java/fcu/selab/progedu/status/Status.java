package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public interface Status {
  public String extractFailureMsg(String consoleText);

  public ArrayList<FeedBack> formatExamineMsg(String consoleText);

  public String toJson(ArrayList<FeedBack> arrayList);

}
