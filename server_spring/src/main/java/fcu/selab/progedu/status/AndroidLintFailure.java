package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidLintFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "Errors found:";
    String feedbackEnd = "BUILD FAILED";
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
      while (consoleText.contains("Error:")) {
        int error = consoleText.indexOf("Error:");
        int nextRow = consoleText.indexOf("\n", error);
        // Duplicate failure need to format
        String fileNameAndLine = consoleText.substring(0, error).trim();
        String fileName = extractFileName(fileNameAndLine);
        String line = extractLine(fileNameAndLine);
        String msg = consoleText.substring(error, nextRow).trim();
        String symptom = consoleText.substring(nextRow,
            consoleText.indexOf("\n", nextRow + 1)).trim();

        if (msg.contains("Duplicate")) {
          int duplicateMsgIdx = consoleText.indexOf("\n",
              consoleText.indexOf('~')) + 1;
          String duplicateMsg = "\n" + consoleText.substring(
              duplicateMsgIdx, consoleText.indexOf("\n", duplicateMsgIdx)).trim();
          fileName += "\n" + extractFileName(duplicateMsg);
          line += "\n" + extractLine(duplicateMsg);
        }

        feedbackList.add(new FeedBack(
            StatusEnum.ANDROID_LINT_FAILURE,
            fileName,
            line,
            msg,
            symptom,
            "https://developer.android.com/studio/write/lint"
        ));

        if (msg.contains("Duplicate")) {
          int duplicateMsgIdx = consoleText.indexOf("\n", consoleText.indexOf('~')) + 1;
          consoleText = consoleText.substring(
              consoleText.indexOf("\n", duplicateMsgIdx) + 1, endIndex);
        } else {
          consoleText = consoleText.substring(
              consoleText.indexOf("\n", consoleText.indexOf('~')) + 1, endIndex);
        }
        endIndex = consoleText.length();
      }
      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      feedbackList.add(
          new FeedBack(StatusEnum.ANDROID_LINT_FAILURE, "", "",
              "Coding Style ArrayList Error", "", ""));
      return feedbackList;
    }
  }

  public String extractFileName(String str) {
    return str.substring(0, str.indexOf(":")).trim();
  }

  /**
   * @param str Input format str
   * @return file line
   */
  public String extractLine(String str) {
    int colonIndex = str.indexOf(":") + 1;
    return str.substring(str.indexOf(":") + 1,
        str.indexOf(":", colonIndex)).trim();
  }
}