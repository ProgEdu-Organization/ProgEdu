package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class WebCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    return "WebCompileFailure";
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
