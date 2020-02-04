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

public class WebStylelintFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebStylelintFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String checkstyleStart = "npm run stylelint";
      String checkstyleEnd = "npm ERR! code ELIFECYCLE";
      int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
      int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

      String checkstyleInfo = consoleText.substring(start, end);
      int nextRowIndex = checkstyleInfo.indexOf("\n");
      checkstyleInfo = checkstyleInfo.substring(nextRowIndex + 1, end - start);

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
      int endIndex = consoleText.length();
      String fileName = "";
      while (consoleText.contains("✖")) {
        int crossIndex = consoleText.indexOf("✖");
        int nextRowIndex = consoleText.indexOf("\n");
        if (nextRowIndex == -1) {
          break;
        }
        if (crossIndex > nextRowIndex) {
          if (consoleText.substring(0, nextRowIndex).contains("/")) {
            fileName = consoleText.substring(0, nextRowIndex).trim();
          }
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        } else {
          String errorString = consoleText.substring(crossIndex + 1, nextRowIndex).trim();
          int errorStyleStart = errorString.indexOf("  ");
          feedbackList.add(
              new FeedBack(
                  StatusEnum.WEB_STYLELINT_FAILURE,
                  fileName,
                  consoleText.substring(0, crossIndex - 1).trim(),
                  errorString.substring(0, errorStyleStart + 1).trim(),
                  errorString.substring(errorStyleStart, errorString.length()).trim(),
                  "https://codertw.com/%E5%89%8D%E7%AB%AF%E9%96%8B%E7%99%BC/183114/\n"));
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        }
      }
      if (feedbackList.isEmpty()) {
        feedbackList.add(
            new FeedBack(StatusEnum.WEB_STYLELINT_FAILURE,
                "Please notify teacher or assistant this situation, thank you!", ""));
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.WEB_STYLELINT_FAILURE,
              "Stylelint ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }

}

