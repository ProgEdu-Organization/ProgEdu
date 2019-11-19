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
    int nextRow = consoleText.indexOf("\n");
    int endIndex = consoleText.length();
    consoleText = consoleText.substring(nextRow, endIndex);
    endIndex = endIndex - nextRow - 1;
    ArrayList<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("error:") != -1) {
      int errorIndex = consoleText.indexOf("error:");
      nextRow = consoleText.indexOf("\n");
      if (errorIndex > nextRow) {
        consoleText = consoleText.substring(nextRow + 1, endIndex);
        endIndex = endIndex - nextRow - 1;
      } else {
        feedbacklist.add(new FeedBack(
            StatusEnum.COMPILE_FAILURE,
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
    String test = "using GIT_ASKPASS to set credentials \n"
        + " > git fetch --tags --progress -- http://140.134.26.62:20100/"
        + "STUDENT2/JAVA1118.git +refs/heads/*:refs/remotes/origin/*\n"
        + "skipping resolution of commit remotes/origin/master, since it "
        + "originates from another repository\n"
        + " > git rev-parse refs/remotes/origin/master^{commit} # timeout=10\n"
        + " > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10\n"
        + "Checking out Revision 2fb4ec84ecc73837d38940c88024ed5b1c7c573b"
        + " (refs/remotes/origin/master)\n"
        + " > git config core.sparsecheckout # timeout=10\n"
        + " > git checkout -f 2fb4ec84ecc73837d38940c88024ed5b1c7c573b\n"
        + "Commit message: \"Update HelloWorld.java\"\n"
        + " > git rev-list --no-walk 349699222e13fbf9dd7c4a57431efe320e608fa4 # timeout=10\n"
        + "[STUDENT2_JAVA1118] $ /bin/sh -xe /tmp/jenkins5935095937269347033.sh\n"
        + "+ javac src/HelloWorld.java\n"
        + "src/HelloWorld.java:5: error: not a statement\n"
        + "\t\tcin >> i;\n"
        + "\t\t    ^\n"
        + "src/HelloWorld.java:6: error: not a statement\n"
        + "\t\tcin >> a;\n"
        + "\t\t    ^\n"
        + "src/HelloWorld.java:7: error: not a statement\n"
        + "\t\tcin >> k;\n"
        + "\t\t    ^\n"
        + "src/HelloWorld.java:8: error: not a statement\n"
        + "\t\tcout << j << endl;\n"
        + "\t\t          ^\n"
        + "4 errors\n"
        + "Build step 'Execute shell' marked build as failure";
    return test;
  }
}
