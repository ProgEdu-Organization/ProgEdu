package fcu.selab.progedu.status;

import javax.json.Json;

public interface Status {
  public String extractFailureMsg(String consoleText);

  public String formatFailureMsg(String consoleText);
}
