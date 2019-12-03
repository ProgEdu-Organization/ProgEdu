package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;

import java.util.ArrayList;

public class AndroidCheckstyleFailure implements Status {
  @Override
  public String extractFailureMsg(String consoleText) {
    String feedback;
    String feedbackStart = "> Task :app:checkStyle FAILED";
    String feedbackEnd = "FAILURE: Build failed with an exception.";
    feedback = consoleText.substring(consoleText.indexOf(feedbackStart) + feedbackStart.length(),
            consoleText.indexOf(feedbackEnd) + 1);
    /**
     * Remove /var/jenkins_home/workspace/
     */
    feedback = feedback.replaceAll("/var/jenkins_home/workspace/", "");
    return feedback.replaceAll("/var/jenkins_home/workspace/", "").trim();
  }
  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    try{
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      int endIndex = consoleText.length();
      while(consoleText.contains("[ERROR]")){
        int error = consoleText.indexOf("[ERROR]") + "[ERROR]".length() ;
        int nextrow = consoleText.indexOf("\n");
        int space = consoleText.indexOf(" ", error + 1);
        // Find the last left bracket for Symptom
        int lastLeftBracket = consoleText.indexOf("[", space);
        while(consoleText.indexOf("[", lastLeftBracket + 1) > 0
                && consoleText.indexOf("[", lastLeftBracket + 1) < nextrow){
          lastLeftBracket = consoleText.indexOf("[", lastLeftBracket + 1);
        }
        feedbacklist.add(new FeedBack(
                StatusEnum.CHECKSTYLE_FAILURE,
                consoleText.substring(error + 1, space - 1).trim(),
                consoleText.substring(space + 1, lastLeftBracket).trim(),
                consoleText.substring(lastLeftBracket, nextrow).trim(),
                "https://github.com/checkstyle/checkstyle"
        ));
        consoleText = consoleText.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      }
      return feedbacklist;
    }catch (Exception e){
      ArrayList<FeedBack> feedbacklist = new ArrayList<>();
      feedbacklist.add(
              new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "",
                      consoleText, "", ""));
      return feedbacklist;
    }
  }
}
