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
    ArrayList<FeedBack> feedbackList = new ArrayList<>();
    try {
      int endIndex = consoleText.length();
      while (consoleText.contains("[ERROR]")) {
        int nextRowIndex = consoleText.indexOf("\n");
        int nextErrorIndex = consoleText.indexOf("[ERROR]");
        if (nextErrorIndex > nextRowIndex) {
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        } else {
          int nextBracketsIndex = consoleText.indexOf("]", 7);
          int lastSlashIndex = consoleText.lastIndexOf("/");
          String errorFileName = consoleText
              .substring(lastSlashIndex + 1, nextBracketsIndex + 1).trim();
          feedbackList.add(new FeedBack(
              StatusEnum.COMPILE_FAILURE,
              errorFileName.substring(0, errorFileName.indexOf(":")).trim(),
              errorFileName
                  .substring(errorFileName.indexOf(":") + 1, errorFileName.length())
                  .replace("[", "").replace("]", "")
                  .replace(",", ":"),
              consoleText.substring(nextBracketsIndex + 1, nextRowIndex).trim(),
              "",
              ""
          ));
          consoleText = consoleText.substring(nextRowIndex + 1, endIndex);
          endIndex = endIndex - nextRowIndex - 1;
        }
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.COMPILE_FAILURE,
              "CompileFailure ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}
