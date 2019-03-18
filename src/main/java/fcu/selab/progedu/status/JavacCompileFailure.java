package fcu.selab.progedu.status;

public class JavacCompileFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "+ javac";
    String feedbackEnd = "Build step 'Execute shell' marked build as failure";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart),
        consoleText.indexOf(feedbackEnd));
    return feedback.trim();

  }
}
