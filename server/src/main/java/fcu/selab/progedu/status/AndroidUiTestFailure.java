package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.service.CommitRecordService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidUiTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "connectedDebugAndroidTest";
    String feedbackEnd = "connectedDebugAndroidTest FAILED";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd) + feedbackEnd.length());
    int countMatches = StringUtils.countMatches(feedback, feedbackStart);

    for (int i = 0; i < countMatches - 1; i++) {
      if (i == (countMatches - 2)) {
        feedback = feedback.substring(feedback.indexOf(feedbackStart) + feedbackStart.length(),
                feedback.indexOf(feedbackEnd));
      } else {
        feedback = feedback.substring(feedback.indexOf(feedbackStart) + feedbackStart.length(),
                feedback.indexOf(feedbackEnd) + feedbackEnd.length());
      }
    }
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
        int nextrow = consoleText.indexOf("\n", consoleText.indexOf(".java"));

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
              new FeedBack(StatusEnum.UI_TEST_FAILURE, "",
                      consoleText, "", ""));
      return feedbacklist;
    }
  }
}
