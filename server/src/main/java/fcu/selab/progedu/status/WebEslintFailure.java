package fcu.selab.progedu.status;

import java.util.ArrayList;

import com.google.gson.Gson;

import fcu.selab.progedu.data.FeedBack;

public class WebEslintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "Warning:";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    int endrow = checkstyleInfo.indexOf("\n", checkstyleInfo.indexOf("problems"));
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,endrow);
    checkstyleInfo = checkstyleInfo.replace("/var/jenkins_home/workspace/","");
    return checkstyleInfo.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    consoleText = consoleText.substring(0, consoleText.indexOf("✖"));
    int endIndex = consoleText.length();
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("error") != -1) {
      int errorIndex = consoleText.indexOf("error");
      int nextrowIndex = consoleText.indexOf("\n");
      if (errorIndex > nextrowIndex) {
        consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
        endIndex = endIndex - nextrowIndex - 1;
      } else {
        int errorStyleStart = consoleText.indexOf("  ", errorIndex + 6);
        feedbacklist.add(new FeedBack(
            StatusEnum.WEB_ESLINT_FAILURE,
            consoleText.substring(0, errorIndex).trim(),
            consoleText.substring(errorIndex + 5, errorStyleStart).trim(),
            consoleText.substring(errorStyleStart, nextrowIndex).trim(),
            ""
        ));
        consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
        endIndex = endIndex - nextrowIndex - 1;
      }
    }
    return feedbacklist;
  }

  @Override
  public String toJson(ArrayList<FeedBack> arrayList) {
    Gson gson = new Gson();
    return gson.toJson(arrayList).toString();
  }
}
