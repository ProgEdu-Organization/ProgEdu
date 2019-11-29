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
    try {
      consoleText = consoleText + "\n";
      int endIndex = consoleText.length();
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      while (consoleText.contains("Failed tests:")) {
        int nextrow = consoleText.indexOf("\n");
        int nextfailedtest = consoleText.indexOf("Failed tests:");
        if (nextfailedtest > nextrow) {
          consoleText = consoleText.substring(nextrow + 1, endIndex);
          endIndex = endIndex - nextrow - 1;
        } else {
          int nextcolon = consoleText.indexOf(":", 13);
          feedbacklist.add(new FeedBack(
              StatusEnum.UNIT_TEST_FAILURE,
              "",
              consoleText.substring(nextcolon + 1, nextrow).trim(),
              "",
              ""
          ));
          consoleText = consoleText.substring(nextrow + 1, endIndex);
          endIndex = endIndex - nextrow - 1;
        }
      }
      return feedbacklist;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      feedbacklist.add(
          new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "",
              "UnitTest ArrayList error", "", ""));
      return feedbacklist;
    }
  }
}