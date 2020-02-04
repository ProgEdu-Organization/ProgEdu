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

public class MavenCheckstyleFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenCheckstyleFailure.class);

  /**
   * get checkstyle information
   * 
   * @param consoleText console text
   * @return checkstyle information
   */
  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String checkstyleInfo;
      String checkstyleStart = "Starting audit...";
      String checkstyleEnd = "Audit done.";
      checkstyleInfo = consoleText.substring(
          consoleText.indexOf(checkstyleStart) + checkstyleStart.length(),
          consoleText.indexOf(checkstyleEnd));

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
      consoleText = consoleText + "\n";
      int endIndex = consoleText.length();
      while (consoleText.contains("error:")) {
        int nextRowIndex = consoleText.indexOf("\n");
        int nextErrorIndex = consoleText.indexOf("error:");
        if (nextErrorIndex > nextRowIndex) {
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        } else {
          String errorRow = consoleText.substring(0, nextRowIndex);
          int lastSlash = errorRow.lastIndexOf("/");
          String errorFileName = errorRow.substring(lastSlash + 1, nextErrorIndex - 2).trim();
          feedbackList.add(new FeedBack(
              StatusEnum.CHECKSTYLE_FAILURE,
              errorFileName.substring(0, errorFileName.indexOf(":")).trim(),
              errorFileName.substring(
                  errorFileName.indexOf(":") + 1, errorFileName.length()),
              errorRow.substring(nextErrorIndex + 6, nextRowIndex).trim(),
              "",
              ""
          ));
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        }
      }
      if (feedbackList.isEmpty()) {
        feedbackList.add(
            new FeedBack(StatusEnum.CHECKSTYLE_FAILURE,
                "Please notify teacher or assistant this situation, thank you!", ""));
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.CHECKSTYLE_FAILURE,
              "Checkstyle ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}

