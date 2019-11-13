package status;

import fcu.selab.progedu.status.WebHtmlhintFailure;

public class WebHtmlhintFailureTest {
  public static void main(String[] args) {
    WebHtmlhintFailure wh = new WebHtmlhintFailure();
    String test = wh.console();
    test = wh.extractFailureMsg(test);
    test = wh.formatFailureMsg(test);

    System.out.println(test);
  }
}
