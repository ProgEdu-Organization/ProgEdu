package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

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
    return null;
  }

  @Override
  public String toJson(ArrayList<FeedBack> arrayList) {
    return "Instructor Commit";
  }
}
