package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    consoleText = consoleText.substring(
        consoleText.indexOf("\n"), consoleText.indexOf("Scanned"));
    int endIndex = consoleText.length();
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("L") != -1) {
      int lineIndex = consoleText.indexOf("L");
      int sparateIndex = consoleText.indexOf("|");
      int arrowIndex = consoleText.indexOf("^");
      int nextlineIndex = consoleText.indexOf("\n", arrowIndex);
      int dotIndex = consoleText.indexOf(".", arrowIndex);
      String errorStyle = consoleText.substring(dotIndex + 1, nextlineIndex)
          .replace("(", "").replace(")", "").trim();
      feedbacklist.add(
          new FeedBack(
              StatusEnum.WEB_HTMLHINT_FAILURE,
              consoleText.substring(lineIndex, sparateIndex - 1).trim(),
              consoleText.substring(arrowIndex + 2, dotIndex).trim(),
              errorStyle,
              "https://codertw.com/%E5%89%8D%E7%AB%AF%E9%96%8B%E7%99%BC/15355/\n"));
      consoleText = consoleText.substring(nextlineIndex + 1, endIndex);
      endIndex = endIndex - nextlineIndex - 1;
    }
    return feedbacklist;
  }
}

