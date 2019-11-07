package fcu.selab.progedu.status;

public class AndroidCompileFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "Task :app:compileDebugJavaWithJavac";
    String feedbackEnd = "Task :app:compileDebugJavaWithJavac FAILED";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
            consoleText.indexOf(feedbackEnd));
    return feedback.trim();
  }
}
