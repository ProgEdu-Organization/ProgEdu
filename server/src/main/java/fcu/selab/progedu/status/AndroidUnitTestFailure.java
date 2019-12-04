package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidUnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "testDebugUnitTest FAILED";
    String feedbackEnd = "FAILURE: Build failed with an exception.";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart) + feedbackStart.length(),
            consoleText.indexOf(feedbackEnd));
    return feedback.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      int endIndex = consoleText.length();
      while (consoleText.contains("FAILED")) {
        int error = consoleText.indexOf(">");
        int failed = consoleText.indexOf("FAILED");
        int nextrow = consoleText.indexOf("\n");

        feedbacklist.add(new FeedBack(
                StatusEnum.UNIT_TEST_FAILURE,
                consoleText.substring(0, error).trim(),
                consoleText.substring(error + ">".length(), failed).trim(),
                consoleText.substring(failed, nextrow).trim(),
                "https://github.com/checkstyle/checkstyle"
        ));
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      }
      return feedbacklist;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      feedbacklist.add(
              new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "",
                      consoleText, "", ""));
      return feedbacklist;
    }
  }
}
