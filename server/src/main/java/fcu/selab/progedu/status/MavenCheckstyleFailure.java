package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class MavenCheckstyleFailure implements Status {

  /**
   * get checkstyle information
   * 
   * @param consoleText console text
   * @return checkstyle information
   */
  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo;
    String checkstyleStart = "Starting audit...";
    String checkstyleEnd = "Audit done.";
    checkstyleInfo = consoleText.substring(
        consoleText.indexOf(checkstyleStart) + checkstyleStart.length(),
        consoleText.indexOf(checkstyleEnd));

    return checkstyleInfo.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      consoleText = consoleText + "\n";
      int endIndex = consoleText.length();
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      while (consoleText.contains("error:")) {
        int nextrow = consoleText.indexOf("\n");
        int nexterror = consoleText.indexOf("error:");
        if (nexterror > nextrow) {
          consoleText = consoleText.substring(nextrow + 1, endIndex);
          endIndex = endIndex - nextrow - 1;
        } else {
          String errorRow = consoleText.substring(0, nextrow);
          int lastslash = errorRow.lastIndexOf("/");
          String errorfileName = errorRow.substring(lastslash + 1, nexterror - 2).trim();
          feedbacklist.add(new FeedBack(
              StatusEnum.CHECKSTYLE_FAILURE,
              errorfileName.substring(0, errorfileName.indexOf(":")).trim(),
              errorfileName.substring(
                  errorfileName.indexOf(":") + 1, errorfileName.length()),
              errorRow.substring(nexterror + 6, nextrow).trim(),
              "",
              ""
          ));
          consoleText = consoleText.substring(nextrow + 1, endIndex);
          endIndex = endIndex - nextrow - 1;
        }
      }
      return feedbacklist;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      feedbacklist.add(
          new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "", "",
              "Checkstyle ArrayList error", "", ""));
      return feedbacklist;
    }
  }
}

