package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebHtmlhintFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebHtmlhintFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String checkstyleStart = "npm run htmlhint";
      String checkstyleEnd = "npm ERR! code ELIFECYCLE";
      int start = consoleText.indexOf(checkstyleStart);
      int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

      String checkstyleInfo = consoleText.substring(start, end);
      checkstyleInfo = checkstyleInfo.replaceAll("[\u001B][\\[][\\d]{0,3}[m]", "");
      checkstyleInfo = checkstyleInfo.replaceAll("\\^", " ^");
      checkstyleInfo = checkstyleInfo.replaceAll(checkstyleStart, "");

      return checkstyleInfo.trim();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "ExtractFailureMsg Method Error";
    }
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
          String errorMessage = consoleText.substring(arrowIndex, nextLineIndex);
          int lastParentheses = errorMessage.lastIndexOf("(");
          feedbackList.add(
              new FeedBack(
              StatusEnum.WEB_HTMLHINT_FAILURE,
              fileName,
              consoleText.substring(lineIndex, separatedIndex - 1).trim(),
              errorMessage.substring(errorMessage.indexOf("^") + 1, lastParentheses - 1).trim(),
              errorMessage.substring(lastParentheses, errorMessage.length())
                  .replace("(","").replace(")","").trim(),
              "https://codertw.com/%E5%89%8D%E7%AB%AF%E9%96%8B%E7%99%BC/15355/\n"));
          consoleText = consoleText.substring(nextLineIndex + 1, endIndex);
          endIndex = endIndex - nextLineIndex - 1;
        }
      }
      if (feedbackList.isEmpty()) {
        feedbackList.add(
            new FeedBack(StatusEnum.WEB_HTMLHINT_FAILURE,
                "Please notify teacher or assistant this situation, thank you!", ""));
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.WEB_HTMLHINT_FAILURE,
              "HtmlHint ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}

