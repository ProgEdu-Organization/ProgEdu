package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;

import fcu.selab.progedu.data.FeedBack;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class MavenCheckstyleFailure implements Status {

  /**
   * get checkstyle information
   * 
   * @param consoleText console text
   * @return checkstyle information
   */
  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleInfo;
    String checkstyleStart = "Starting audit...";
    String checkstyleEnd = "Audit done.";
    checkstyleInfo = consoleText.substring(
        consoleText.indexOf(checkstyleStart) + checkstyleStart.length(),
        consoleText.indexOf(checkstyleEnd));

    return checkstyleInfo.trim();
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    consoleText = consoleText + "\n";
    int endIndex = consoleText.length();
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("error:") != -1) {
      int nextrow = consoleText.indexOf("\n");
      int nexterror = consoleText.indexOf("error:");
      if (nexterror > nextrow) {
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      } else {
        String errorRow = consoleText.substring(0, nextrow);
        int lastslash = errorRow.lastIndexOf("/");
        feedbacklist.add(new FeedBack(
            StatusEnum.CHECKSTYLE_FAILURE,
            errorRow.substring(lastslash + 1, nexterror - 2).trim(),
            errorRow.substring(nexterror + 6, nextrow).trim(),
            "",
            ""
        ));
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      }
    }
    return feedbacklist;
  }

  @Override
  public String toJson(ArrayList<FeedBack> arrayList) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(arrayList);
      return jsonString;
    } catch (JsonGenerationException e) {
      e.printStackTrace();
      return "JsonGenerationException Error";
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return "JsonMappingException Error";
    } catch (IOException e) {
      e.printStackTrace();
      return "IOException Error";
    }
  }

  public String console() {
    String test = "[INFO] Compiling 1 source file to /var/jenkins_home/"
        + "workspace/STUDENT2_MAVAN1119/target/classes\n"
        + "[INFO] \n"
        + "[INFO] --- maven-checkstyle-plugin:2.17:check (compile) @ HelloMaven ---\n"
        + "[INFO] Starting audit...\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab"
        + "/myapp/App.java:8:3: error: Missing a Javadoc comment.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab"
        + "/myapp/App.java:10:9: error: Local variable name 'i' must match "
        + "pattern '^[a-z][a-z0-9][a-zA-Z0-9]*$'.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/sela"
        + "b/myapp/App.java:10:10: error: WhitespaceAround: '=' is not preceded with whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab"
        + "/myapp/App.java:10:11: error: WhitespaceAround: '=' is not followed by whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/sela"
        + "b/myapp/App.java:11:7: error: WhitespaceAround: 'if' is not followed by whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/sel"
        + "ab/myapp/App.java:11:9: error: WhitespaceAround: '<' is not preceded with whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab/m"
        + "yapp/App.java:11:10: error: WhitespaceAround: '<' is not followed by whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab/m"
        + "app/App.java:11:12: error: WhitespaceAround: '{' is not preceded with whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab/my"
        + "app/App.java:12: error: 'if' child have incorrect indentation level 8,"
        + " expected level should be 6.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab/my"
        + "app/App.java:12:10: error: WhitespaceAround: '=' is not preceded with whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab/m"
        + "yapp/App.java:12:11: error: WhitespaceAround: '=' is not followed by whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab/m"
        + "yapp/App.java:12:12: error: WhitespaceAround: '+' is not preceded with whitespace.\n"
        + "/var/jenkins_home/workspace/STUDENT2_MAVAN1119/src/main/java/selab/m"
        + "yapp/App.java:12:13: error: WhitespaceAround: '+' is not followed by whitespace.\n"
        + "Audit done.\n"
        + "[INFO] ------------------------------------------------------------------------";
    return test;
  }
}
