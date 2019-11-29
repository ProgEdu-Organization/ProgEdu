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
    try {
      consoleText = consoleText.substring(0, consoleText.indexOf("✖"));
      int endIndex = consoleText.length();
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      while (consoleText.contains("error")) {
        int errorIndex = consoleText.indexOf("error");
        int nextrowIndex = consoleText.indexOf("\n");
        if (errorIndex > nextrowIndex) {
          consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
          endIndex = endIndex - nextrowIndex - 1;
        } else {
          String errorString = consoleText.substring(errorIndex + 6, nextrowIndex).trim();
          int errorStyleStart = errorString.indexOf("  ");
          feedbacklist.add(
              new FeedBack(
                  StatusEnum.WEB_ESLINT_FAILURE,
                  consoleText.substring(0, errorIndex).trim(),
                  errorString.substring(0, errorStyleStart).trim(),
                  errorString.substring(errorStyleStart, errorString.length()).trim(),
                  "https://github.com/airbnb/javascript\n"));
          consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
          endIndex = endIndex - nextrowIndex - 1;
        }
      }
      return feedbacklist;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      feedbacklist.add(
          new FeedBack(StatusEnum.WEB_ESLINT_FAILURE, "",
              "Eslint ArrayList error", "", ""));
      return feedbacklist;
    }
  }
}
