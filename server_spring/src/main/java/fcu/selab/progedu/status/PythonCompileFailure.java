package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PythonCompileFailure implements Status {

   private static final Logger LOGGER = LoggerFactory.getLogger(PythonCompileFailure.class);


   @Override
   public String extractFailureMsg(String consoleText) {
     try {
       String compileInfo;
       String compileInfoStart = "python -m compileall";
       String compileInfoEnd = "[Pipeline] }\n";
       compileInfoInfo = consoleText.substring(consoleText.indexOf(checkstyleStart));
       compileInfoInfo = checkstyleInfo.substring(0, checkstyleInfo.indexOf(checkstyleEnd));
       return compileInfo;
     } catch (Exception e) {
       LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
       LOGGER.error(e.getMessage());
       return "ExtractFailureMsg Method Error";
     }
   }

   @Override
   public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
     return null;
   }
}
