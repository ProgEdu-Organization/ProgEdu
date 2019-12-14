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
    try {
      int endIndex = consoleText.length();
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      while (consoleText.contains("[ERROR]")) {
        int nextrow = consoleText.indexOf("\n");
        int nexterror = consoleText.indexOf("[ERROR]");
        if (nexterror > nextrow) {
          consoleText = consoleText.substring(nextrow + 1, endIndex);
          endIndex = endIndex - nextrow - 1;
        } else {
          int nextbrackets = consoleText.indexOf("]", 7);
          int lastslash = consoleText.lastIndexOf("/");
          String errorfileName = consoleText.substring(lastslash + 1, nextbrackets + 1).trim();
          feedbacklist.add(new FeedBack(
              StatusEnum.COMPILE_FAILURE,
              errorfileName.substring(0, errorfileName.indexOf(":")).trim(),
              errorfileName
                  .substring(errorfileName.indexOf(":") + 1, errorfileName.length())
                  .replace("[", "").replace("]", "")
                  .replace(",", ":"),
              consoleText.substring(nextbrackets + 1, nextrow).trim(),
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
          new FeedBack(StatusEnum.COMPILE_FAILURE, "", "",
              "CompileFailure ArrayList error", "", ""));
      return feedbacklist;
    }
  }
}
