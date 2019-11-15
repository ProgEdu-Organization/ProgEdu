package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class MavenCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "[INFO] Scanning for projects...";
    String feedbackEnd = "Build step";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
        consoleText.indexOf(feedbackEnd));
    return feedback.trim();
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
