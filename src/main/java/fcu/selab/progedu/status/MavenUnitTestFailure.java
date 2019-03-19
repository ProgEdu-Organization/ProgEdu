package fcu.selab.progedu.status;

public class MavenUnitTestFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String unitTest = "";
    String startStr = "Failed tests:";
    String goal = "Tests run:";
    int goalStr = consoleText.indexOf(goal, consoleText.indexOf(goal) + 1);

    unitTest = consoleText.substring(consoleText.indexOf(startStr), goalStr - 1);
    //<, > will be HTML tag, change to the " 
    unitTest = unitTest.replaceAll("<", "\"").replaceAll(">", "\"");
    
    return unitTest.trim();
  }
}