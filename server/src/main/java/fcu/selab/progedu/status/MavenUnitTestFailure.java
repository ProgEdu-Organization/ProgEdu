package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class MavenUnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String unitTest = "";
    String startStr = "Failed tests:";
    String goal = "Tests run:";
    int goalStr = consoleText.indexOf(goal, consoleText.indexOf(goal) + 1);

    unitTest = consoleText.substring(consoleText.indexOf(startStr), goalStr - 1);
    //<, > will be HTML tag, change to the " 
    unitTest = unitTest.replaceAll("<", "\"").replaceAll(">", "\"");
    
    return unitTest.trim();
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
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.UNIT_TEST_FAILURE,
              "UnitTest ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}
