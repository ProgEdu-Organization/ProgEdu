package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidCompileFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "> Task :app:compileDebugJavaWithJavac";
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
    System.out.println(consoleText);
    try {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      Pattern pattern = Pattern.compile("[0-9]+ error");
      Matcher matcher = pattern.matcher(consoleText);
      String endStr = "";
      if (matcher.find()) {
        endStr = matcher.group(0);
      }
      int endIndex = consoleText.indexOf(endStr);

      while (consoleText.contains(".java")) {
        int error = consoleText.indexOf(": error:");
        int nextRow = consoleText.indexOf("\n", error);
        int caret = consoleText.indexOf("^", nextRow);
        if (nextRow == -1) {
          break;
        }
        int fileStart = consoleText.indexOf("/");
        String fileNameAndLine = consoleText.substring(fileStart, error).trim();
        feedbackList.add(new FeedBack(
            StatusEnum.COMPILE_FAILURE,
            fileNameAndLine.substring(0, fileNameAndLine.indexOf(":")).trim(),
            fileNameAndLine.substring(fileNameAndLine.indexOf(":") + 1,
                fileNameAndLine.length()).trim(),
            consoleText.substring(error + ": error:".length(), nextRow).trim(),
            consoleText.substring(nextRow + 1, caret),
            "https://developer.android.com/studio/build"
        ));
        consoleText = consoleText.substring(caret + 1, endIndex);
        endIndex = consoleText.length();
      }

      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      feedbackList.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "", "",
          "Compile ArrayList error", "", ""));
      return feedbackList;
    }
  }
}
