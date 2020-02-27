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
      String startString = "Failed tests:";
      String endString = "Tests run:";

      unitTest = consoleText.substring(consoleText.indexOf(startString),
          consoleText.indexOf(endString, consoleText.indexOf(startString)));

      return unitTest;
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "ExtractFailureMsg Method Error";
    }
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    String suggest = "https://www.learnjavaonline.org/";
    ArrayList<FeedBack> feedbackList = new ArrayList<>();
    try {
      Pattern pattern = Pattern.compile("(.*?)((\\()(.*?)(\\)))(.*?)(\n)");
      Matcher matcher = pattern.matcher(consoleText);
      while (matcher.find()) {
        String message;
        String fileName = matcher.group(4).replaceAll("\\.", "/");
        if (matcher.group(6).contains(":")) {
          message = matcher.group(6).substring(matcher.group(6).indexOf(":") + 1);
        } else {
          message = matcher.group(6);
        }
        feedbackList.add(new FeedBack(
              StatusEnum.UNIT_TEST_FAILURE, fileName, "", message.trim(), "", suggest
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
