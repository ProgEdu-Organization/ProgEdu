package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.utils.ExceptionUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonUnitTestFailure implements Status  {
  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String unitTestInfo;
      String unitTestStart = "=================================== FAILURES ===================================\n";
      String unitTestEnd = "[Pipeline] }\n";
      unitTestInfo = consoleText.substring(consoleText.indexOf(unitTestStart));
      unitTestInfo = unitTestInfo.substring(0, unitTestInfo.indexOf(unitTestEnd));


      return unitTestInfo;
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
      Pattern pattern = Pattern.compile("(.*?)(_)(.*?)(.py)(::)(.*?)(\n)");
      Matcher matcher = pattern.matcher(consoleText);
      while (matcher.find()) {
        String fileName = matcher.group(3) + matcher.group(4);
        String message = matcher.group(6);

        feedbackList.add(new FeedBack(
                StatusEnum.UNIT_TEST_FAILURE, fileName, "", message, "", ""));
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
