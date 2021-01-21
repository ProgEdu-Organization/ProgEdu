package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import java.util.ArrayList;

public class BuildSuccess implements Status {

  // Todo 提取Failure 怎麼會有 Success
  @Override
  public String extractFailureMsg(String consoleText) {
    return "Success";
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    feedbacklist.add(new FeedBack(StatusEnum.BUILD_SUCCESS, consoleText));
    return feedbacklist;
  }
}
