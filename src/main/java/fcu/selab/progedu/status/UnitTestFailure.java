package fcu.selab.progedu.status;

public class UnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String tests = "";
    String unitTest = "";
    String startStr = "-------------------------------------------------------\r\n" + " T E S T S";
    String goal = "tests run:";
    int endStr = consoleText.indexOf(goal, consoleText.indexOf(goal) + goal.length());
    int end = consoleText.indexOf("\n", endStr);
    tests = consoleText.substring(endStr, end);

    String errorsStr = "Errors:";
    String failuresStr = "Failures:";

    int errorsGoal = tests.indexOf(errorsStr) + errorsStr.length();
    int failuresGoal = tests.indexOf(failuresStr) + failuresStr.length();

    String errors = tests.substring(errorsGoal, tests.indexOf(",", errorsGoal)).trim();
    String failures = tests.substring(failuresGoal, tests.indexOf(",", failuresGoal)).trim();

    if (errors.equals("0") && failures.equals("0")) {
      return "";
    }
    unitTest = consoleText.substring(consoleText.indexOf(startStr), end);
    return unitTest;
  }

}
