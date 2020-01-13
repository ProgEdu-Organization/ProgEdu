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

public class MavenUnitTestFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenUnitTestFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String unitTest = "";
      String startStr = "Failed tests:";
      String goal = "Tests run:";
      int goalStr = consoleText.indexOf(goal, consoleText.indexOf(goal) + 1);

      unitTest = consoleText.substring(consoleText.indexOf(startStr), goalStr - 1);
      //<, > will be HTML tag, change to the "
      unitTest = unitTest.replaceAll("<", "\"").replaceAll(">", "\"");

      return unitTest.trim();
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
      while (consoleText.contains("Failed tests:")) {
        int nextRowIndex = consoleText.indexOf("\n");
        int nextFailedTest = consoleText.indexOf("Failed tests:");
        if (nextFailedTest > nextRowIndex) {
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        } else {
          int nextColonIndex = consoleText.indexOf(":", 13);
          feedbackList.add(new FeedBack(
              StatusEnum.UNIT_TEST_FAILURE,
              "",
              "",
              consoleText.substring(nextColonIndex + 1, nextRowIndex).trim(),
              "",
              ""
          ));
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
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
