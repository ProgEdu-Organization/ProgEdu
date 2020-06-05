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
      String unitTestFailureStart = "SideeX test failed with the reason below:";
      String unitTestFailureEnd = "FATAL: SideeX Test Error";

      String unitTestInfo = consoleText.substring(
          consoleText.indexOf(unitTestFailureStart) + unitTestFailureStart.length() + 1,
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
      feedbackList.add(
          new FeedBack(StatusEnum.UNIT_TEST_FAILURE,
              consoleText, ""));
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
