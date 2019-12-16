package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class WebStylelintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "+ npm run stylelint";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,end - start);

    return checkstyleInfo.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      int endIndex = consoleText.length();
      String fileName = "";
      ArrayList<FeedBack> feedbackList = new ArrayList<>();
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
      return feedbackList;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      feedbacklist.add(
          new FeedBack(StatusEnum.WEB_STYLELINT_FAILURE, "", "",
              "Stylelint ArrayList error", "", ""));
      return feedbacklist;
    }
  }

}

