package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class PythonUnitTestFailure implements Status  {
  @Override
  public String extractFailureMsg(String consoleText) {
    return null;
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    return null;
  }
}
