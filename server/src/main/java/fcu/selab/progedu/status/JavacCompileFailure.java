package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JavacCompileFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "+ javac";
    String feedbackEnd = "Build step 'Execute shell' marked build as failure";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
        consoleText.indexOf(feedbackEnd));
    return feedback.trim();

  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try {
      int nextRow = consoleText.indexOf("\n");
      int endIndex = consoleText.length();
      consoleText = consoleText.substring(nextRow, endIndex);
      endIndex = endIndex - nextRow - 1;
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      while (consoleText.contains("error:")) {
        int errorIndex = consoleText.indexOf("error:");
        nextRow = consoleText.indexOf("\n");
        if (errorIndex > nextRow) {
          consoleText = consoleText.substring(nextRow + 1, endIndex);
          endIndex = endIndex - nextRow - 1;
        } else {
          int colonIndex = consoleText.indexOf(":");
          feedbacklist.add(new FeedBack(
              StatusEnum.COMPILE_FAILURE,
              consoleText.substring(0, colonIndex).trim(),
              consoleText.substring(0, errorIndex)
                  .replace(":", " ").trim(),
              consoleText.substring(errorIndex + 6, nextRow).trim(),
              "",
              ""
          ));
          consoleText = consoleText.substring(nextRow + 1, endIndex);
          endIndex = endIndex - nextRow - 1;
        }
      }
      return feedbacklist;
    } catch (Exception e) {
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      feedbacklist.add(
          new FeedBack(StatusEnum.COMPILE_FAILURE, "", "",
              "CompileFailure ArrayList error", "", ""));
      return feedbacklist;
    }
  }
}

