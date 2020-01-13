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

public class WebUnitTestFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebUnitTestFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String unitTestFailureStart = "npm run test";
      String unitTestFailureEnd = "npm ERR! code ELIFECYCLE";

      String unitTestInfo = consoleText.substring(
          consoleText.indexOf(unitTestFailureStart) + unitTestFailureStart.length(),
          consoleText.indexOf(unitTestFailureEnd));

      return unitTestInfo.trim();
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
      int consoleEnd = consoleText.indexOf("passing");
      String unitTestInfo = consoleText.substring(0, consoleEnd);
      int nextRow = unitTestInfo.indexOf("\n");
      unitTestInfo = unitTestInfo.substring(nextRow + 1, unitTestInfo.length()).trim();
      int endIndex = unitTestInfo.length();
      unitTestInfo = unitTestInfo.substring(nextRow + 1, endIndex);
      endIndex = unitTestInfo.length();
      while (unitTestInfo.contains(")")) {
        int nextParentheses = unitTestInfo.indexOf(")");
        int nextRowIndex = unitTestInfo.indexOf("\n", nextParentheses);
        if (nextRowIndex - nextParentheses == 1) { //
          unitTestInfo = unitTestInfo.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        } else {
          int nextSpace = unitTestInfo.indexOf("\n", nextParentheses + 1);
          feedbackList.add(new FeedBack(
              StatusEnum.UNIT_TEST_FAILURE,
              "",
              "",
              unitTestInfo.substring(nextParentheses + 2, nextSpace),
              "",
              ""));
          unitTestInfo = unitTestInfo.substring(nextSpace + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        }
      }
      if (feedbackList.isEmpty()) {
        feedbackList.add(
            new FeedBack(StatusEnum.UNIT_TEST_FAILURE,
                "Please notify teacher or assistant this situation, thank you!", ""));
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.UNIT_TEST_FAILURE,
              "UnitTest ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}
