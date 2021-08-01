package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidUnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "testDebugUnitTest FAILED";
    String feedbackEnd = "FAILURE: Build failed with an exception.";
    return (consoleText.substring(consoleText.indexOf(feedbackStart) + feedbackStart.length(),
        consoleText.indexOf(feedbackEnd))).trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      int endIndex = consoleText.length();
      while (consoleText.contains("FAILED")) {
        int error = consoleText.indexOf(">");
        int failed = consoleText.indexOf("FAILED");
        int nextRow = consoleText.indexOf("\n");

        feedbackList.add(new FeedBack(
            StatusEnum.UNIT_TEST_FAILURE,
            consoleText.substring(0, error).trim(),
            "",
            consoleText.substring(error + ">".length(), failed).trim(),
            consoleText.substring(failed, nextRow).trim(),
            ""
        ));
        consoleText = consoleText.substring(nextRow + 1, endIndex);
        endIndex = consoleText.length();
      }
      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      feedbackList.add(
          new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "", "",
              consoleText, "", ""));
      return feedbackList;
    }
  }
}
