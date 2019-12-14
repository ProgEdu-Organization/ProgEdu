package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidCheckstyleFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "checkStyle FAILED";
    String feedbackEnd = "FAILURE: Build failed with an exception.";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart) + feedbackStart.length(),
            consoleText.indexOf(feedbackEnd) + 1);
    /**
     * Remove /var/jenkins_home/workspace/
     */
    feedback = feedback.replaceAll("/var/jenkins_home/workspace/", "");
    return feedback.replaceAll("/var/jenkins_home/workspace/", "").trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      int endIndex = consoleText.length();
      while (consoleText.contains("[ERROR]")) {
        int error = consoleText.indexOf("[ERROR]") + "[ERROR]".length() ;
        int nextRow = consoleText.indexOf("\n");
        int space = consoleText.indexOf(" ", error + 1);
        // Find the last left bracket for Symptom
        int lastLeftBracket = consoleText.indexOf("[", space);
        while (consoleText.indexOf("[", lastLeftBracket + 1) > 0
                && consoleText.indexOf("[", lastLeftBracket + 1) < nextRow) {
          lastLeftBracket = consoleText.indexOf("[", lastLeftBracket + 1);
        }
        feedbackList.add(new FeedBack(
                StatusEnum.CHECKSTYLE_FAILURE,
                "",
                consoleText.substring(error + 1, space - 1).trim(),
                consoleText.substring(space + 1, lastLeftBracket).trim(),
                consoleText.substring(lastLeftBracket, nextRow).trim(),
                "https://github.com/checkstyle/checkstyle"
        ));
        consoleText = consoleText.substring(nextRow + 1, endIndex);
        endIndex = endIndex - nextRow - 1;
      }
      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      feedbackList.add(
              new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "","",
                      consoleText, "", ""));
      return feedbackList;
    }
  }
}
