package fcu.selab.progedu.status;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fcu.selab.progedu.data.FeedBack;

public class WebHtmlhintFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String checkstyleStart = "   /var/jenkins_home/workspace/";
    String checkstyleEnd = "npm ERR! code ELIFECYCLE";
    int start = consoleText.indexOf(checkstyleStart);
    int end = consoleText.lastIndexOf(checkstyleEnd) - 1;

    String checkstyleInfo = consoleText.substring(start,end);
    checkstyleInfo = checkstyleInfo.replaceAll("[\u001B][\\[][\\d]{0,3}[m]", "");
    checkstyleInfo = checkstyleInfo.replaceAll("\\^", " ^");
    checkstyleInfo = checkstyleInfo.replaceAll(checkstyleStart, "");

    return checkstyleInfo.trim();
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    consoleText = consoleText.substring(
        consoleText.indexOf("\n"), consoleText.indexOf("Scanned"));
    int endIndex = consoleText.length();
    List<FeedBack> feedbacklist = new ArrayList<>();
    while (consoleText.indexOf("L") != -1) {
      int lineIndex = consoleText.indexOf("L");
      int sparateIndex = consoleText.indexOf("|");
      int arrowIndex = consoleText.indexOf("^");
      int nextlineIndex = consoleText.indexOf("\n", arrowIndex);
      int dotIndex = consoleText.indexOf(".", arrowIndex);
      String errorStyle = consoleText.substring(dotIndex + 1, nextlineIndex)
          .replace("(", "").replace(")", "").trim();
      feedbacklist.add(new FeedBack(
          "Htmlhint",
          consoleText.substring(lineIndex, sparateIndex - 1).trim(),
          consoleText.substring(arrowIndex + 2,dotIndex).trim(),
          errorStyle,
          ""
      ));
      consoleText = consoleText.substring(nextlineIndex + 1, endIndex);
      endIndex = endIndex - nextlineIndex - 1;
    }
    Gson gson = new Gson();
    return gson.toJson(feedbacklist).toString();
  }



  public String console() {
    String test = "+ npm run htmlhint\n"
        + "\n"
        + "> node_sample@1.0.0 htmlhint /var/jenkins_home/workspace/STUDENT2_1024WEB\n"
        + "> htmlhint ./src/web/\n"
        + "\n"
        + "\n"
        + "   /var/jenkins_home/workspace/STUDENT2_1024WEB/src/web/html/index.html\n"
        + "\u001B[37m      L2 |\u001B[90m<html>\u001B[39m\n"
        + "\u001B[37m          ^ \u001B[31mDoctype must be declared first."
        + " (doctype-first)\u001B[39m\n"
        + "\u001B[37m      L10 |\u001B[90m        <Input name=\"username\""
        + " type=\"text\" />\u001B[39m\n"
        + "\u001B[37m                   ^ \u001B[31mThe html element name of "
        + "[ Input ] must be in lowercase. (tagname-lowercase)\u001B[39m\n"
        + "\u001B[37m      L11 |\u001B[90m        <input name='password' "
        + "type=\"password\" />\u001B[39m\n"
        + "\u001B[37m                         ^ \u001B[31mThe value of attribute "
        + "[ name ] must be in double quotes. (attr-value-double-quotes)\u001B[39m\n"
        + "\u001B[37m      L12 |\u001B[90m...    <input name=\"continue\" "
        + "type=\"submit\" Value=\"Login\" />\u001B[39m\n"
        + "\u001B[37m                                                      "
        + "^ \u001B[31mThe attribute name of [ Value ] must be in lowercase. "
        + "(attr-lowercase)\u001B[39m\n"
        + "\n"
        + "   /var/jenkins_home/workspace/STUDENT2_1024WEB/src/web/html/index2.html\n"
        + "\u001B[37m      L2 |\u001B[90m<html>\u001B[39m\n"
        + "\u001B[37m          ^ \u001B[31mDoctype must be declared first."
        + " (doctype-first)\u001B[39m\n"
        + "\u001B[37m      L10 |\u001B[90m        <Input name=\"username\""
        + " type=\"text\" />\u001B[39m\n"
        + "\u001B[37m                   ^ \u001B[31mThe html element name of "
        + "[ Input ] must be in lowercase. (tagname-lowercase)\u001B[39m\n"
        + "\u001B[37m      L11 |\u001B[90m        <input name='password' "
        + "type=\"password\" />\u001B[39m\n"
        + "\u001B[37m                         ^ \u001B[31mThe value of attribute "
        + "[ name ] must be in double quotes. (attr-value-double-quotes)\u001B[39m\n"
        + "\u001B[37m      L12 |\u001B[90m...    <input name=\"continue\" "
        + "type=\"submit\" Value=\"Login\" />\u001B[39m\n"
        + "\u001B[37m                                                      "
        + "^ \u001B[31mThe attribute name of [ Value ] must be in lowercase. "
        + "(attr-lowercase)\u001B[39m\n"
        + "\n"
        + "Scanned 2 files, found 4 errors in 1 files (16 ms)\n"
        + "npm ERR! code ELIFECYCLE\n"
        + "npm ERR! errno 1\n"
        + "npm ERR! node_sample@1.0.0 htmlhint: `htmlhint ./src/web/`\n"
        + "npm ERR! Exit status 1\n"
        + "npm ERR! \n"
        + "npm ERR! Failed at the node_sample@1.0.0 htmlhint script.\n"
        + "npm ERR! This is probably not a problem with npm. There is likely "
        + "additional logging output above.";

    return test;
  }
}
