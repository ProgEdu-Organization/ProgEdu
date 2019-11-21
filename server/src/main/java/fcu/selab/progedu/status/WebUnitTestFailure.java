package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class WebUnitTestFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String unitTestFailureStart = "npm run test";
    String unitTestFailureEnd = "npm ERR! code ELIFECYCLE";

    String unitTestInfo = consoleText.substring(
        consoleText.indexOf(unitTestFailureStart) + unitTestFailureStart.length(),
        consoleText.indexOf(unitTestFailureEnd));

    return unitTestInfo.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    int consoleStart = consoleText.indexOf("測試");
    int consoleEnd = consoleText.indexOf("failing");
    String unitTestInfo = consoleText.substring(consoleStart, consoleEnd);
    int nextRow = unitTestInfo.indexOf("\n");
    int endIndex = consoleEnd - consoleStart;
    unitTestInfo = unitTestInfo.substring(nextRow + 1, endIndex);
    endIndex = endIndex - nextRow - 1;
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (unitTestInfo.indexOf(")") != -1) {
      int nextparentheses = unitTestInfo.indexOf(")");
      int nextrow = unitTestInfo.indexOf("\n", nextparentheses);
      if (nextrow - nextparentheses == 1) { //
        unitTestInfo = unitTestInfo.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      } else {
        int netspace = unitTestInfo.indexOf("\n", nextparentheses + 1);
        feedbacklist.add(new FeedBack(
            StatusEnum.UNIT_TEST_FAILURE,
            "",
            unitTestInfo.substring(nextparentheses + 2, netspace),
            "",
            ""));
        unitTestInfo = unitTestInfo.substring(netspace + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      }
    }
    return feedbacklist;
  }
}
