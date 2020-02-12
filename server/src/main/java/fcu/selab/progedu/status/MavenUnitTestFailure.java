package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
      String startString = "[INFO] --- maven-surefire";
      String endString = "[INFO] BUILD FAILURE";

      unitTest = consoleText.substring(consoleText.indexOf(startString),
          consoleText.indexOf(endString) - 1);

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
      consoleText = consoleText.substring(consoleText.indexOf("Results :"), consoleText.length());
      Pattern pattern = Pattern.compile("(.*?)((\\()(.*?)(.java.)(.*?)(\\)))(.*?)(\n)");
      Matcher matcher = pattern.matcher(consoleText);
      while (matcher.find()) {
        feedbackList.add(new FeedBack(
              StatusEnum.UNIT_TEST_FAILURE, matcher.group(6), "", matcher.group(8), "",
              "https://www.learnjavaonline.org/"
          ));
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
