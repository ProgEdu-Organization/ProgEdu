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

  public static void main(String []args) {
    CommitRecordService c = new CommitRecordService();
    System.out.print(c.getFeedback("M0806615", "HW1", 15).getEntity().toString());
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      int endIndex = consoleText.length();
      String keyWord = "[31mFAILED \u001B[0m";
      while (consoleText.contains(keyWord)) {
        int failure = consoleText.indexOf(keyWord);
        int nextrow = consoleText.indexOf("\n", failure + keyWord.length() +1);

        feedbacklist.add(new FeedBack(
                StatusEnum.UNIT_TEST_FAILURE,
               "",
                consoleText.substring(failure + keyWord.length() , nextrow).trim(),
                "",
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
