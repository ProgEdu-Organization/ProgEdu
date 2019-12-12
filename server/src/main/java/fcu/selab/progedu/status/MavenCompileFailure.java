package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class MavenCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "[ERROR] COMPILATION ERROR : ";
    String feedbackEnd = "[INFO] BUILD FAILURE";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart) + feedbackStart.length(),
        consoleText.indexOf(feedbackEnd));
    return feedback.replace("/var/jenkins_home/workspace/", "").trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    int endIndex = consoleText.length();
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("[ERROR]") != -1) {
      int nextrow = consoleText.indexOf("\n");
      int nexterror = consoleText.indexOf("[ERROR]");
      if (nexterror > nextrow) {
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      } else {
        int nextbrackets = consoleText.indexOf("]", 7);
        int lastslash = consoleText.lastIndexOf("/");
        feedbacklist.add(new FeedBack(
            StatusEnum.COMPILE_FAILURE,
            consoleText.substring(lastslash + 1, nextbrackets + 1).trim(),
            consoleText.substring(nextbrackets + 1, nextrow).trim(),
            "",
            ""
        ));
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      }
    }
    return feedbacklist;
  }
}
