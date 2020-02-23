package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
      Pattern pattern = Pattern.compile("[\\d]{1,3}( error)");
      Matcher matcher = pattern.matcher(consoleText);
      if (matcher.find()) {
        feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            matcher.start());
      } else {
        feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd));
      }
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
      Pattern patternJavac = Pattern.compile("(\\+ javac )(.*?)(.java)");
      Matcher matcherJavac = patternJavac.matcher(consoleText);
      String suggest = "https://www.learnjavaonline.org/";
      int nextMatcherJavac;
      while (matcherJavac.find()) {
        String subString;
        String fileName = matcherJavac.group(2) + ".java";
        nextMatcherJavac = matcherJavac.start() + 1;
        int endJavacIndex = consoleText.indexOf(matcherJavac.group(1), nextMatcherJavac) - 1;
        if (endJavacIndex != -2) {
          subString = consoleText.substring(matcherJavac.start(), endJavacIndex);
        } else {
          subString = consoleText.substring(matcherJavac.start(), consoleText.length());
        }
        Pattern patternError = Pattern.compile("(.*?)((.java:)[\\d]{1,4})(: error:)");
        Matcher matcherError = patternError.matcher(subString);
        int nextMatcherError;
        while (matcherError.find()) {
          int errorEnd;
          int matchRow = subString.indexOf("\n", matcherError.start());
          nextMatcherError = subString.indexOf("error:", matchRow);
          if (nextMatcherError != -1) {
            errorEnd = matcherError.start()
                + subString.substring(matcherError.start(), nextMatcherError).lastIndexOf("\n");
          } else {
            errorEnd = subString.length();
          }
          String line = matcherError.group(2).substring(matcherError.group(2).indexOf(":") + 1);
          String symptom = subString.substring(subString.indexOf("error:", matcherError.start())
              + 6, matchRow);
          String message = subString.substring(matchRow + 1, errorEnd);
          feedbackList.add(new FeedBack(
              StatusEnum.COMPILE_FAILURE, fileName, line, message, symptom, suggest
          ));
        }
        nextMatcherJavac = endJavacIndex + 2;
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

