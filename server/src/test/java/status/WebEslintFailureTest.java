package status;

import fcu.selab.progedu.status.WebEslintFailure;

public class WebEslintFailureTest {
  public static void main(String[] args) {
    WebEslintFailure we = new WebEslintFailure();
    String test = we.console();
    test = we.extractFailureMsg(test);
    test = we.formatFailureMsg(test);
    System.out.println("-----------------\n" + test);
  }
}
