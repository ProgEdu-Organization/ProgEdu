package fcu.selab.progedu.status;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fcu.selab.progedu.data.FeedBack;

public class WebEslintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "Warning:";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    int endrow = checkstyleInfo.indexOf("\n", checkstyleInfo.indexOf("problems"));
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,endrow);
    checkstyleInfo = checkstyleInfo.replace("/var/jenkins_home/workspace/","");
    return checkstyleInfo.trim();
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    consoleText = consoleText.substring(0, consoleText.indexOf("✖"));
    int endIndex = consoleText.length();
    List<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("error") != -1) {
      int errorIndex = consoleText.indexOf("error");
      int nextrowIndex = consoleText.indexOf("\n");
      if (errorIndex > nextrowIndex) {
        consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
        endIndex = endIndex - nextrowIndex - 1;
      } else {
        int errorStyleStart = consoleText.indexOf("  ", errorIndex + 6);
        feedbacklist.add(new FeedBack(
            "Eslint",
            consoleText.substring(0, errorIndex).trim(),
            consoleText.substring(errorIndex + 5, errorStyleStart).trim(),
            consoleText.substring(errorStyleStart, nextrowIndex).trim(),
            ""
        ));
        consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
        endIndex = endIndex - nextrowIndex - 1;
      }
    }
    Gson gson = new Gson();
    return gson.toJson(feedbacklist).toString();
  }




  public String console() {
    String test = "Warning: React version was set to \"detect\" in eslint-plugin-react settings, "
        + "but the \"react\" package is not installed. Assuming latest React version for linting.\n"
        + "\n"
        + "/var/jenkins_home/workspace/STUDENT3_1024WEB3/src/web/html/index.html\n"
        + "   8:1  error  Unexpected var, use let or const instead  no-var\n"
        + "   8:5  error  'bar' is assigned a value but never used  no-unused-vars\n"
        + "  10:1  error  Unexpected var, use let or const instead  no-var\n"
        + "  10:5  error  'fo' is assigned a value but never used   no-unused-vars\n"
        + "  10:7  error  Operator '=' must be spaced               space-infix-ops\n"
        + "  12:1  error  Unexpected var, use let or const instead  no-var\n"
        + "  12:5  error  'foo' is assigned a value but never used  no-unused-vars\n"
        + "  12:8  error  Operator '=' must be spaced               space-infix-ops\n"
        + "\n"
        + "/var/jenkins_home/workspace/STUDENT3_1024WEB3/src/web/html/index2.html\n"
        + "   9:1  error  Unexpected var, use let or const instead"
        + "                   no-var\n"
        + "   9:5  error  'bar' is assigned a value but never used"
        + "                   no-unused-vars\n"
        + "  10:1  error  More than 2 blank lines not allowed     "
        + "                   no-multiple-empty-lines\n"
        + "  17:1  error  Unexpected var, use let or const instead"
        + "                   no-var\n"
        + "  17:5  error  'foo' is assigned a value but never used"
        + "                   no-unused-vars\n"
        + "  17:8  error  Operator '=' must be spaced              "
        + "                  space-infix-ops\n"
        + "  18:1  error  Too many blank lines at the end of file."
        + " Max of 0 allowed  no-multiple-empty-lines\n"
        + "\n"
        + "✖ 15 problems (15 errors, 0 warnings)\n"
        + "  10 errors and 0 warnings potentially fixable with the `--fix` option.\n"
        + "\n"
        + "npm ERR! code ELIFECYCLE";

    return test;
  }
}
