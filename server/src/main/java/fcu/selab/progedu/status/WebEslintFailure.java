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

public class WebEslintFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebEslintFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String checkstyleStart = "npm run eslint";
      String checkstyleEnd = "npm ERR! code ELIFECYCLE";
      int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
      int end = consoleText.lastIndexOf(checkstyleEnd) - 1;
      String checkstyleInfo = consoleText.substring(start, end);
      int nextRowIndex = checkstyleInfo.indexOf("\n");
      int endRowIndex = checkstyleInfo.indexOf("\n", checkstyleInfo.indexOf("problem"));
      checkstyleInfo = checkstyleInfo.substring(nextRowIndex + 1, endRowIndex);
      checkstyleInfo = checkstyleInfo.replace("/var/jenkins_home/workspace/", "");
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
      consoleText = consoleText.substring(0, consoleText.indexOf("✖"));
      int endIndex = consoleText.length();
      String fileName = "";
      while (consoleText.contains("error")) {
        int errorIndex = consoleText.indexOf("error");
        int nextRowIndex = consoleText.indexOf("\n");
        if (nextRowIndex == -1) {
          break;
        }
        if (errorIndex > nextRowIndex) {
          if (consoleText.substring(0, nextRowIndex).contains("/")) {
            fileName = consoleText.substring(0, nextRowIndex).trim();
          }
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        } else {
          String errorString = consoleText.substring(errorIndex + 6, nextRowIndex);
          // Sometimes, eslint did not provide suggest, we need to ignore it.
          int errorStyleStart = errorString.indexOf("  ");
          String message = "";
          String symptom = "";
          if (errorStyleStart != -1) {
            message = errorString.substring(0, errorStyleStart).trim();
            symptom = errorString.substring(errorStyleStart, errorString.length()).trim();
          } else {
            message = errorString.substring(0, errorString.length()).trim();
          }
          feedbackList.add(
              new FeedBack(
                  StatusEnum.WEB_ESLINT_FAILURE,
                  fileName,
                  consoleText.substring(0, errorIndex).trim(), message, symptom,
                  "https://github.com/airbnb/javascript\n"));
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        }
      }
      if (feedbackList.isEmpty()) {
        feedbackList.add(
            new FeedBack(StatusEnum.WEB_ESLINT_FAILURE,
                "Please notify teacher or assistant this situation, thank you!", ""));
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.WEB_ESLINT_FAILURE,
              "Eslint ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}
