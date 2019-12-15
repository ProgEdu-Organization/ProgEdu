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
    String feedbackEnd = "FAILURE: Build failed with an exception.";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd) + feedbackEnd.length());
    /* UI Test Failure Will occur to differen place */
    return feedback.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      int endIndex = consoleText.length();
      String keyWord = "[31mFAILED \u001B[0m";
      while (consoleText.contains(keyWord)) {
        int failure = consoleText.indexOf(keyWord);
        int nextRow = consoleText.indexOf("\n", failure + keyWord.length() + 1);
        feedbackList.add(new FeedBack(
                StatusEnum.UNIT_TEST_FAILURE,
               "",
                "",
                consoleText.substring(failure + keyWord.length() , nextRow).trim(),
                "",
                ""
        ));
        consoleText = consoleText.substring(nextRow + 1, endIndex);
        endIndex = consoleText.length();
      }
      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      feedbackList.add(
              new FeedBack(StatusEnum.UI_TEST_FAILURE, "","",
                      consoleText, "", ""));
      return feedbackList;
    }
  }
}
