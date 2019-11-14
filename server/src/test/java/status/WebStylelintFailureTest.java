package status;

import fcu.selab.progedu.status.WebStylelintFailure;

public class WebStylelintFailureTest {
  public static void main(String[] args) {
    WebStylelintFailure ws = new WebStylelintFailure();
    String test = ws.console();
    test = ws.extractFailureMsg(test);
    System.out.println(test + "\n-------------------");
    test = ws.formatFailureMsg(test);
    System.out.println(test);

  }
}
