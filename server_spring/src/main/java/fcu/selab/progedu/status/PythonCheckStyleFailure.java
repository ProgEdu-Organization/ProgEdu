package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PythonCheckStyleFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(PythonCheckStyleFailure.class);

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
    return null;
  }
}
