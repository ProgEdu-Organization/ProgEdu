package status;

import fcu.selab.progedu.status.WebUnitTestFailure;

public class WebUnitTestFailureTest {
  public static void main(String[] args) {

    WebUnitTestFailure test = new WebUnitTestFailure();
    String console = test.console();
    console = test.formatFailureMsg(console);

    System.out.println( "123132\n" + console);
  }
  
}
