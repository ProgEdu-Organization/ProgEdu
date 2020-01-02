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
    String checkstyleStart = "npm run htmlhint";
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
    ArrayList<FeedBack> feedbackList = new ArrayList<>();
    try {
      consoleText = consoleText.substring(0, consoleText.indexOf("Scanned"));
      int endIndex = consoleText.length();
      String fileName = "";
      while (consoleText.contains("L")) {
        int lineIndex = consoleText.indexOf("L");
        int nextRowIndex = consoleText.indexOf("\n");
        if (nextRowIndex == -1) {
          break;
        }
        if (lineIndex > nextRowIndex) {
          if (consoleText.substring(0, nextRowIndex).contains("/")) {
            fileName = consoleText.substring(0, nextRowIndex)
                .replace("/var/jenkins_home/workspace/","").trim();
          }
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        } else {
          int separatedIndex = consoleText.indexOf("|");
          int arrowIndex = consoleText.indexOf("^");
          int nextLineIndex = consoleText.indexOf("\n", arrowIndex);
          int dotIndex = consoleText.indexOf(".", arrowIndex);
          String errorStyle = consoleText.substring(dotIndex + 1, nextLineIndex)
              .replace("(", "").replace(")", "").trim();
          feedbackList.add(
              new FeedBack(
              StatusEnum.WEB_HTMLHINT_FAILURE,
              fileName,
              consoleText.substring(lineIndex, separatedIndex - 1).trim(),
              consoleText.substring(arrowIndex + 2, dotIndex).trim(),
              errorStyle,
              "https://codertw.com/%E5%89%8D%E7%AB%AF%E9%96%8B%E7%99%BC/15355/\n"));
          consoleText = consoleText.substring(nextLineIndex + 1, endIndex);
          endIndex = endIndex - nextLineIndex - 1;
        }
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.WEB_HTMLHINT_FAILURE,
              "HtmlHint ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}

