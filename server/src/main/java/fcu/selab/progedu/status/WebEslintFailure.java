package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class WebEslintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "+ npm run eslint";
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
    try {
      consoleText = consoleText.substring(0, consoleText.indexOf("✖"));
      int endIndex = consoleText.length();
      String fileName = "";
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
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
          String errorString = consoleText.substring(errorIndex + 6, nextRowIndex).trim();
          int errorStyleStart = errorString.indexOf("  ");
          feedbackList.add(
              new FeedBack(
                  StatusEnum.WEB_ESLINT_FAILURE,
                  fileName,
                  consoleText.substring(0, errorIndex).trim(),
                  errorString.substring(0, errorStyleStart).trim(),
                  errorString.substring(errorStyleStart, errorString.length()).trim(),
                  "https://github.com/airbnb/javascript\n"));
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        }
      }
      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
      feedbackList.add(
          new FeedBack(StatusEnum.WEB_ESLINT_FAILURE, "", "",
              "Eslint ArrayList error", "", ""));
      return feedbackList;
    }
  }
}
