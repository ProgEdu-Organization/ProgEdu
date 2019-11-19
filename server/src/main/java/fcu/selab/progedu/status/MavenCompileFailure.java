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
    String test = "[INFO] Scanning for projects...\n"
        + "[INFO]                                                           \n"
        + "[INFO] ----------------------------------------------------------\n"
        + "[INFO] Building HelloMaven 1.0-SNAPSHOT\n"
        + "[INFO] ---------------------------------------------------------\n"
        + "[INFO] \n"
        + "[INFO] --- maven-resources-plugin:2.6:resources (default-"
        + "resources) @ HelloMaven ---\n"
        + "[WARNING] Using platform encoding (UTF-8 actually) to copy"
        + " filtered resources, i.e. build is platform dependent!\n"
        + "[INFO] skip non existing resourceDirectory /var/jenkins_ho"
        + "me/workspace/STUDENT3_MAVAN1119/src/main/resources\n"
        + "[INFO] \n"
        + "[INFO] --- maven-compiler-plugin:3.6.1:compile (default-co"
        + "mpile) @ HelloMaven ---\n"
        + "[INFO] Changes detected - recompiling the module!\n"
        + "[WARNING] File encoding has not been set, using platform e"
        + "ncoding UTF-8, i.e. build is platform dependent!\n"
        + "[INFO] Compiling 1 source file to /var/jenkins_home/workspa"
        + "ce/STUDENT3_MAVAN1119/target/classes\n"
        + "[INFO] -------------------------------------------------------------\n"
        + "[ERROR] COMPILATION ERROR : \n"
        + "[INFO] -------------------------------------------------------------\n"
        + "[ERROR] /var/jenkins_home/workspace/STUDENT3_MAVAN1119/src/"
        + "main/java/selab/myapp/App.java:[9,9] not a statement\n"
        + "[INFO] 1 error\n"
        + "[INFO] --------------------------------------------------\n"
        + "[INFO] -----------------------------------------------------------\n"
        + "[INFO] BUILD FAILURE\n"
        + "[INFO] ---------------------------------------------\n"
        + "[INFO] Total time: 0.891 s\n"
        + "[INFO] Finished at: 2019-11-19T01:07:23+00:00\n"
        + "[INFO] Final Memory: 10M/191M\n"
        + "[INFO] ------------------------------------------------------\n"
        + "[ERROR] Failed to execute goal org.apache.maven.plugins:"
        + "maven-compiler-plugin:3.6.1:compile (default-compile) on "
        + "project HelloMaven: Compilation failure\n"
        + "[ERROR] /var/jenkins_home/workspace/STUDENT3_MAVAN1119/"
        + "src/main/java/selab/myapp/App.java:[9,9] not a statement\n"
        + "[ERROR] -> [Help 1]\n"
        + "[ERROR] \n"
        + "[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.\n"
        + "[ERROR] Re-run Maven using the -X switch to enable full debug logging.\n"
        + "[ERROR] \n"
        + "[ERROR] For more information about the errors and possibl"
        + "e solutions, please read the following articles:\n"
        + "[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/"
        + "MAVEN/MojoFailureException\n"
        + "Build step 'Run with timeout' marked build as failure";
    return test;
  }

}
