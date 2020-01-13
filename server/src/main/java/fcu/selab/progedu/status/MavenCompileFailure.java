package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenCompileFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenCompileFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String feedback;
      String feedbackStart = "[ERROR] COMPILATION ERROR : ";
      String feedbackEnd = "[INFO] BUILD FAILURE";
      feedback = consoleText.substring(consoleText.indexOf(feedbackStart) + feedbackStart.length(),
          consoleText.indexOf(feedbackEnd));
      return feedback.replace("/var/jenkins_home/workspace/", "").trim();
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
      if (feedbackList.isEmpty()) {
        feedbackList.add(
            new FeedBack(StatusEnum.COMPILE_FAILURE,
                "Please notify teacher or assistant this situation, thank you!", ""));
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.COMPILE_FAILURE,
              "CompileFailure ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}
