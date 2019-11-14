package fcu.selab.progedu.status;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fcu.selab.progedu.data.FeedBack;

public class WebHtmlhintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "   /var/jenkins_home/workspace/";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart);
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    checkstyleInfo = checkstyleInfo.replaceAll("[\u001B][\\[][\\d]{0,3}[m]", "");
    checkstyleInfo = checkstyleInfo.replaceAll("\\^", " ^");
    checkstyleInfo = checkstyleInfo.replaceAll(checkstyleStart, "");

    return checkstyleInfo.trim();
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    consoleText = consoleText.substring(
        consoleText.indexOf("\n"), consoleText.indexOf("Scanned"));
    int endIndex = consoleText.length();
    List<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("L") != -1) {
      int lineIndex = consoleText.indexOf("L");
      int sparateIndex = consoleText.indexOf("|");
      int arrowIndex = consoleText.indexOf("^");
      int nextlineIndex = consoleText.indexOf("\n", arrowIndex);
      int dotIndex = consoleText.indexOf(".", arrowIndex);
      String errorStyle = consoleText.substring(dotIndex + 1, nextlineIndex)
          .replace("(", "").replace(")", "").trim();
      feedbacklist.add(new FeedBack(
          "Htmlhint",
          consoleText.substring(lineIndex, sparateIndex - 1).trim(),
          consoleText.substring(arrowIndex + 2,dotIndex).trim(),
          errorStyle,
          ""
      ));
      consoleText = consoleText.substring(nextlineIndex + 1, endIndex);
      endIndex = endIndex - nextlineIndex - 1;
    }
    Gson gson = new Gson();
    return gson.toJson(feedbacklist).toString();
  }
}
