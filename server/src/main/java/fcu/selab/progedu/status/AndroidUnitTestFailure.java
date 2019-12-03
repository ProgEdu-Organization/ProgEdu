package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidUnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "> Task :app:testDebugUnitTest FAILED";
    String feedbackEnd = "FAILURE: Build failed with an exception.";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd));
    return feedback.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    feedbacklist.add(
            new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "",
                    consoleText, "", ""));
    return feedbacklist;
  }
}