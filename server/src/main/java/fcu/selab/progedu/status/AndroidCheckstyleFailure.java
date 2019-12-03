package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidCheckstyleFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "Task :app:checkStyle";
    String feedbackEnd = "> Task :app:checkStyle FAILED";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd));
    return feedback.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    feedbacklist.add(
            new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "",
                    consoleText, "", ""));
    return feedbacklist;
  }
}