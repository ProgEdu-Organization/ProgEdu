package fcu.selab.progedu.status;

public class AndroidCheckstyleFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "Task :app:checkStyle";
    String feedbackEnd = "Task :app:checkStyle FAILED";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd));
    return feedback.trim();
  }
}