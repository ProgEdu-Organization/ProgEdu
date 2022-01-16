package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PythonCompileFailure implements Status {

   private static final Logger LOGGER = LoggerFactory.getLogger(PythonCompileFailure.class);


   @Override
   public String extractFailureMsg(String consoleText) {
     return null;
   }

   @Override
   public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
     return null;
   }
}
