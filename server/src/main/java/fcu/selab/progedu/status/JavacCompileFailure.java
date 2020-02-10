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

public class JavacCompileFailure implements Status {

  private static final Logger LOGGER = LoggerFactory.getLogger(JavacCompileFailure.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String feedback;
      String feedbackStart = "+ javac";
      String feedbackEnd = "Build step 'Execute shell' marked build as failure";
      feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
          consoleText.indexOf(feedbackEnd));
      return feedback.trim();
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
      int nextRow = consoleText.indexOf("\n");
      int endIndex = consoleText.length();
      consoleText = consoleText.substring(nextRow, endIndex);
      endIndex = endIndex - nextRow - 1;
      while (consoleText.contains("error:")) {
        int errorIndex = consoleText.indexOf("error:");
        int nextArrow = consoleText.indexOf("^") + 1;
        nextRow = consoleText.indexOf("\n");
        if (errorIndex > nextRow) {
          consoleText = consoleText.substring(nextRow + 1, endIndex);
          endIndex = endIndex - nextRow - 1;
        } else {
          int colonIndex = consoleText.indexOf(":");
          feedbackList.add(new FeedBack(
              StatusEnum.COMPILE_FAILURE,
              consoleText.substring(0, colonIndex).trim(),
              consoleText.substring(0, errorIndex)
                  .replace(":", " ").trim(),
              consoleText.substring(nextRow, nextArrow).replace("\t", "").trim(),
              consoleText.substring(errorIndex + 6, nextRow).trim(),
              ""
          ));
          consoleText = consoleText.substring(nextRow + 1, endIndex);
          endIndex = endIndex - nextRow - 1;
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

