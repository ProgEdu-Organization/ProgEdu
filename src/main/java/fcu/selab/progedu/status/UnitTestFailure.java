package fcu.selab.progedu.status;

public class UnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String consoleTest = "";
    String[] str = consoleText.split("\n");
    int start = 0;
    StringBuilder sb = new StringBuilder();
    if (str != null) {
      for (String s : str) {
        if (!s.startsWith("[INFO]")
            && s.contains("-------------------------------------------------------")
            && start == 0) {
          start = 1;
        } else if (s
            .contains("--------------------------UpdateDbPublisher--------------------------------")
            && start == 1) {
          break;
        }
        if (start == 1) {
          sb.append(s);
          sb.append("\n");
        }
      }
      consoleTest = sb.toString();
    }
    return consoleTest;
  }

}
