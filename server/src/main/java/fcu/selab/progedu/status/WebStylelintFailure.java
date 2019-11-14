package fcu.selab.progedu.status;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fcu.selab.progedu.data.FeedBack;

public class WebStylelintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "> stylelint";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart) + checkstyleStart.length();
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    int nextrow = checkstyleInfo.indexOf("\n");
    checkstyleInfo = checkstyleInfo.substring(nextrow + 1,end - start);

    return checkstyleInfo;
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    int endIndex = consoleText.length();
    List<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("✖") != -1) {
      int crossIndex = consoleText.indexOf("✖");
      int nextrowIndex = consoleText.indexOf("\n");
      if (crossIndex > nextrowIndex) {
        consoleText = consoleText.substring(nextrowIndex + 1, endIndex);
        endIndex = endIndex - nextrowIndex - 1;
      } else {
        int errorStyleStart = consoleText.indexOf("  ", crossIndex + 2);
        feedbacklist.add(new FeedBack(
            "Stylelint",
            consoleText.substring(0, crossIndex - 1).trim(),
            consoleText.substring(crossIndex + 1, errorStyleStart + 1).trim(),
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
    String test = "> node_sample@1.0.0 stylelint /var/jenkins_home/workspace/STUDENT3_1024WEB\n"
        + "> stylelint ./src/web/\n"
        + "\n"
        + "\n"
        + "src/web/html/index.html\n"
        + " 7:9   ✖  Expected indentation of 4 spaces                                 "
        + "indentation                     \n"
        + " 7:12  ✖  Expected single space after \"{\" of a single-line block           "
        + "block-opening-brace-space-after \n"
        + " 7:18  ✖  Expected single space after \":\" with a single-line declaration   "
        + "declaration-colon-space-after   \n"
        + " 7:21  ✖  Expected single space before \"}\" of a single-line block          "
        + "block-closing-brace-space-before\n"
        + "\n"
        + "src/web/html/index2.html\n"
        + " 7:88   ✖  Expected indentation of 4 spaces                                 "
        + "indentation                     \n"
        + " 7:92  ✖  Expected single space after \"{\" of a single-line block           "
        + "block-opening-brace-space-after \n"
        + " 7:58  ✖  Expected single space after \":\" with a single-line declaration   "
        + "declaration-colon-space-after   \n"
        + " 7:21  ✖  Expected single space before \"}\" of a single-line block          "
        + "block-closing-brace-space-before\n"
        + "\n"
        + "npm ERR! code ELIFECYCLE\n"
        + "npm ERR! errno 2";
    return test;
  }
}
