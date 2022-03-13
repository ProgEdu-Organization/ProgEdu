package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonCompileFailure implements Status {

   private static final Logger LOGGER = LoggerFactory.getLogger(PythonCompileFailure.class);


   @Override
   public String extractFailureMsg(String consoleText) {
     try {
       String compileInfo;
       String compileInfoStart = "python -m compileall";
       String compileInfoEnd = "[Pipeline] }\n";
       compileInfo = consoleText.substring(consoleText.indexOf(compileInfoStart));
       compileInfo = compileInfo.substring(0, compileInfo.indexOf(compileInfoEnd));
       return compileInfo;
     } catch (Exception e) {
       LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
       LOGGER.error(e.getMessage());
       return "ExtractFailureMsg Method Error";
     }
   }

   @Override
   public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
     ArrayList<FeedBack> feedbackList = new ArrayList<>();

     try {
       Pattern pattern = Pattern.compile("(.*?)(File )(.*?)(line )(\\d{1,4})(\n)(.*?)(\n)(.*?)(\n)(.*?)(\n)");
       Matcher matcher = pattern.matcher(consoleText);
       while (matcher.find()) {
         String fileName = matcher.group(3);
         String line = matcher.group(5);
         String message = matcher.group(7) + matcher.group(8) + matcher.group(9) + matcher.group(10) + matcher.group(11);

         feedbackList.add(new FeedBack(
             StatusEnum.COMPILE_FAILURE, fileName, line, message, "", ""));
       }
       if (feedbackList.isEmpty()) {
         feedbackList.add(
             new FeedBack(StatusEnum.COMPILE_FAILURE,
                 "Please notify teacher or assistant this situation, thank you!", ""));
       }
     } catch (Exception e) {
       feedbackList.add(
           new FeedBack(StatusEnum.COMPILE_FAILURE,
               "UnitTest ArrayList error", e.getMessage()));
     }
     return feedbackList;
   }
}
