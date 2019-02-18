package fcu.selab.progedu.status;

public class UnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String tests = "";
    String unitTest = "";
    String startStr = "------------------------------------------------------\n" + " T E S T S";
    String goal = "Tests run:";
    int testStr = consoleText.indexOf(goal);
    int testEnd = consoleText.indexOf("\n", testStr);
    tests = consoleText.substring(testStr, testEnd);

    String errorsStr = "Errors:";
    String failuresStr = "Failures:";
    int errorsGoal = tests.indexOf(errorsStr) + errorsStr.length();
    int failuresGoal = tests.indexOf(failuresStr) + failuresStr.length();
    String errors = tests.substring(errorsGoal, tests.indexOf(",", errorsGoal)).trim();
    String failures = tests.substring(failuresGoal, tests.indexOf(",", failuresGoal)).trim();
    if (errors.equals("0") && failures.equals("0")) {
      return null;
    }

    // ´M§ä²Ä¤G­ÓTests run:
    int end = consoleText.indexOf(goal, consoleText.indexOf("\n", testEnd));
    unitTest = consoleText.substring(consoleText.indexOf(startStr), end);
    return unitTest;
  }

}