package fcu.selab.progedu.status;

import javax.json.Json;

public class BuildSuccess implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    return "Success";
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    return "Success";
  }

}
