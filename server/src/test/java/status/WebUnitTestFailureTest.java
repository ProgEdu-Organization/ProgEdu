package status;

import fcu.selab.progedu.status.WebUnitTestFailure;

public class WebUnitTestFailureTest {
  WebUnitTestFailure test = new WebUnitTestFailure();
  String console = test.console();
  String consolea = test.extractFailureMsg(console);
  
}
