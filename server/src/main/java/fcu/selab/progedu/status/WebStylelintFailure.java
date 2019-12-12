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
    String checkstyleStart = "> stylelint";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,end - start);

    return checkstyleInfo;
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    int endIndex = consoleText.length();
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("✖") != -1) {
      int crossIndex = consoleText.indexOf("✖");
      int nextrowIndex = consoleText.indexOf("\n");
      if (crossIndex > nextrowIndex) {
        consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
        endIndex = endIndex - nextrowIndex - 1;
      } else {
        int errorStyleStart = consoleText.indexOf("  ", crossIndex + 2);
        feedbacklist.add(
            new FeedBack(
                StatusEnum.WEB_STYLELINT_FAILURE,
                consoleText.substring(0, crossIndex - 1).trim(),
                consoleText.substring(crossIndex + 1, errorStyleStart + 1).trim(),
                consoleText.substring(errorStyleStart, nextrowIndex).trim(),
                "https://codertw.com/%E5%89%8D%E7%AB%AF%E9%96%8B%E7%99%BC/183114/\n"));
        consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
        endIndex = endIndex - nextrowIndex - 1;
      }
    }
    return feedbacklist;
  }

}

