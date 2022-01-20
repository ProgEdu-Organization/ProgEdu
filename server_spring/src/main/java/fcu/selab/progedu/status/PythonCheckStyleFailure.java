package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonCheckStyleFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(PythonCheckStyleFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String checkstyleInfo;
      String checkstyleStart = "flake8 --filename=*.py";
      String checkstyleEnd = "[Pipeline] }\n";
      checkstyleInfo = consoleText.substring(consoleText.indexOf(checkstyleStart));
      checkstyleInfo = checkstyleInfo.substring(0, checkstyleInfo.indexOf(checkstyleEnd));
      checkstyleInfo = checkstyleInfo.replace("flake8 --filename=*.py\n", "");

      return checkstyleInfo;
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "ExtractFailureMsg Method Error";
    }
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    ArrayList<FeedBack> feedbackList = new ArrayList<>();

    String suggest = "https://www.python.org/dev/peps/pep-0008/";
    try {
      Pattern pattern = Pattern.compile("(.*?)(.py)(:)(\\d{1,4}:\\d{1,4})(:)(.*?)(\n)");
      Matcher matcher = pattern.matcher(consoleText);
      while (matcher.find()) {
        String fileName = matcher.group(1) + matcher.group(2);
        String line = matcher.group(4);
        String message = matcher.group(6);

        feedbackList.add(new FeedBack(
                StatusEnum.CHECKSTYLE_FAILURE, fileName, line, message, "", suggest));
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
