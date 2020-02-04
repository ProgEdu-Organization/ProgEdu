package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidCheckstyleFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "Task :app:checkStyle";
    String feedbackEnd = "FAILURE: Build failed with an exception.";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart) + feedbackStart.length(),
        consoleText.indexOf(feedbackEnd));
    /**
     * Remove /var/jenkins_home/workspace/
     */
    return feedback.replaceAll("/var/jenkins_home/workspace", "").trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      int endIndex = consoleText.length();
      while (consoleText.contains("[ERROR]")) {
        int error = consoleText.indexOf("[ERROR]") + "[ERROR]".length();
        int nextRow = consoleText.indexOf("\n", error);
        int space = consoleText.indexOf(" ", error + 1);
        if (nextRow == -1 || space == -1) {
          break;
        }
        // Find the last left bracket for Symptom
        int lastLeftBracket = consoleText.indexOf(" [", space);
        int lastRightBracket = consoleText.indexOf(" ]", lastLeftBracket);
        int period = consoleText.indexOf(".", space);
        String fileNameAndLine = consoleText.substring(error + 1, space - 1).trim();
        feedbackList.add(new FeedBack(
            StatusEnum.CHECKSTYLE_FAILURE,
            fileNameAndLine.substring(0, fileNameAndLine.indexOf(":")).trim(),
            fileNameAndLine.substring(fileNameAndLine.indexOf(":") + 1,
                fileNameAndLine.length()).trim(),
            consoleText.substring(space + 1, period).trim(),
            consoleText.substring(period + 1, nextRow).trim(),
            "https://github.com/checkstyle/checkstyle"
        ));

        consoleText = consoleText.substring(nextRow + 1, endIndex);
        endIndex = consoleText.length();
      }

      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      feedbackList.add(
          new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "", "",
              "Coding Style ArrayList error", "", ""));
      return feedbackList;
    }
  }
}
